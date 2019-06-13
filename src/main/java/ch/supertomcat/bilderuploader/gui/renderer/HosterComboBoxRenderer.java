package ch.supertomcat.bilderuploader.gui.renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import ch.supertomcat.bilderuploader.gui.Favicons;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;

/**
 * Renderer for Hoster ComboBox
 */
public class HosterComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof Hoster) {
			Hoster hoster = (Hoster)value;
			setText(hoster.getDisplayName());

			String displayIcon = hoster.getDisplayIcon();
			if (displayIcon != null && !displayIcon.isEmpty()) {
				setIcon(Favicons.getFaviconIcon(new File(ApplicationProperties.getProperty("ApplicationPath"), "hostIcons/" + displayIcon), 16));
			} else {
				setIcon(null);
			}
		}
		return comp;
	}
}
