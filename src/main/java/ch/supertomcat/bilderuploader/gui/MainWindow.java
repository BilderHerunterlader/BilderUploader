package ch.supertomcat.bilderuploader.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.hoster.HosterPanel;
import ch.supertomcat.bilderuploader.gui.queue.QueuePanel;
import ch.supertomcat.bilderuploader.gui.templates.TemplatesPanel;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.queue.QueueManager;
import ch.supertomcat.bilderuploader.queue.UploadQueueManager;
import ch.supertomcat.bilderuploader.settings.BUSettingsListener;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.settingsconfig.WindowSettings;
import ch.supertomcat.bilderuploader.templates.TemplateManager;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParserManager;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.supertomcatutils.application.ApplicationMain;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.progress.ProgressObserver;

/**
 * Main Window
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Tabpane
	 */
	private JTabbedPane tabs = new JTabbedPane();

	/**
	 * mainMenuBar
	 */
	private MainMenuBar mainMenuBar;

	/**
	 * mainProgressPopup
	 */
	private MainProgressPopup mainProgressPopup = new MainProgressPopup();

	/**
	 * Label
	 */
	private JLabel lblMessage = new JLabel(Localization.getString("BHStarted"));

	/**
	 * Panel
	 */
	private JPanel pnlMessage = new JPanel();

	private JLabel lblProgress = new JLabel("");

	private String windowTitlePrefix = ApplicationProperties.getProperty(ApplicationMain.APPLICATION_SHORT_NAME) + " - ";
	private String windowTitleSuffix = " (" + ApplicationProperties.getProperty(ApplicationMain.APPLICATION_VERSION) + ")";

	/**
	 * Listeners
	 */
	private List<MainWindowListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param hosterManager Hoster Manager
	 * @param templateManager Template Manager
	 * @param titleFilenameParserManager Title Filename Parser Manager
	 * @param settingsManager Settings Manager
	 * @param proxyManager Proxy Manager
	 * @param queueManager Queue Manager
	 * @param uploadQueueManager Upload Queue Manager
	 * @param systemTray True if system tray is used, false otherwise
	 */
	public MainWindow(HosterManager hosterManager, TemplateManager templateManager, TitleFilenameParserManager titleFilenameParserManager, SettingsManager settingsManager, ProxyManager proxyManager,
			QueueManager queueManager, UploadQueueManager uploadQueueManager, boolean systemTray) {
		super("BilderUploader");
		this.mainMenuBar = new MainMenuBar(this, settingsManager, proxyManager, hosterManager, listeners);

		setIconImage(Icons.getImage("/ch/supertomcat/bilderuploader/gui/icons/BilderUploader-16x16.png"));

		if (systemTray) {
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		setLayout(new BorderLayout());

		setJMenuBar(mainMenuBar.getJMenuBar());

		QueuePanel queue = new QueuePanel(this, settingsManager, hosterManager, queueManager, uploadQueueManager, templateManager, titleFilenameParserManager);
		HosterPanel hosterPanel = new HosterPanel(hosterManager, settingsManager);
		TemplatesPanel templatesPanel = new TemplatesPanel(templateManager, settingsManager);

		tabs.setFocusable(false);
		tabs.addTab(Localization.getString("Queue"), Icons.getTangoIcon("actions/go-up.png", 22), queue);
		tabs.addTab(Localization.getString("Hoster"), Icons.getTangoIcon("places/network-server.png", 22), hosterPanel);
		tabs.addTab(Localization.getString("Templates"), Icons.getTangoIcon("mimetypes/x-office-document-template.png", 22), templatesPanel);

		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				String tabTitle = tabs.getTitleAt(tabs.getSelectedIndex());
				setTitle(windowTitlePrefix + tabTitle + windowTitleSuffix);
			}
		});

		setTitle(windowTitlePrefix + Localization.getString("Queue") + windowTitleSuffix);

		add(tabs, BorderLayout.CENTER);

		lblProgress.setIcon(Icons.getApplIcon("dummy.png", 16));

		pnlMessage.setLayout(new BorderLayout());
		pnlMessage.add(lblMessage, BorderLayout.WEST);
		pnlMessage.add(lblProgress, BorderLayout.EAST);
		lblProgress.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				if (e.getSource() instanceof JComponent) {
					JComponent comp = (JComponent)e.getSource();
					Dimension d = mainProgressPopup.getPreferredSize();
					int w = d.width;
					int h = d.height;
					if (w < 300) {
						w = 300;
					}
					int x = getX() + getInsets().left + pnlMessage.getX() + comp.getX() + comp.getWidth() - w;
					int y = getY() + getInsets().top + mainMenuBar.getJMenuBar().getHeight() + pnlMessage.getY() + comp.getY() - h;
					mainProgressPopup.setBounds(x, y, w, h);
					mainProgressPopup.setVisible(true);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mainProgressPopup.setVisible(false);
			}
		});

		new DropTarget(tabs, new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				checkDragAccepted(dtde);
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				checkDragAccepted(dtde);
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				checkDragAccepted(dtde);
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				// Nothing to do
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				if (!dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.rejectDrop();
					return;
				}

				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

				try {
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					Hoster selectedHoster = hosterManager.getSelectedHoster();
					if (selectedHoster == null) {
						logger.error("Could not drag and drop files, because selected hoster is null");
						dtde.dropComplete(false);
						return;
					}
					Pattern fileNamePattern = null;
					Predicate<Path> fileNameFilter = null;
					if (selectedHoster.getFileNameFilter() != null) {
						fileNamePattern = Pattern.compile(selectedHoster.getFileNameFilter());
						final Pattern fileNamePredicatePattern = fileNamePattern;
						fileNameFilter = x -> fileNamePredicatePattern.matcher(x.getFileName().toString()).find();
					}
					Predicate<Path> fileSizeFilter = null;
					long maxFileSize = 0;
					if (selectedHoster.getMaxFileSize() != null && selectedHoster.getMaxFileSize() > 0) {
						maxFileSize = selectedHoster.getMaxFileSize();
						final long maxFileSizePredicate = maxFileSize;
						fileSizeFilter = x -> {
							try {
								return Files.size(x) <= maxFileSizePredicate;
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						};
					}

					// TODO Get from Settings
					boolean recursive = true;
					for (File file : files) {
						if (file.isFile()) {
							if ((fileNamePattern == null || fileNamePattern.matcher(file.getName()).find()) && (maxFileSize <= 0 || file.length() <= maxFileSize)) {
								UploadFile uploadFile = new UploadFile(file.getAbsoluteFile(), selectedHoster);
								queueManager.addFile(uploadFile);
							}
						} else if (file.isDirectory()) {
							try (@SuppressWarnings("resource")
							Stream<Path> stream = recursive ? Files.walk(file.toPath()) : Files.list(file.toPath())) {
								Stream<Path> filterStream = stream.filter(Files::isRegularFile);
								if (fileNameFilter != null) {
									filterStream = filterStream.filter(fileNameFilter);
								}
								if (fileSizeFilter != null) {
									filterStream = filterStream.filter(fileSizeFilter);
								}
								List<UploadFile> uploadFiles = filterStream.map(x -> new UploadFile(x.toAbsolutePath().toFile(), selectedHoster)).collect(Collectors.toList());
								queueManager.addFiles(uploadFiles);
							}
						}
					}
					dtde.dropComplete(true);
				} catch (UnsupportedFlavorException | IOException e) {
					logger.error("Could not drop files", e);
					dtde.dropComplete(false);
				}
			}

			private void checkDragAccepted(DropTargetDragEvent dtde) {
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
				} else {
					dtde.rejectDrag();
				}
			}
		});

		add(tabs, BorderLayout.CENTER);
		add(pnlMessage, BorderLayout.SOUTH);

		WindowSettings mainWindowSettings = settingsManager.getGUISettings().getMainWindow();
		if (mainWindowSettings.isSave()) {
			this.setSize(mainWindowSettings.getWidth(), mainWindowSettings.getHeight());
			this.setLocation(mainWindowSettings.getX(), mainWindowSettings.getY());
			this.setExtendedState(mainWindowSettings.getState());
		} else {
			pack();
			setLocationRelativeTo(null);
		}

		settingsManager.addSettingsListener(new BUSettingsListener() {

			@Override
			public void settingsChanged() {
				// Nothing to do
			}

			@Override
			public void lookAndFeelChanged() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						SwingUtilities.updateComponentTreeUI(MainWindow.this);
					}
				});
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (systemTray) {
					setVisible(false);
				} else {
					if (JOptionPane.showConfirmDialog(MainWindow.this, Localization.getString("ReallyExit"), Localization.getString("Exit"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
						return;
					}
					dispose();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				for (MainWindowListener listener : listeners) {
					listener.exitApplication();
				}
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				WindowSettings mainWindowSettings = settingsManager.getGUISettings().getMainWindow();
				mainWindowSettings.setWidth(MainWindow.this.getWidth());
				mainWindowSettings.setHeight(MainWindow.this.getHeight());
				mainWindowSettings.setState(MainWindow.this.getExtendedState());
				settingsManager.writeSettings(true);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				WindowSettings mainWindowSettings = settingsManager.getGUISettings().getMainWindow();
				mainWindowSettings.setX(MainWindow.this.getX());
				mainWindowSettings.setY(MainWindow.this.getY());
				settingsManager.writeSettings(true);
			}
		});
	}

	/**
	 * Add Listener
	 * 
	 * @param listener Listener
	 */
	public void addListener(MainWindowListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove Listener
	 * 
	 * @param listener Listener
	 */
	public void removeListener(MainWindowListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @param progress Progress
	 */
	public synchronized void addProgressObserver(ProgressObserver progress) {
		mainProgressPopup.addProgressObserver(progress);
		lblProgress.setIcon(Icons.getApplIcon("animations/process-working.gif", 16));
	}

	/**
	 * @param progress Progress
	 */
	public synchronized void removeProgressObserver(ProgressObserver progress) {
		mainProgressPopup.removeProgressObserver(progress);
		if (mainProgressPopup.getProgressObserverCount() == 0) {
			lblProgress.setIcon(Icons.getApplIcon("dummy.png", 16));
		}
	}

	/**
	 * Set-Method
	 * 
	 * @param message Message
	 */
	public void setMessage(String message) {
		this.lblMessage.setText(message);
	}
}
