package ch.supertomcat.bilderuploader.settings;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

import ch.supertomcat.bilderuploader.settingsconfig.ConnectionSettings;
import ch.supertomcat.bilderuploader.settingsconfig.CustomSetting;
import ch.supertomcat.bilderuploader.settingsconfig.DirectorySettings;
import ch.supertomcat.bilderuploader.settingsconfig.GUISettings;
import ch.supertomcat.bilderuploader.settingsconfig.HosterSettings;
import ch.supertomcat.bilderuploader.settingsconfig.LogLevelSetting;
import ch.supertomcat.bilderuploader.settingsconfig.LookAndFeelSetting;
import ch.supertomcat.bilderuploader.settingsconfig.ObjectFactory;
import ch.supertomcat.bilderuploader.settingsconfig.Settings;
import ch.supertomcat.bilderuploader.settingsconfig.UploadSettings;
import ch.supertomcat.bilderuploader.util.BUUtil;
import ch.supertomcat.supertomcatutils.settings.SettingsManagerBase;
import ch.supertomcat.supertomcatutils.settings.SettingsUtil;
import jakarta.xml.bind.JAXBException;

/**
 * Class which handels the settings
 */
public class SettingsManager extends SettingsManagerBase<Settings, BUSettingsListener> {
	/**
	 * Resource Path to the default settings file
	 */
	private static final String DEFAULT_SETTINGS_FILE_RESOURCE_PATH = "/ch/supertomcat/bilderuploader/settingsconfig/default-settings.xml";

	/**
	 * Resource Path to settings schema file
	 */
	private static final String SETTINGS_SCHEMA_FILE_RESOURCE_PATH = "/ch/supertomcat/bilderuploader/settingsconfig/settings.xsd";

	/**
	 * LookAndFeel ClassNames
	 */
	protected static final Map<LookAndFeelSetting, String> LOOK_AND_FEEL_CLASS_NAMES = new LinkedHashMap<>();

	/**
	 * LookAndFeel Names
	 */
	protected static final Map<LookAndFeelSetting, String> LOOK_AND_FEEL_NAMES = new LinkedHashMap<>();

	/**
	 * Log Level Mapping
	 */
	protected static final Map<LogLevelSetting, Level> LOG_LEVEL_MAPPING = new LinkedHashMap<>();

