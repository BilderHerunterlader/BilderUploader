package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.filenameparser.DateParser;

/**
 * Parser for Date in filename
 */
public class TitleFilenameDateParser {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Pattern for Date
	 */
	private final Pattern datePattern;

	/**
	 * Date Format
	 */
	private final DateTimeFormatter dateFormat;

	/**
	 * Constructor
	 * 
	 * @param dateParser Parser Definition
	 */
	public TitleFilenameDateParser(DateParser dateParser) {
		datePattern = Pattern.compile(dateParser.getDatePattern());
		Locale locale;
		if (dateParser.getDateLocale() != null) {
			locale = Locale.forLanguageTag(dateParser.getDateLocale());
		} else {
			locale = Locale.getDefault();
		}
		ZoneId zone = ZoneOffset.UTC;
		dateFormat = new DateTimeFormatterBuilder().appendPattern(dateParser.getDateFormat()).parseDefaulting(ChronoField.YEAR_OF_ERA, 0).parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
				.parseDefaulting(ChronoField.DAY_OF_MONTH, 1).parseDefaulting(ChronoField.MONTH_OF_YEAR, 1).parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter().withLocale(locale).withZone(zone);
	}

	/**
	 * Parse date from part
	 * 
	 * @param part Part
	 * @return Date or null
	 */
	public ZonedDateTime parseDate(String part) {
		Matcher matcher = datePattern.matcher(part);
		if (matcher.matches()) {
			try {
				return ZonedDateTime.parse(part, dateFormat);
			} catch (DateTimeParseException e) {
				logger.error("Could not parse date", e);
				return null;
			}
		}
		return null;
	}
}
