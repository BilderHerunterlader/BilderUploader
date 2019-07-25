package ch.supertomcat.bilderuploader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXBException;

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
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.application.ApplicationUtil;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.io.FileUtil;

/**
 * Class which contains the main-Method
 */
public class BilderUploader {
	/**
	 * Path of the folder which contains the lockfile
	 */
	private static String strLockFilePath = System.getProperty("user.home") + FileUtil.FILE_SEPERATOR + ".BU" + FileUtil.FILE_SEPERATOR;

	/**
	 * Path and filename of the lockfile
	 */
	private static String strLockFilename = "BU.lock";

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
	 * Shutdown-Thread
	 */
	private Thread shutdownThread = null;

	/**
	 * Constructor
	 * 
	 * @throws JAXBException
	 */
	public BilderUploader() throws JAXBException {
		SettingsManager settingsManager = new SettingsManager(ApplicationProperties.getProperty("SettingsPath"), "settings.xml");

		// Read the settings from settings file
		settingsManager.readSettings();
		if (settingsManager.isLanguageFirstRun()) {
			// If the application is started at first time, the user must select the language
			String options[] = { "English", "Deutsch" };
			// Display a frame, so that BH already shows up in the taskbar and can be switched to. Otherwise the user might not see that there was a dialog open
			JFrame frame = null;
			try {
				frame = createInvisibleFrame();
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
		UploadQueueManager uploadQueueManager = new UploadQueueManager(queueManager, settingsManager, proxyManager);

		boolean systemTray = SystemTrayTool.isTraySupported();

		mainWindow = new MainWindow(hosterManager, templateManager, titleFilenameParserManager, settingsManager, proxyManager, queueManager, uploadQueueManager, systemTray);
		mainWindow.addListener(new MainWindowListener() {
			@Override
			public void exitApplication() {
				uploadQueueManager.stop();
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

		// Create and register the Shutdown-Thread
		shutdownThread = new Thread("Shutdown-Thread") {
			@Override
			public void run() {
				exit();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}

	/**
	 * Exit
	 */
	private synchronized void exit() {
		if (stt != null) {
			stt.remove();
			stt = null;
		}
	}

	private static JFrame createInvisibleFrame() {
		JFrame frame = new JFrame("BU");
		frame.setIconImage(Icons.getImage("/ch/supertomcat/bilderuploader/gui/icons/BilderUploader-16x16.png"));
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	private static void parseCommandLine(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-version")) {
				System.out.print(ApplicationProperties.getProperty("ApplicationVersion"));
				System.exit(0);
			} else if (arg.equalsIgnoreCase("-versionNumber")) {
				System.out.print(ApplicationProperties.getProperty("ApplicationVersion").replaceAll("\\.", ""));
				System.exit(0);
			} else if (arg.equalsIgnoreCase("-help")) {
				String help = ApplicationProperties.getProperty("ApplicationName") + " v" + ApplicationProperties.getProperty("ApplicationVersion") + "\n\n";
				help += "Command Line Arguments:\n";
				help += "-version\t\tPrints the Version of BH (e.g. 1.2.0)\n\n";
				help += "-versionNumber\t\tPrints the VersionNumber of BH (e.g. 120)\n\n";
				System.out.print(help);
				System.exit(0);
			}
		}
	}

	/**
	 * The user can override the path of some directories, such as
	 * Download-Path, Settings-Path and so on.
	 * There must only be a textfile called directories.txt in
	 * the programm folder.
	 * 
	 * A line in the file must look like this
	 * Name Path
	 * Name and Path must be seperated by a tab.
	 * 
	 * Available Names:
	 * Database
	 * Settings
	 * Download-Log
	 * Logs
	 * Downloads
	 * 
	 * @throws IOException
	 */
	private static void readDirectoriesFile() throws IOException {
		File file = new File(ApplicationProperties.getProperty("ApplicationPath") + "directories.properties");
		if (file.exists() == false) {
			return;
		}
		String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

		Properties directoriesProperties = new Properties();
		// We do the replace, because load will treat backslashes as escape characters
		directoriesProperties.load(new StringReader(content.replace("\\", "\\\\")));

		String databaseDir = directoriesProperties.getProperty("DatabasePath");
		String settingsDir = directoriesProperties.getProperty("SettingsPath");
		String uploadLogDir = directoriesProperties.getProperty("UploadLogPath");
		String logsDir = directoriesProperties.getProperty("LogsPath");

		if (databaseDir != null && !databaseDir.isEmpty()) {
			ApplicationProperties.setProperty("DatabasePath", databaseDir);
		}
		if (settingsDir != null && !settingsDir.isEmpty()) {
			ApplicationProperties.setProperty("SettingsPath", settingsDir);
		}
		if (uploadLogDir != null && !uploadLogDir.isEmpty()) {
			ApplicationProperties.setProperty("UploadLogPath", uploadLogDir);
		}
		if (logsDir != null && !logsDir.isEmpty()) {
			ApplicationProperties.setProperty("LogsPath", logsDir);
		}
	}

	/**
	 * Format Stacktrace to String
	 * 
	 * @param throwable Throwable
	 * @return Stacktrace as String
	 */
	private static String formatStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	/**
	 * Main-Method
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		try {
			ApplicationProperties.initProperties(BilderUploader.class.getResourceAsStream("/Application_Config.properties"));
		} catch (IOException e) {
			// Logger is not initialized at this point
			System.err.println("Could not initialize application properties");
			e.printStackTrace();
			ApplicationUtil.writeBasicErrorLogfile(new File("BU-Error.log"), "Could not initialize application properties:\n" + formatStackTrace(e));
			System.exit(1);
		}

		String jarFilename = ApplicationUtil.getThisApplicationsJarFilename(BilderUploader.class);
		ApplicationProperties.setProperty("JarFilename", jarFilename);

		// Geth the program directory
		String appPath = ApplicationUtil.getThisApplicationsPath(!jarFilename.isEmpty() ? jarFilename : ApplicationProperties.getProperty("ApplicationShortName") + ".jar");
		ApplicationProperties.setProperty("ApplicationPath", appPath);

		String programUserDir = System.getProperty("user.home") + FileUtil.FILE_SEPERATOR + "." + ApplicationProperties.getProperty("ApplicationShortName") + FileUtil.FILE_SEPERATOR;
		ApplicationProperties.setProperty("ProfilePath", programUserDir);
		ApplicationProperties.setProperty("DatabasePath", programUserDir);
		ApplicationProperties.setProperty("SettingsPath", programUserDir);
		ApplicationProperties.setProperty("UploadLogPath", programUserDir);
		ApplicationProperties.setProperty("LogsPath", programUserDir);

		// Parse Command Line
		parseCommandLine(args);

		/*
		 * read the directories.txt from program folder if exists and
		 * override paths of BH when definded in the file
		 */
		try {
			readDirectoriesFile();
		} catch (IOException e) {
			// Logger is not initialized at this point
			System.err.println("Could not read directories.properties");
			e.printStackTrace();
			ApplicationUtil.writeBasicErrorLogfile(new File("BU-Error.log"), "Could not read directories.properties:\n" + formatStackTrace(e));
			System.exit(1);
		}

		String logFilename = ApplicationProperties.getProperty("ApplicationShortName") + ".log";
		// Loggers can be created after this point
		System.setProperty("bhlog4jlogfile", programUserDir + FileUtil.FILE_SEPERATOR + logFilename);

		ApplicationUtil.initializeSLF4JUncaughtExceptionHandler();

		/*
		 * Now try to lock the file
		 * We do this, to make sure, there is only one instance of BH runnig.
		 */
		if (ApplicationUtil.lockLockFile(strLockFilePath, strLockFilename) == false) {
			// Display a frame, so that BU already shows up in the taskbar and can be switched to. Otherwise the user might not see that there was a dialog open
			JFrame frame = null;
			try {
				frame = createInvisibleFrame();
				// If the lockfile could not locked, we display a error-message and exit
				JOptionPane.showMessageDialog(frame, "Another Instance of the Application is running. Application is terminating.", "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (frame != null) {
					frame.dispose();
				}
			}
			System.exit(0);
		}

		// Write some useful info to the logfile
		ApplicationUtil.logApplicationInfo();

		// Delete old log files
		ApplicationUtil.deleteOldLogFiles(7, logFilename, ApplicationProperties.getProperty("LogsPath"));

		try {
			new BilderUploader();
		} catch (Exception e) {
			LoggerFactory.getLogger(BilderUploader.class).error("Could not initialized BilderUploader", e);
			JFrame frame = null;
			try {
				frame = createInvisibleFrame();
				JOptionPane.showMessageDialog(frame, "BilderUploader could not be started!: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (frame != null) {
					frame.dispose();
				}
			}
			System.exit(1);
		}
	}
}
