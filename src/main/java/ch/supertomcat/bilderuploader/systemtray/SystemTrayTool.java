package ch.supertomcat.bilderuploader.systemtray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.MainWindow;
import ch.supertomcat.bilderuploader.gui.MainWindowListener;
import ch.supertomcat.bilderuploader.settings.BUSettingsListener;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.supertomcatutils.gui.Icons;

/**
 * Class which handles the SystemTray
 */
public class SystemTrayTool {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Main Window
	 */
	private MainWindow mainWindow;

	/**
	 * Popupmenu
	 */
	private PopupMenu popup = new PopupMenu();

	/**
	 * Menuitem
	 */
	private MenuItem itemOpen = new MenuItem("Open");

	/**
	 * Menuitem
	 */
	private MenuItem itemExit = new MenuItem("Exit");

	/**
	 * Tray-Icon-Image
	 */
	private Image image = Icons.getImage("/ch/supertomcat/bilderuploader/gui/icons/BilderUploader-128x128.png");

	/**
	 * Tray-Icon
	 */
	private TrayIcon trayIcon = null;

	/**
	 * SystemTray
	 */
	private SystemTray tray = null;

	/**
	 * Flag if notifications should be displayed
	 */
	private boolean displayNotifications;

	/**
	 * Constructor
	 * 
	 * @param mainWindow Main Window
	 * @param settingsManager Settings Manager
	 */
	public SystemTrayTool(MainWindow mainWindow, SettingsManager settingsManager) {
		this.mainWindow = mainWindow;
		this.displayNotifications = settingsManager.getGUISettings().isUploadsCompleteNotification();
		settingsManager.addSettingsListener(new BUSettingsListener() {

			@Override
			public void settingsChanged() {
				displayNotifications = settingsManager.getGUISettings().isUploadsCompleteNotification();
			}

			@Override
			public void lookAndFeelChanged() {
				// Nothing to do
			}
		});
	}

	/**
	 * Initializes the SystemTray, but does not show the trayicon
	 */
	public void init() {
		if (SystemTray.isSupported()) {
			// Add menuitems to the popup
			popup.add(itemOpen);
			popup.add(itemExit);

			itemOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainWindow.setVisible(true);
					mainWindow.toFront();
				}
			});

			itemExit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainWindow.dispose();
				}
			});

			// Create the TrayIcon
			trayIcon = new TrayIcon(image, "", popup);
			trayIcon.setImageAutoSize(true);

			// Get the SystemTray
			tray = SystemTray.getSystemTray();

			mainWindow.addListener(new MainWindowListener() {
				@Override
				public void exitApplication() {
					// Nothing to do
				}
			});
		}
	}

	/**
	 * Now, here we add the icon to the SystemTray and register Listeners for the menuitems
	 */
	public void showTrayIcon() {
		if (SystemTray.isSupported()) {
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1 && e.getButton() == 1) {
						if (!mainWindow.isVisible()) {
							mainWindow.setVisible(true);
							mainWindow.toFront();
						} else {
							mainWindow.setVisible(false);
						}
					}
				}
			});

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				logger.error("Could not add try icon", e);
			}
		}
	}

	/**
	 * Removes the icon from the SystemTray
	 */
	public void remove() {
		tray.remove(trayIcon);
	}

	/**
	 * Display Notification Message
	 * 
	 * TODO Implement listener from queuemanager to detect downloads complete
	 * 
	 * @param title Title
	 * @param message Message
	 */
	private void displayMessage(String title, String message) {
		trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
	}

	/**
	 * Checks if SystemTray is supported or not
	 * 
	 * @return True if supported
	 */
	public static boolean isTraySupported() {
		return SystemTray.isSupported();
	}
}
