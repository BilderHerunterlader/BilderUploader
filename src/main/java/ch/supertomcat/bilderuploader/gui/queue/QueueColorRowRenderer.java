package ch.supertomcat.bilderuploader.gui.queue;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ch.supertomcat.bilderuploader.gui.Favicons;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultStringColorRowRenderer;

/**
 * QueueColorRowRenderer
 */
public class QueueColorRowRenderer extends DefaultStringColorRowRenderer implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	private static final Pattern PATTERN_IMAGE = Pattern.compile("(?i)^.+?\\.(?:bmp|gif|jpe|jpg|jpeg|png|tif|tiff)$");

	private static final Pattern PATTERN_VIDEO = Pattern
			.compile("(?i)^.+?\\.(?:3g2|3gp|3gp2|3gpp|amr|asf|divx|evo|flv|hdmov|m2t|m2ts|m2v|m4v|mkv|m1v|mov|mp2v|mp4|mpe|mpeg|mpg|mts|ogm|ogv|pva|pss|qt|rm|ram|rpm|rmm|ts|tp|tpr|vob|wmv|wmp)$");

	private static final Pattern PATTERN_AUDIO = Pattern.compile("(?i)^.+?\\.(?:aac|ac3|au|dts|flac|m1a|m2a|m4a|m4b|mid|midi|mka|mp2|mp3|mpa|oga|ogg|ra|rmi|snd|wav|wma)$");

	private static final Pattern PATTERN_ARCHIVE = Pattern
			.compile("(?i)^.+?\\.(?:7z|arj|bz2|bzip2|cab|cpio|deb|dmg|gz|gzip|hfs|iso|lha|lzh|lzma|rar|rpm|split|swm|tar|taz|tbz|tbz2|tgz|tpz|wim|xar|z|zip)$");

	private final Map<String, Icon> fileTypeIcons = new HashMap<>();

	/**
	 * Constructor
	 */
	public QueueColorRowRenderer() {
		fileTypeIcons.put("IMAGE", Icons.getTangoIcon("mimetypes/image-x-generic.png", 16));
		fileTypeIcons.put("VIDEO", Icons.getTangoIcon("mimetypes/video-x-generic.png", 16));
		fileTypeIcons.put("AUDIO", Icons.getTangoIcon("mimetypes/audio-x-generic.png", 16));
		fileTypeIcons.put("ARCHIVE", Icons.getTangoIcon("mimetypes/package-x-generic.png", 16));
		fileTypeIcons.put("TEXT", Icons.getTangoIcon("mimetypes/text-x-generic.png", 16));
	}

	@Override
	public void prepareForegroundColor(Component comp, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.prepareForegroundColor(comp, table, value, isSelected, hasFocus, row, column);
		} else {
			Object progressValue = table.getModel().getValueAt(table.convertRowIndexToModel(row), table.getColumn("Progress").getModelIndex());
			if (progressValue instanceof UploadFile && ((UploadFile)progressValue).isDeactivated()) {
				comp.setForeground(Color.RED);
			} else {
				super.prepareForegroundColor(comp, table, value, isSelected, hasFocus, row, column);
			}
		}
	}

	@Override
	public void prepareValueText(JLabel label, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof Hoster) {
			Hoster hoster = (Hoster)value;
			String hosterName = hoster.getDisplayName();
			label.setText(hosterName);
			label.setToolTipText(hosterName);

			String displayIcon = hoster.getDisplayIcon();
			if (displayIcon != null && !displayIcon.isEmpty()) {
				label.setIcon(Favicons.getFaviconIcon(new File(ApplicationProperties.getProperty("ApplicationPath"), "hostIcons/" + displayIcon), 16));
			} else {
				label.setIcon(null);
			}
		} else if (value instanceof File) {
			super.prepareValueText(label, table, value, isSelected, hasFocus, row, column);

			String mimeType = ((UploadFile)table.getValueAt(row, table.getColumn("Progress").getModelIndex())).getMimeType();
			if (mimeType.startsWith("image/")) {
				setIcon(fileTypeIcons.get("IMAGE"));
			} else if (mimeType.startsWith("video/")) {
				setIcon(fileTypeIcons.get("VIDEO"));
			} else if (mimeType.startsWith("audio/")) {
				setIcon(fileTypeIcons.get("AUDIO"));
			} else if (mimeType.startsWith("archive/")) {
				setIcon(fileTypeIcons.get("ARCHIVE"));
			} else if (mimeType.startsWith("text/")) {
				setIcon(fileTypeIcons.get("TEXT"));
			} else if (PATTERN_IMAGE.matcher(((File)value).getName()).matches()) {
				setIcon(fileTypeIcons.get("IMAGE"));
			} else if (PATTERN_VIDEO.matcher(((File)value).getName()).matches()) {
				setIcon(fileTypeIcons.get("VIDEO"));
			} else if (PATTERN_AUDIO.matcher(((File)value).getName()).matches()) {
				setIcon(fileTypeIcons.get("AUDIO"));
			} else if (PATTERN_ARCHIVE.matcher(((File)value).getName()).matches()) {
				setIcon(fileTypeIcons.get("ARCHIVE"));
			}
		} else {
			super.prepareValueText(label, table, value, isSelected, hasFocus, row, column);
		}
	}
}