	static {
		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_DEFAULT, UIManager.getCrossPlatformLookAndFeelClassName());
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_DEFAULT, "Default");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_OS, UIManager.getSystemLookAndFeelClassName());
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_OS, "OperatingSystem");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_METAL, "javax.swing.plaf.metal.MetalLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_METAL, "Metal");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_WINDOWS, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_WINDOWS, "Windows");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_WINDOWS_CLASSIC, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_WINDOWS_CLASSIC, "Windows Classic");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_MOTIF, "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_MOTIF, "Motif");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_GTK, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_GTK, "GTK");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_MACOS, "javax.swing.plaf.mac.MacLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_MACOS, "Mac OS");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_NIMBUS, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_NIMBUS, "Nimbus");

		LOG_LEVEL_MAPPING.put(LogLevelSetting.TRACE, Level.TRACE);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.DEBUG, Level.DEBUG);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.INFO, Level.INFO);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.WARN, Level.WARN);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.ERROR, Level.ERROR);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.FATAL, Level.FATAL);
	}

	/**
	 * Flag if the Application is started at first time
	 */
	protected boolean languageFirstRun = true;

	/**
	 * Hoster Settings Map
	 */
	protected final Map<String, HosterSettings> hosterSettingsMap = new HashMap<>();

	/**
	 * Dummy Constructor. ONLY USE FOR UNIT TESTS.
	 * 
	 * @throws JAXBException
	 */
	protected SettingsManager() throws JAXBException {
		super(ObjectFactory.class, DEFAULT_SETTINGS_FILE_RESOURCE_PATH, SETTINGS_SCHEMA_FILE_RESOURCE_PATH);
	}

	/**
	 * Constructor
	 * 
	 * @param strSettingsFolder Settings Folder
	 * @param strSettingsFilename Settings Filename
	 * @throws JAXBException
	 */
	public SettingsManager(String strSettingsFolder, final String strSettingsFilename) throws JAXBException {
		super(strSettingsFolder, strSettingsFilename, ObjectFactory.class, DEFAULT_SETTINGS_FILE_RESOURCE_PATH, SETTINGS_SCHEMA_FILE_RESOURCE_PATH);
	}

	/**
	 * Update Hoster Settings Map
	 */
	private synchronized void updateHosterSettingsMap() {
		for (HosterSettings hosterSettings : this.settings.getHosterSettings()) {
			this.hosterSettingsMap.put(hosterSettings.getName(), hosterSettings);
		}
	}

	/**
	 * Read the Settings
	 * 
	 * @return True if successful, false otherwise
	 */
	public synchronized boolean readSettings() {
		if (Files.exists(settingsFile)) {
			logger.info("Loading Settings File: {}", settingsFile);
			try {
				this.settings = loadUserSettingsFile();
				updateHosterSettingsMap();
				applyLogLevel();
				languageFirstRun = false;
				settingsChanged();
				return true;
			} catch (Exception e) {
				logger.error("Could not read settings file: {}", settingsFile, e);
				return false;
			}
		} else {
			logger.info("Loading Default Settings File");
			try {
				this.settings = loadDefaultSettingsFile();
				updateHosterSettingsMap();
				applyLogLevel();
				settingsChanged();
				return true;
			} catch (Exception e) {
				logger.error("Could not read default settings file", e);
				return false;
			}
		}
	}

	/**
	 * Save the Settings
	 * 
	 * @param noShutdown When the application is not shutdowned
	 * @return True if successful, false otherwise
	 */
	public synchronized boolean writeSettings(boolean noShutdown) {
		try (OutputStream out = Files.newOutputStream(settingsFile)) {
			writeSettingsFile(this.settings, out, false);
			updateHosterSettingsMap();
			settingsChanged();
			if (noShutdown) {
				backupSettingsFile();
			}
			return true;
		} catch (IOException | SAXException | JAXBException e) {
			logger.error("Could not write settings file: {}", settingsFile, e);
			return false;
		}
	}

	/**
	 * @return the languageFirstRun
	 */
	public boolean isLanguageFirstRun() {
		return languageFirstRun;
	}

	/**
	 * Apply the log level
	 */
	private void applyLogLevel() {
		Level log4jLevel = LOG_LEVEL_MAPPING.get(settings.getLogLevel());
		if (log4jLevel == null) {
			logger.error("Unsupported log level: {}", settings.getLogLevel());
			return;
		}
		BUUtil.changeLog4JRootLoggerLevel(log4jLevel);
	}

	/**
	 * Sets the log level
	 * 
	 * @param logLevel Log Level
	 */
	public void setLogLevel(LogLevelSetting logLevel) {
		if (logLevel == null) {
			throw new IllegalArgumentException("logLevel is null");
		}

		Level log4jLevel = LOG_LEVEL_MAPPING.get(logLevel);
		if (log4jLevel == null) {
			logger.error("Unsupported log level: {}", logLevel);
			return;
		}
		settings.setLogLevel(logLevel);
		BUUtil.changeLog4JRootLoggerLevel(log4jLevel);
	}

	/**
	 * @param language Language
	 */
	public void setLanguage(String language) {
		settings.getGuiSettings().setLanguage(language);
		languageFirstRun = false;
	}

	/**
	 * @param lookAndFeel Look and Feel
	 */
	public void setLookAndFeel(LookAndFeelSetting lookAndFeel) {
		LookAndFeelSetting previousLAF = settings.getGuiSettings().getLookAndFeel();
		settings.getGuiSettings().setLookAndFeel(lookAndFeel);
		if (lookAndFeel != previousLAF) {
			String lafClassName = LOOK_AND_FEEL_CLASS_NAMES.get(lookAndFeel);
			if (lafClassName == null) {
				logger.error("LookAndFeelSetting missing in LOOK_AND_FEEL_CLASS_NAMES map!");
				return;
			}
			try {
				UIManager.setLookAndFeel(lafClassName);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
				logger.error("Could not set LookAndFeel", e);
				return;
			}

			for (BUSettingsListener listener : listeners) {
				listener.lookAndFeelChanged();
			}
		}
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public DirectorySettings getDirectorySettings() {
		return settings.getDirectorySettings();
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public ConnectionSettings getConnectionSettings() {
		return settings.getConnectionSettings();
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public UploadSettings getUploadSettings() {
		return settings.getUploadSettings();
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public GUISettings getGUISettings() {
		return settings.getGuiSettings();
	}

	/**
	 * Get Hoster Settings for given Hoster Name
	 * 
	 * @param hosterName Hoster Name
	 * @return Hoster Settings or null if not found
	 */
	public HosterSettings getHosterSettings(String hosterName) {
		return hosterSettingsMap.get(hosterName);
	}

	/**
	 * Get Hoster Settings Values for given Hoster Name
	 * 
	 * @param hosterName Hoster Name
	 * @return Hoster Settings or an empty Map if not found
	 */
	public Map<String, Object> getHosterSettingsValues(String hosterName) {
		Map<String, Object> settingsValues = new HashMap<>();
		HosterSettings hosterSettings = getHosterSettings(hosterName);
		if (hosterSettings == null || hosterSettings.getSettings().isEmpty()) {
			return settingsValues;
		}
		for (CustomSetting customSetting : hosterSettings.getSettings()) {
			Object value = parseCustomSettingValue(customSetting);
			settingsValues.put(customSetting.getName(), value);
		}
		return settingsValues;
	}

	/**
	 * Set Hoster Settings
	 * 
	 * @param hosterName Hoster Name
	 * @param settingsValues Settings Values
	 */
	public synchronized void setHosterSettings(String hosterName, Map<String, String> settingsValues) {
		HosterSettings hosterSettings = getHosterSettings(hosterName);
		if (hosterSettings == null) {
			hosterSettings = new HosterSettings();
			hosterSettings.setName(hosterName);
			hosterSettingsMap.put(hosterName, hosterSettings);
			settings.getHosterSettings().add(hosterSettings);
		}

		List<CustomSetting> customSettings = hosterSettings.getSettings();
		for (Map.Entry<String, String> setting : settingsValues.entrySet()) {
			CustomSetting customSetting = customSettings.stream().filter(x -> x.getName().equals(setting.getKey())).findFirst().orElse(null);
			if (customSetting == null) {
				customSetting = new CustomSetting();
				customSettings.add(customSetting);
			}
			customSetting.setDataType("string");
			customSetting.setName(setting.getKey());
			customSetting.setValue(setting.getValue());
		}
	}

	/**
	 * @param count Count
	 */
	public synchronized void increaseOverallUploadedFiles(int count) {
		settings.getUploadSettings().setUploadedFiles(settings.getUploadSettings().getUploadedFiles() + count);
	}

	/**
	 * @param size Size
	 */
	public synchronized void increaseOverallUploadedBytes(long size) {
		settings.getUploadSettings().setUploadedBytes(settings.getUploadSettings().getUploadedBytes() + size);
	}

	/**
	 * Parse Custom Setting Value
	 * 
	 * @param customSetting Custom Setting
	 * @return Parsed Value
	 */
	private Object parseCustomSettingValue(CustomSetting customSetting) {
		String dataType = customSetting.getDataType();
		String strValue = customSetting.getValue();
		return SettingsUtil.parseValue(dataType, strValue);
	}

	/**
	 * Gets the class name for the given LookAndFeel Setting
	 * 
	 * @param lookAndFeelSetting LookAndFeel Setting
	 * @return Class name for the given LookAndFeel Setting
	 */
	public static String getLookAndFeelClassName(LookAndFeelSetting lookAndFeelSetting) {
		String className = LOOK_AND_FEEL_CLASS_NAMES.get(lookAndFeelSetting);
		if (className == null) {
			return UIManager.getSystemLookAndFeelClassName();
		}
		return className;
	}

	/**
	 * Gets the name for the given LookAndFeel Setting
	 * 
	 * @param lookAndFeelSetting LookAndFeel Setting
	 * @return Name for the given LookAndFeel Setting
	 */
	public static String getLookAndFeelName(LookAndFeelSetting lookAndFeelSetting) {
		String name = LOOK_AND_FEEL_NAMES.get(lookAndFeelSetting);
		if (name == null) {
			return "OperatingSystem";
		}
		return name;
	}

	/**
	 * @return LookAndFeels
	 */
	public static List<LookAndFeelSetting> getLookAndFeels() {
		List<LookAndFeelSetting> lookAndFeels = new ArrayList<>();
		// Use entrySet instead of keySet, so that insertion order is preserved
		for (Map.Entry<LookAndFeelSetting, String> entry : LOOK_AND_FEEL_NAMES.entrySet()) {
			lookAndFeels.add(entry.getKey());
		}
		return lookAndFeels;
	}
}
