package ch.supertomcat.bilderuploader.test;

import ch.supertomcat.bilderuploader.settings.SettingsManager;
import jakarta.xml.bind.JAXBException;

/**
 * Class which handels the settings
 */
public class DummySettingsManager extends SettingsManager {
	/**
	 * Constructor
	 * 
	 * @throws JAXBException
	 */
	public DummySettingsManager() throws JAXBException {
		readSettings();
	}

	@Override
	public synchronized boolean readSettings() {
		logger.info("Loading Default Settings File");
		try {
			this.settings = loadDefaultSettingsFile();
			settingsChanged();
			return true;
		} catch (Exception e) {
			logger.error("Could not read default settings file", e);
			return false;
		}
	}

	@Override
	public synchronized boolean writeSettings(boolean noShutdown) {
		// Nothing to do
		return true;
	}
}
