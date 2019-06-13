package ch.supertomcat.bilderuploader.settings;

import ch.supertomcat.supertomcatutils.settings.SettingsListener;

/**
 * Interface of DummySettingsManager
 */
public interface BUSettingsListener extends SettingsListener {
	/**
	 * Look and Feel changed
	 */
	public void lookAndFeelChanged();
}
