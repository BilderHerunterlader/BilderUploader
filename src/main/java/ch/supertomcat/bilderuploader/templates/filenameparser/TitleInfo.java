package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * Title Information
 */
public class TitleInfo {
	/**
	 * Filename Parts
	 */
	private final Map<String, String> filenameParts;

	/**
	 * Date Parts
	 */
	private final Map<String, ZonedDateTime> dateParts;

	/**
	 * Formatted Title or null
	 */
	private final String formattedTitle;

	/**
	 * Constructor
	 * 
	 * @param filenameParts Filename Parts
	 * @param dateParts Date Parts
	 * @param formattedTitle Formatted Title or null
	 */
	public TitleInfo(Map<String, String> filenameParts, Map<String, ZonedDateTime> dateParts, String formattedTitle) {
		this.filenameParts = filenameParts;
		this.dateParts = dateParts;
		this.formattedTitle = formattedTitle;
	}

	/**
	 * Format Date Time
	 * 
	 * @param key Date Part Key
	 * @param dateFormat Date Format
	 * @param locale Locale or null
	 * @return Formatted Date Time
	 */
	public String getFormattedDateTime(String key, String dateFormat, String locale) {
		Locale parsedLocale;
		if (locale != null) {
			parsedLocale = Locale.forLanguageTag(locale);
		} else {
			parsedLocale = Locale.getDefault();
		}
		ZoneId zone = ZoneOffset.UTC;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat).withLocale(parsedLocale).withZone(zone);

		ZonedDateTime date = dateParts.get(key);
		if (date == null) {
			return "";
		}
		return date.format(dateFormatter);
	}

	/**
	 * Returns the filenameParts
	 * 
	 * @return filenameParts
	 */
	public Map<String, String> getFilenameParts() {
		return filenameParts;
	}

	/**
	 * Returns the dateParts
	 * 
	 * @return dateParts
	 */
	public Map<String, ZonedDateTime> getDateParts() {
		return dateParts;
	}

	/**
	 * Returns the formattedTitle
	 * 
	 * @return formattedTitle
	 */
	public String getFormattedTitle() {
		return formattedTitle;
	}

	@Override
	public String toString() {
		return "TitleInfo [filenameParts=" + filenameParts + ", dateParts=" + dateParts + ", formattedTitle=" + formattedTitle + "]";
	}
}
