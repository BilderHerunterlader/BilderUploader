package ch.supertomcat.bilderuploader.gui.queue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.ApplGUIConstants;
import ch.supertomcat.bilderuploader.gui.MainWindow;
import ch.supertomcat.bilderuploader.gui.templates.OutputGeneratorDialog;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.queue.QueueManager;
import ch.supertomcat.bilderuploader.queue.QueueManagerListener;
import ch.supertomcat.bilderuploader.queue.UploadQueueManager;
import ch.supertomcat.bilderuploader.queue.UploadQueueManagerListener;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.templates.TemplateManager;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParserManager;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.supertomcatutils.clipboard.ClipboardUtil;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;
import ch.supertomcat.supertomcatutils.gui.progress.ProgressObserver;
import ch.supertomcat.supertomcatutils.gui.table.TableUtil;

/**
 * QueuePanel-Panel
 */
public class QueuePanel extends JPanel implements QueueManagerListener, UploadQueueManagerListener, MouseListener, TableColumnModelListener {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 5907100131845566233L;

	/**
	 * Logger for this class
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Synchronization Object for changes of the table. This object is used instead of
	 * the QueuePanel (this) itself to prevent deadlocks, when EventQueue.invokeAndWait is used.
	 * 
	 * A deadlock happened when SwingUtilities.updateComponentTreeUI was called, while another Thread called picProgressBarUpdated.
	 * picProgressBarUpdated locked the QueuePanel(this) and SwingUtilities.updateComponentTreeUI did too. This causes the deadlock.
	 */
	private Object syncObject = new Object();

	/**
	 * TabelModel
	 */
	private QueueTableModel model = new QueueTableModel();

	/**
	 * Table
	 */
	private JTable jtQueue = new JTable(model);

	/**
	 * Renderer for ProgressBars
	 */
	private QueueProgressColumnRenderer pcr;

	/**
	 * Button
	 */
	private JButton btnStart = new JButton(Localization.getString("Start"), Icons.getTangoIcon("actions/media-playback-start.png", 16));

	/**
	 * Button
	 */
	private JButton btnStop = new JButton(Localization.getString("Stop"), Icons.getTangoIcon("actions/media-playback-stop.png", 16));

	/**
	 * Button
	 */
	private JButton btnGenerateTemplate = new JButton(Localization.getString("GenerateCode"), Icons.getTangoIcon("mimetypes/text-html.png", 16));

	/**
	 * Panel
	 */
	private JPanel pnlButtons = new JPanel();

	/**
	 * Label
	 */
	private JLabel lblStatus = new JLabel();

