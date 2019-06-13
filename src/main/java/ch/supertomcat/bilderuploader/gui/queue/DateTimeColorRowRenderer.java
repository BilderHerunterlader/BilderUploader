package ch.supertomcat.bilderuploader.gui.queue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * DateTime Color Row Renderer
 */
public class DateTimeColorRowRenderer extends QueueColorRowRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * Date Format
	 */
	private final DateTimeFormatter dateFormat;

	/**
	 * Constructor
	 */
	public DateTimeColorRowRenderer() {
		this.dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
	}

	/**
	 * Constructor
	 * 
	 * @param dateFormat Date Format
	 */
	public DateTimeColorRowRenderer(DateTimeFormatter dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public void prepareValueText(JLabel label, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof TemporalAccessor) {
			String formattedDate = dateFormat.format((TemporalAccessor)value);
			super.prepareValueText(label, table, formattedDate, isSelected, hasFocus, row, column);
		} else if (value instanceof Long) {
			String formattedDate = dateFormat.format(Instant.ofEpochMilli((Long)value));
			super.prepareValueText(label, table, formattedDate, isSelected, hasFocus, row, column);
		} else {
			super.prepareValueText(label, table, value, isSelected, hasFocus, row, column);
		}
	}
}
