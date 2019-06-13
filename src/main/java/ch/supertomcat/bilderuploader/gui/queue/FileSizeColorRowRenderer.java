package ch.supertomcat.bilderuploader.gui.queue;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;

/**
 * File Size Color Row Renderer
 */
public class FileSizeColorRowRenderer extends QueueColorRowRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Settings Manager
	 */
	private SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager
	 */
	public FileSizeColorRowRenderer(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	@Override
	public void prepareValueText(JLabel label, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Long) {
			super.prepareValueText(label, table, UnitFormatUtil.getSizeString((Long)value, settingsManager.getGUISettings().getSizeDisplayMode().ordinal()), isSelected, hasFocus, row, column);
		} else {
			super.prepareValueText(label, table, value, isSelected, hasFocus, row, column);
		}
	}
}
