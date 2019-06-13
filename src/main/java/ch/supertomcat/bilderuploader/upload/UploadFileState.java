package ch.supertomcat.bilderuploader.upload;

import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.Localization;

/**
 * UploadFile State Enum
 */
public enum UploadFileState {
	/**
	 * Download is sleeping
	 */
	SLEEPING("Sleeping"),

	/**
	 * Download is waiting for a free slot
	 */
	WAITING("Waiting"),

	/**
	 * Upload is uploading
	 */
	UPLOADING("Uploading"),

	/**
	 * Download is complete
	 */
	COMPLETE("Complete"),

	/**
	 * Download failed
	 */
	FAILED("Failed"),

	/**
	 * Download failed
	 */
	ABORTING("Aborting");

	/**
	 * Locale Key
	 */
	private final String localeKey;

	/**
	 * Constructor
	 * 
	 * @param localeKey Locale Key
	 */
	private UploadFileState(String localeKey) {
		this.localeKey = localeKey;
	}

	/**
	 * Returns the localeKey
	 * 
	 * @return localeKey
	 */
	public String getLocaleKey() {
		return localeKey;
	}

	/**
	 * @return Text
	 */
	public String getText() {
		return Localization.getString(localeKey);
	}

	/**
	 * Get UploadFileState by name
	 * 
	 * @param name Name
	 * @return UploadFileState
	 */
	public static UploadFileState getByName(String name) {
		for (UploadFileState state : UploadFileState.values()) {
			if (state.name().equals(name)) {
				return state;
			}
		}
		LoggerFactory.getLogger(UploadFileState.class).error("Unknown value: {}", name);
		return SLEEPING;
	}
}