	/**
	 * PopupMenu
	 */
	private JPopupMenu popupMenu = new JPopupMenu();

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemReset = new JMenuItem(Localization.getString("Reset"), Icons.getTangoIcon("actions/edit-undo.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemCopy = new JMenuItem(Localization.getString("Copy"), Icons.getTangoIcon("actions/edit-copy.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemChangeHoster = new JMenuItem(Localization.getString("ChangeHoster"), Icons.getTangoIcon("places/network-server.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemDelete = new JMenuItem(Localization.getString("Delete"), Icons.getTangoIcon("actions/edit-delete.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemActivate = new JMenuItem(Localization.getString("Activate"), Icons.getTangoIcon("actions/media-record.png", 16));

	/**
	 * MenuItem
	 */
	private JMenuItem menuItemDeactivate = new JMenuItem(Localization.getString("Deactivate"), Icons.getTangoIcon("emblems/emblem-readonly.png", 16));

	/**
	 * Scrollpane
	 */
	private JScrollPane jsp = new JScrollPane(jtQueue);

	/**
	 * QueueColorRowRenderer
	 */
	private QueueColorRowRenderer crr = new QueueColorRowRenderer();

	/**
	 * FileSizeColorRowRenderer
	 */
	private FileSizeColorRowRenderer fscrr;

	/**
	 * DateTimeColorRowRenderer
	 */
	private DateTimeColorRowRenderer dtcrr = new DateTimeColorRowRenderer();

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Hoster Manager
	 */
	private final HosterManager hosterManager;

	/**
	 * Queue Manager
	 */
	private final QueueManager queueManager;

	/**
	 * Upload Queue Manager
	 */
	private final UploadQueueManager uploadQueueManager;

	/**
	 * Template Manager
	 */
	private final TemplateManager templateManager;

	/**
	 * Title Filename Parser Manager
	 */
	private final TitleFilenameParserManager titleFilenameParserManager;

	/**
	 * Owner
	 */
	private final MainWindow owner;

	/**
	 * File Column Model Index
	 */
	private final int fileColumnModelIndex = jtQueue.getColumn("File").getModelIndex();

	/**
	 * Hoster Column Model Index
	 */
	private final int hosterColumnModelIndex = jtQueue.getColumn("Hoster").getModelIndex();

	/**
	 * Size Column Model Index
	 */
	private final int sizeColumnModelIndex = jtQueue.getColumn("Size").getModelIndex();

	/**
	 * Added Column Model Index
	 */
	private final int addedColumnModelIndex = jtQueue.getColumn("Added").getModelIndex();

	/**
	 * Progress Column Model Index
	 */
	private final int progressColumnModelIndex = jtQueue.getColumn("Progress").getModelIndex();

	/**
	 * Constructor
	 * 
	 * @param owner Owner
	 * @param settingsManager Settings Manager
	 * @param hosterManager Hoster Manager
	 * @param queueManager Queue Manager
	 * @param uploadQueueManager Upload Queue Manager
	 * @param templateManager Template Manager
	 * @param titleFilenameParserManager Title Filename Parser Manager
	 */
	public QueuePanel(MainWindow owner, SettingsManager settingsManager, HosterManager hosterManager, QueueManager queueManager, UploadQueueManager uploadQueueManager, TemplateManager templateManager,
			TitleFilenameParserManager titleFilenameParserManager) {
		this.owner = owner;
		this.settingsManager = settingsManager;
		this.hosterManager = hosterManager;
		this.queueManager = queueManager;
		this.queueManager.addListener(this);
		this.uploadQueueManager = uploadQueueManager;
		this.uploadQueueManager.addDownloadQueueManagerListener(this);
		this.templateManager = templateManager;
		this.titleFilenameParserManager = titleFilenameParserManager;
		fscrr = new FileSizeColorRowRenderer(this.settingsManager);
		pcr = new QueueProgressColumnRenderer(this.settingsManager);

		setLayout(new BorderLayout());

		TableUtil.internationalizeColumns(jtQueue);

		jtQueue.getColumn("Progress").setCellRenderer(pcr);
		jtQueue.getColumn("Size").setCellRenderer(fscrr);
		jtQueue.getColumn("Added").setCellRenderer(dtcrr);

		int fileOrHosterTableHeaderWidth = TableUtil.calculateColumnHeaderWidth(jtQueue, jtQueue.getColumn("File"), 47);

		jtQueue.getColumn("File").setPreferredWidth(fileOrHosterTableHeaderWidth);
		jtQueue.getColumn("Hoster").setPreferredWidth(fileOrHosterTableHeaderWidth);
		updateColWidthsFromSettingsManager();
		jtQueue.getColumnModel().addColumnModelListener(this);
		jtQueue.addMouseListener(this);
		jtQueue.getTableHeader().setReorderingAllowed(false);

		jtQueue.setGridColor(ApplGUIConstants.TABLE_GRID_COLOR);
		jtQueue.setRowHeight(TableUtil.calculateRowHeight(jtQueue, false, true));

		jtQueue.setPreferredScrollableViewportSize(new Dimension(fileOrHosterTableHeaderWidth * 4, jtQueue.getPreferredScrollableViewportSize().height));

		add(jsp, BorderLayout.CENTER);

		btnStart.setMnemonic(KeyEvent.VK_S);
		btnStop.setMnemonic(KeyEvent.VK_T);

		btnStart.addActionListener(e -> actionStart());
		btnStop.addActionListener(e -> actionStop());
		btnStop.setToolTipText(Localization.getString("StopTooltip"));
		btnGenerateTemplate.addActionListener(e -> actionGenerate(false));
		pnlButtons.add(btnStart);
		pnlButtons.add(btnStop);
		pnlButtons.add(btnGenerateTemplate);
		add(pnlButtons, BorderLayout.SOUTH);
		add(lblStatus, BorderLayout.NORTH);
		uploadQueueManager.addDownloadQueueManagerListener(this);

		popupMenu.add(menuItemActivate);
		popupMenu.add(menuItemDeactivate);
		popupMenu.add(menuItemReset);
		popupMenu.add(menuItemChangeHoster);
		popupMenu.add(menuItemCopy);
		popupMenu.add(menuItemDelete);

		menuItemActivate.addActionListener(e -> actionActivate());
		menuItemDeactivate.addActionListener(e -> actionDeactivate());
		menuItemDelete.addActionListener(e -> actionDelete());
		menuItemReset.addActionListener(e -> actionReset());
		menuItemChangeHoster.addActionListener(e -> actionChangeHoster());
		menuItemCopy.addActionListener(e -> actionCopy());

		jtQueue.setDefaultRenderer(Object.class, crr);

		// Load downloadqueue
		List<UploadFile> files = queueManager.getQueue();
		for (UploadFile file : files) {
			model.addRow(file);
		}
		updateStatus();

		queueManager.addListener(this);

		// Register Key
		ActionMap am = getActionMap();
		InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		Object deleteKey = new Object();

		KeyStroke deleteStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		Action deleteAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (uploadQueueManager.isExecutingTasks()) {
					return;
				}
				actionDelete();
			}
		};
		im.put(deleteStroke, deleteKey);
		am.put(deleteKey, deleteAction);

		updateStatus();
	}

	/**
	 * Start
	 */
	private void actionStart() {
		Thread t = new Thread(() -> queueManager.startUpload());
		t.start();
	}

	/**
	 * Stop
	 */
	private void actionStop() {
		Thread t = new Thread(() -> queueManager.stopUpload());
		t.start();
	}

	private void actionGenerate(boolean onlySelected) {
		List<UploadFile> files = new ArrayList<>();
		synchronized (syncObject) {
			if (onlySelected) {
				int[] selectedRows = jtQueue.getSelectedRows();
				int[] selectedModelRows = TableUtil.convertRowIndexToModel(jtQueue, selectedRows, true);
				for (int selectedModelRow : selectedModelRows) {
					UploadFile file = (UploadFile)model.getValueAt(selectedModelRow, progressColumnModelIndex);
					if (file.getStatus() == UploadFileState.COMPLETE) {
						files.add(file);
					}
				}
			} else {
				for (int i = 0; i < model.getRowCount(); i++) {
					UploadFile file = (UploadFile)model.getValueAt(i, progressColumnModelIndex);
					if (file.getStatus() == UploadFileState.COMPLETE) {
						files.add(file);
					}
				}
			}
		}
		new OutputGeneratorDialog(owner, templateManager, titleFilenameParserManager, settingsManager, files);
	}

	/**
	 * Delete
	 */
	private void actionDelete() {
		int retval = JOptionPane.showConfirmDialog(owner, Localization.getString("QueueReallyDelete"), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, Icons
				.getTangoIcon("status/dialog-warning.png", 32));
		if (retval == JOptionPane.NO_OPTION) {
			return;
		}

		disableComponents();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ProgressObserver pg = new ProgressObserver();
				try {
					owner.addProgressObserver(pg);
					pg.progressModeChanged(true);
					pg.progressChanged(Localization.getString("DeleteEntries"));
					synchronized (queueManager.getSyncObject()) {
						synchronized (syncObject) {
							int[] selectedRows = jtQueue.getSelectedRows();
							int[] selectedModelRows = TableUtil.convertRowIndexToModel(jtQueue, selectedRows, true);
							queueManager.removeFiles(selectedModelRows);
						}
					}
					owner.setMessage(Localization.getString("EntriesDeleted"));
				} finally {
					owner.removeProgressObserver(pg);
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							enableComponents();
						}
					});
				}
			}
		});
		t.setName("QueueDeleteThread-" + t.getId());
		t.start();
	}

	/**
	 * Reset
	 */
	private void actionReset() {
		disableComponents();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ProgressObserver pg = new ProgressObserver();
				try {
					owner.addProgressObserver(pg);
					pg.progressModeChanged(true);
					pg.progressChanged(Localization.getString("ResettingEntries"));
					synchronized (queueManager.getSyncObject()) {
						synchronized (syncObject) {
							int[] selectedRows = jtQueue.getSelectedRows();
							int[] selectedModelRows = TableUtil.convertRowIndexToModel(jtQueue, selectedRows, true);
							for (int selectedRow : selectedModelRows) {
								UploadFile file = (UploadFile)model.getValueAt(selectedRow, progressColumnModelIndex);
								file.setFileUploadResult(null);
								file.setStatus(UploadFileState.SLEEPING);
								queueManager.updateFile(file);
							}
						}
					}
					owner.setMessage(Localization.getString("EntriesResetted"));
				} finally {
					owner.removeProgressObserver(pg);
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							enableComponents();
						}
					});
				}
			}
		});
		t.setName("QueueResetThread-" + t.getId());
		t.start();
	}

	/**
	 * Change Hoster
	 */
	private void actionChangeHoster() {
		Hoster defaultHoster = hosterManager.getSelectedHoster();
		HosterSelectionDialog hosterSelectionDialog = new HosterSelectionDialog(owner, hosterManager.getHosts(), defaultHoster);
		Hoster hoster = hosterSelectionDialog.getChosenHoster();
		if (hoster == null) {
			return;
		}

		disableComponents();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ProgressObserver pg = new ProgressObserver();
				try {
					owner.addProgressObserver(pg);
					pg.progressModeChanged(true);
					pg.progressChanged(Localization.getString("ChangingHosterEntries"));
					synchronized (queueManager.getSyncObject()) {
						synchronized (syncObject) {
							int[] selectedRows = jtQueue.getSelectedRows();
							int[] selectedModelRows = TableUtil.convertRowIndexToModel(jtQueue, selectedRows, true);
							for (int selectedRow : selectedModelRows) {
								UploadFile file = (UploadFile)model.getValueAt(selectedRow, progressColumnModelIndex);
								file.setHoster(hoster);
								queueManager.updateFile(file);
							}
						}
					}
					owner.setMessage(Localization.getString("EntriesHosterChanged"));
				} finally {
					owner.removeProgressObserver(pg);
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							enableComponents();
						}
					});
				}
			}
		});
		t.setName("QueueResetThread-" + t.getId());
		t.start();
	}

	/**
	 * Activate
	 */
	private void actionActivate() {
		synchronized (queueManager.getSyncObject()) {
			synchronized (syncObject) {
				int s[] = jtQueue.getSelectedRows();
				for (int i = 0; i < s.length; i++) {
					UploadFile file = queueManager.getFileByIndex(jtQueue.convertRowIndexToModel(s[i]));
					file.setDeactivated(false);
					queueManager.updateFile(file);
				}
				model.fireTableDataChanged();
			}
		}
	}

	/**
	 * Deactivate
	 */
	private void actionDeactivate() {
		synchronized (queueManager.getSyncObject()) {
			synchronized (syncObject) {
				int s[] = jtQueue.getSelectedRows();
				for (int i = 0; i < s.length; i++) {
					UploadFile file = queueManager.getFileByIndex(jtQueue.convertRowIndexToModel(s[i]));
					file.setDeactivated(true);
					queueManager.updateFile(file);
				}
				model.fireTableDataChanged();
			}
		}
	}

	/**
	 * Copy
	 */
	private void actionCopy() {
		disableComponents();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ProgressObserver pg = new ProgressObserver();
				try {
					owner.addProgressObserver(pg);
					pg.progressModeChanged(true);
					pg.progressChanged(Localization.getString("CopyEntries"));
					StringJoiner sj = new StringJoiner("\n");
					synchronized (queueManager.getSyncObject()) {
						synchronized (syncObject) {
							int[] selectedRows = jtQueue.getSelectedRows();
							int[] selectedModelRows = TableUtil.convertRowIndexToModel(jtQueue, selectedRows, true);
							for (int selectedRow : selectedModelRows) {
								UploadFile file = (UploadFile)model.getValueAt(selectedRow, progressColumnModelIndex);
								for (String uploadResultText : file.getFileUploadResult().getUploadResultTexts()) {
									// TODO filter out only link, defined by regex in settings
									sj.add(uploadResultText);
								}
							}
						}
					}
					ClipboardUtil.setClipboardContent(sj.toString());
					owner.setMessage(Localization.getString("EntriesCopied"));
				} finally {
					owner.removeProgressObserver(pg);
					EventQueue.invokeLater(new Runnable() {
						@Override
						public void run() {
							enableComponents();
						}
					});
				}
			}
		});
		t.setName("QueueCopyThread-" + t.getId());
		t.start();
	}

	private void disableComponents() {
		jtQueue.setEnabled(false);
		btnStart.setEnabled(false);
		btnStop.setEnabled(false);
	}

	private void enableComponents() {
		jtQueue.setEnabled(true);
		btnStart.setEnabled(true);
		btnStop.setEnabled(true);
	}

	@Override
	public void queueChanged(final int queue, final int openSlots, final int maxSlots) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateStatus();
			}
		});
	}

	private void showTablePopupMenu(MouseEvent e) {
		boolean bIsDownloading = uploadQueueManager.isExecutingTasks();
		boolean bTableEnabled = jtQueue.isEnabled();
		boolean enableMenuItems = !bIsDownloading && bTableEnabled;
		menuItemActivate.setEnabled(enableMenuItems);
		menuItemDeactivate.setEnabled(enableMenuItems);
		menuItemDelete.setEnabled(enableMenuItems);
		SwingUtilities.updateComponentTreeUI(popupMenu);
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void uploadsComplete(int queue, int openSlots, int maxSlots) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == jtQueue && e.isPopupTrigger() && jtQueue.getSelectedRowCount() > 0) {
			showTablePopupMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == jtQueue && e.isPopupTrigger() && jtQueue.getSelectedRowCount() > 0) {
			showTablePopupMenu(e);
		}
	}

	/**
	 * Updates the status-display
	 */
	private void updateStatus() {
		int queueCount = jtQueue.getRowCount();
		int openDownloadSlots = uploadQueueManager.getOpenSlots();
		int connectionCount = uploadQueueManager.getMaxConnectionCount();
		long overallDownloadedFiles = settingsManager.getUploadSettings().getUploadedFiles();
		String overallDownloadedBytes = UnitFormatUtil.getSizeString(settingsManager.getUploadSettings().getUploadedBytes(), settingsManager.getGUISettings().getSizeDisplayMode().ordinal());
		int sessionDownloadedFiles = uploadQueueManager.getSessionFiles();
		String sessionDownloadedBytes = UnitFormatUtil.getSizeString(uploadQueueManager.getSessionBytes(), settingsManager.getGUISettings().getSizeDisplayMode().ordinal());
		// TODO Update Rate
		// String downloadRate = UnitFormatUtil.getBitrateString(uploadQueueManager.get.getDownloadBitrate());
		String rate = "";
		if (rate.isEmpty()) {
			rate = Localization.getString("NotAvailable");
		}
		lblStatus.setText(Localization.getString("Queue") + ": " + queueCount + " | " + Localization.getString("FreeSlots") + ": " + openDownloadSlots + "/" + connectionCount + " | "
				+ Localization.getString("DownloadedFiles") + ": " + overallDownloadedFiles + " (" + sessionDownloadedFiles + ") | " + Localization.getString("DownloadedBytes") + ": "
				+ overallDownloadedBytes + " (" + sessionDownloadedBytes + ") | " + Localization.getString("DownloadBitrate") + ": " + rate);
	}

	@Override
	public void sessionUploadedBytesChanged(long count) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateStatus();
			}
		});
	}

	@Override
	public void sessionUploadedFilesChanged(int count) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateStatus();
			}
		});
	}

	@Override
	public void totalUploadRateCalculated(double rate) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateStatus();
			}
		});
	}

	/**
	 * updateColWidthsToSettingsManager
	 */
	private void updateColWidthsToSettingsManager() {
		if (settingsManager.getGUISettings().isSaveTableColumnSizes() == false) {
			return;
		}
		settingsManager.getGUISettings().setColWidthsQueue(TableUtil.serializeColWidthSetting(jtQueue));
		settingsManager.writeSettings(true);
	}

	/**
	 * updateColWidthsFromSettingsManager
	 */
	private void updateColWidthsFromSettingsManager() {
		if (settingsManager.getGUISettings().isSaveTableColumnSizes() == false) {
			return;
		}
		TableUtil.applyColWidths(jtQueue, settingsManager.getGUISettings().getColWidthsQueue());
	}

	@Override
	public void columnAdded(TableColumnModelEvent e) {
	}

	@Override
	public void columnMarginChanged(ChangeEvent e) {
		updateColWidthsToSettingsManager();
	}

	@Override
	public void columnMoved(TableColumnModelEvent e) {
	}

	@Override
	public void columnRemoved(TableColumnModelEvent e) {
	}

	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
	}

	@Override
	public void fileAdded(UploadFile file) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					model.addRow(file);
					updateStatus();
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void filesAdded(List<UploadFile> files) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					for (UploadFile file : files) {
						model.addRow(file);
					}
					updateStatus();
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void fileRemoved(UploadFile file, int index) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					model.removeRow(index);
					model.fireTableDataChanged();
					updateStatus();
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void filesRemoved(int[] removedIndeces) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					// Convert first removed row index, before removing any rows
					int firstRemovedRowViewIndex = jtQueue.convertRowIndexToView(removedIndeces[0]);

					for (int i = removedIndeces.length - 1; i > -1; i--) {
						model.removeRow(removedIndeces[i]);
					}
					model.fireTableDataChanged();

					int rowCount = jtQueue.getRowCount();
					int aboveFirstRemovedRowViewIndex = firstRemovedRowViewIndex - 1;
					if (firstRemovedRowViewIndex < rowCount) {
						jtQueue.setRowSelectionInterval(firstRemovedRowViewIndex, firstRemovedRowViewIndex);
					} else if (aboveFirstRemovedRowViewIndex >= 0 && aboveFirstRemovedRowViewIndex < rowCount) {
						jtQueue.setRowSelectionInterval(aboveFirstRemovedRowViewIndex, aboveFirstRemovedRowViewIndex);
					}
					updateStatus();
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void fileProgressChanged(UploadFile file, int index) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					int row = index;
					if ((row > -1) && (model.getRowCount() > row)) {
						model.fireTableCellUpdated(row, progressColumnModelIndex);
					}
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void fileHosterChanged(UploadFile file, int index) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					int row = index;
					if ((row > -1) && (model.getRowCount() > row)) {
						// Change Cell
						Hoster hoster = file.getHoster();
						model.setValueAt(hoster, row, hosterColumnModelIndex);
						model.fireTableCellUpdated(row, hosterColumnModelIndex);
					}
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void fileStatusChanged(UploadFile file, int index) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					int row = index;
					if ((row > -1) && (model.getRowCount() > row)) {
						model.fireTableCellUpdated(row, progressColumnModelIndex);
						if (file.getStatus() == UploadFileState.UPLOADING) {
							// TODO Settings
							jtQueue.scrollRectToVisible(new Rectangle(jtQueue.getCellRect(jtQueue.convertRowIndexToView(row), 0, true)));
						}
					}
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void fileDeactivatedChanged(UploadFile file, int index) {
		synchronized (syncObject) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					int row = index;
					if ((row > -1) && (model.getRowCount() > row)) {
						model.fireTableRowsUpdated(row, row);
					}
				}
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				try {
					EventQueue.invokeAndWait(r);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void increaseSessionUploadedFiles() {
		// Nothing to do
	}

	@Override
	public void increaseSessionUploadedBytes(long size) {
		// Nothing to do
	}

	@Override
	public void startUpload(UploadFile file) {
		// Nothing to do
	}

	@Override
	public void startUpload(List<UploadFile> files) {
		// Nothing to do
	}

	@Override
	public void stopUpload(boolean cancelAlreadyExecutingTasks) {
		// Nothing to do
	}
}
