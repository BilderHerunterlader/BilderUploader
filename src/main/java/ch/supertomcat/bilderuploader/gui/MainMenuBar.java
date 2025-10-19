package ch.supertomcat.bilderuploader.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ch.supertomcat.bilderuploader.gui.renderer.HosterComboBoxRenderer;
import ch.supertomcat.bilderuploader.gui.settings.SettingsDialog;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.supertomcatutils.application.ApplicationMain;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.gui.FileExplorerUtil;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;

/**
 * Main Menu Bar
 */
public class MainMenuBar {
	/**
	 * MenuBar
	 */
	private JMenuBar mb = new JMenuBar();

	/**
	 * Menu
	 */
	private JMenu menuFile = new JMenu(Localization.getString("File"));

	/**
	 * MenuItem
	 */
	private JMenuItem itemExit = new JMenuItem(Localization.getString("Exit"), Icons.getTangoIcon("actions/system-log-out.png", 16));

	/**
	 * Menu
	 */
	private JMenu menuSettings = new JMenu(Localization.getString("Settings"));

	/**
	 * MenuItem
	 */
	private JMenuItem itemSettings = new JMenuItem(Localization.getString("Settings"), Icons.getTangoIcon("categories/preferences-system.png", 16));

	/**
	 * Menu
	 */
	private JMenu menuHelp = new JMenu(Localization.getString("Help"));

	/**
	 * MenuItem
	 */
	private JMenuItem itemLogFolder = new JMenuItem(Localization.getString("OpenLogFolder"), Icons.getTangoIcon("places/folder.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem itemTutorial = new JMenuItem(Localization.getString("Tutorial"), Icons.getTangoIcon("apps/internet-web-browser.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem itemAbout = new JMenuItem(Localization.getString("About"), Icons.getTangoIcon("apps/help-browser.png", 16));

	/**
	 * Hoster ComboBox
	 */
	private JComboBox<Hoster> cmbHoster = new JComboBox<>();

	/**
	 * Constructor
	 * 
	 * @param mainWindow Main Window
	 * @param settingsManager Settings Manager
	 * @param proxyManager Proxy Manager
	 * @param hosterManager Hoster Manager
	 * @param listeners Main Window Listeners
	 */
	public MainMenuBar(MainWindow mainWindow, SettingsManager settingsManager, ProxyManager proxyManager, HosterManager hosterManager, List<MainWindowListener> listeners) {
		menuFile.add(itemExit);

		menuSettings.add(itemSettings);

		menuHelp.add(itemLogFolder);
		menuHelp.add(itemTutorial);
		menuHelp.add(itemAbout);

		itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		itemSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.SHIFT_MASK));
		itemTutorial.setAccelerator(KeyStroke.getKeyStroke("F1"));

		itemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.dispose();
			}
		});

		itemSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsDialog(mainWindow, settingsManager, proxyManager);
			}
		});

		itemLogFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Path logDir = Paths.get(ApplicationProperties.getProperty(ApplicationMain.LOGS_PATH));
				FileExplorerUtil.openDirectory(logDir);
			}
		});

		itemTutorial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String url = ApplicationProperties.getProperty("TutorialURL");
				FileExplorerUtil.openURL(url);
			}
		});

		itemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new BUAboutDialog(mainWindow);
			}
		});

		List<Hoster> hosts = hosterManager.getHosts();
		for (Hoster hoster : hosts) {
			cmbHoster.addItem(hoster);
		}

		String lastSelectedHoster = settingsManager.getGUISettings().getSelectedHoster();
		if (lastSelectedHoster != null) {
			Hoster selectedHoster = hosterManager.getHostByName(lastSelectedHoster);
			if (selectedHoster != null) {
				cmbHoster.setSelectedItem(selectedHoster);
				hosterManager.setSelectedHoster(selectedHoster);
			} else {
				hosterManager.setSelectedHoster((Hoster)cmbHoster.getSelectedItem());
			}
		} else {
			hosterManager.setSelectedHoster((Hoster)cmbHoster.getSelectedItem());
		}

		cmbHoster.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Hoster selectedHoster = (Hoster)e.getItem();
					if (selectedHoster != null) {
						hosterManager.setSelectedHoster(selectedHoster);
						settingsManager.getGUISettings().setSelectedHoster(selectedHoster.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		cmbHoster.setRenderer(new HosterComboBoxRenderer());
		cmbHoster.setFocusable(false);

		mb.add(menuFile);
		mb.add(menuSettings);
		mb.add(menuHelp);
		mb.add(Box.createGlue());
		mb.add(cmbHoster);
		mb.add(Box.createGlue());
	}

	/**
	 * @return JMenuBar
	 */
	public JMenuBar getJMenuBar() {
		return mb;
	}
}
