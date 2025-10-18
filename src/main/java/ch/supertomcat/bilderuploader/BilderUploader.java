package ch.supertomcat.bilderuploader;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.MainWindow;
import ch.supertomcat.bilderuploader.gui.MainWindowListener;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.queue.QueueManager;
import ch.supertomcat.bilderuploader.queue.UploadQueueManager;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.settingsconfig.LookAndFeelSetting;
import ch.supertomcat.bilderuploader.systemtray.SystemTrayTool;
import ch.supertomcat.bilderuploader.templates.TemplateManager;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParserManager;
import ch.supertomcat.supertomcatutils.application.ApplicationMain;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.application.ApplicationUtil;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import jakarta.xml.bind.JAXBException;

/**
 * Class which contains the main-Method
 */
public class BilderUploader {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Main Window
	 */
	private MainWindow mainWindow;

	/**
	 * SystemTray
	 */
	private SystemTrayTool stt = null;

	/**
	 * Upload Queue Manager
	 */
	private UploadQueueManager uploadQueueManager = null;

	/**
	 * Constructor
	 * 
	 * @throws JAXBException
	 */
	public BilderUploader() throws JAXBException {
		SettingsManager settingsManager = new SettingsManager(ApplicationProperties.getProperty(ApplicationMain.SETTINGS_PATH), "settings.xml");

		// Read the settings from settings file
		settingsManager.readSettings();
		if (settingsManager.isLanguageFirstRun()) {
			// If the application is started at first time, the user must select the language
			String options[] = { "English", "Deutsch" };
			// Display a frame, so that BH already shows up in the taskbar and can be switched to. Otherwise the user might not see that there was a dialog open
			JFrame frame = null;
			try {
				frame = ApplicationUtil.createInvisibleFrame("BU", Icons.getImage("/ch/supertomcat/bilderuploader/gui/icons/BilderUploader-16x16.png"));
				int ret = JOptionPane.showOptionDialog(frame, "Choose a language", "Language", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (ret == 0) {
					settingsManager.setLanguage("en_EN");
				} else if (ret == 1) {
					settingsManager.setLanguage("de_DE");
				}
				settingsManager.writeSettings(true);
			} finally {
				if (frame != null) {
					frame.dispose();
				}
			}
		}

		/*
		 * No try to change the look and feel if needed
		 */
		try {
			LookAndFeelSetting lookAndFeelSetting = settingsManager.getGUISettings().getLookAndFeel();
			UIManager.setLookAndFeel(SettingsManager.getLookAndFeelClassName(lookAndFeelSetting));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			logger.error("Could not set LookAndFeel", e);
		}

		// Initialize the localized Strings
		String language = "en";
		String country = "EN";
		if ("de_DE".equals(settingsManager.getGUISettings().getLanguage())) {
			language = "de";
			country = "DE";
		}
		Localization.init("ch.supertomcat.bilderuploader.BilderUploader", language, country);

		ProxyManager proxyManager = new ProxyManager(settingsManager);
		HosterManager hosterManager = new HosterManager();

		String lastSelectedHoster = settingsManager.getGUISettings().getSelectedHoster();
		if (lastSelectedHoster != null) {
			Hoster hoster = hosterManager.getSelectedHoster();
			if (hoster != null) {
				hosterManager.setSelectedHoster(hoster);
			}
		}

		TemplateManager templateManager = new TemplateManager(null);
		TitleFilenameParserManager titleFilenameParserManager = new TitleFilenameParserManager();
		QueueManager queueManager = new QueueManager(settingsManager, hosterManager);
		uploadQueueManager = new UploadQueueManager(queueManager, settingsManager, proxyManager);

		boolean systemTray = SystemTrayTool.isTraySupported();

		mainWindow = new MainWindow(hosterManager, templateManager, titleFilenameParserManager, settingsManager, proxyManager, queueManager, uploadQueueManager, systemTray);
		mainWindow.addListener(new MainWindowListener() {
			@Override
			public void exitApplication() {
				exit();
			}
		});

		if (systemTray) {
			stt = new SystemTrayTool(mainWindow, settingsManager);
			stt.init();
			mainWindow.setVisible(true);
			stt.showTrayIcon();
		} else {
			// If SystemTray is not used bring the main window to the front and request the focus
			mainWindow.setVisible(true);
			mainWindow.toFront();
			mainWindow.requestFocus();
		}
	}

	/**
	 * Exit
	 */
	private synchronized void exit() {
		if (uploadQueueManager != null) {
			uploadQueueManager.stop();
		}
		if (stt != null) {
			stt.remove();
			stt = null;
		}
	}

	/**
	 * Main-Method
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		List<String> additionalPaths = Arrays.asList("DatabasePath", "SettingsPath", "UploadLogPath");
		ApplicationMain applicationMain = new ApplicationMain("BU", null, true, true, BilderUploader.class, additionalPaths) {
			/**
			 * BU
			 */
			private BilderUploader bu = null;

			@Override
			protected void main(String[] args) {
				try {
					bu = new BilderUploader();

					// Create and register the Shutdown-Thread
					addDefaultShutdownHook();
				} catch (Exception e) {
					LoggerFactory.getLogger(BilderUploader.class).error("Could not initialized BilderUploader", e);
					displayStartupError("BilderUploader could not be started!: " + e.getMessage());
					System.exit(1);
				}
			}

			@Override
			protected void shutdownHookExit() {
				if (bu != null) {
					bu.exit();
				}
			}
		};
		applicationMain.start(args);
	}
}
