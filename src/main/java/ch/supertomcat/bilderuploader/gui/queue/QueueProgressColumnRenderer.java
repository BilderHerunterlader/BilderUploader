package ch.supertomcat.bilderuploader.gui.queue;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.upload.UploadFileProgress;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;

/**
 * QueueProgressColumnRenderer
 */
public class QueueProgressColumnRenderer extends QueueColorRowRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Progress String Format
	 */
	private static final String PROGRESS_PERCENT_STRING_FORMAT = "%.2f%% %s";

	/**
	 * Progress String Format
	 */
	private static final String PROGRESS_SIZE_STRING_FORMAT = "%s";

	/**
	 * Complete Color
	 */
	private static final Color COMPLETE_COLOR = Color.decode("#81ff68");

	/**
	 * Waiting Color
	 */
	private static final Color WAITING_COLOR = Color.decode("#30ffff");

	/**
	 * Failed Color
	 */
	private static final Color FAILED_COLOR = Color.decode("#ff9e9e");

	/**
	 * Progress Bar
	 */
	private JProgressBar progressBar = new JProgressBar();

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 */
	public QueueProgressColumnRenderer(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof UploadFile) {
			UploadFile file = (UploadFile)value;

			Component comp;
			if (file.getStatus() == UploadFileState.UPLOADING && file.getProgress() != null) {
				UploadFileProgress progress = file.getProgress();
				progressBar.setValue(progress.getPercentInt());

				String progressString;
				switch (settingsManager.getGUISettings().getProgressDisplayMode()) {
					case PROGRESSBAR_SIZE:
						String sizeString = UnitFormatUtil.getSizeString(progress.getBytesUploaded(), settingsManager.getGUISettings().getSizeDisplayMode().ordinal());
						progressString = String.format(PROGRESS_SIZE_STRING_FORMAT, sizeString);
						break;
					case PROGRESSBAR_PERCENT:
					default:
						if (progress.getBytesTotal() > 0) {
							progressString = String.format(PROGRESS_PERCENT_STRING_FORMAT, progress.getPercent(), UnitFormatUtil.getBitrateString(progress.getRate()));
						} else {
							String sizeStringAlt = UnitFormatUtil.getSizeString(progress.getBytesUploaded(), settingsManager.getGUISettings().getSizeDisplayMode().ordinal());
							progressString = String.format(PROGRESS_SIZE_STRING_FORMAT, sizeStringAlt);
						}
						break;
				}

				progressBar.setString(progressString);
				progressBar.setToolTipText(progressString);

				comp = progressBar;
			} else {
				setText(file.getStatusText());
				setOpaque(true);

				String errMsg = file.getErrMsg();
				if (errMsg != null && !errMsg.isEmpty()) {
					setToolTipText(errMsg);
				} else {
					setToolTipText(file.getStatusText());
				}

				prepareForegroundColor(this, table, value, isSelected, hasFocus, row, column);

				comp = this;
			}

			if (isSelected) {
				prepareBackgroundColor(comp, table, value, isSelected, hasFocus, row, column);
			} else {
				switch (file.getStatus()) {
					case COMPLETE:
						comp.setBackground(COMPLETE_COLOR);
						break;
					case FAILED:
						comp.setBackground(FAILED_COLOR);
						break;
					case WAITING:
						comp.setBackground(WAITING_COLOR);
						break;
					case ABORTING:
					case SLEEPING:
					case UPLOADING:
					default:
						prepareBackgroundColor(comp, table, value, isSelected, hasFocus, row, column);
						break;
				}
			}

			return comp;
		} else {
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
