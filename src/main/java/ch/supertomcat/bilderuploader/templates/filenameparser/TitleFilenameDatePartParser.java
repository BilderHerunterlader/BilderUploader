package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import ch.supertomcat.bilderuploader.filenameparser.DateParser;
import ch.supertomcat.bilderuploader.filenameparser.FilenameDatePartParser;

/**
 * Parser for Date in filename
 */
public class TitleFilenameDatePartParser {
	/**
	 * Variable Name
	 */
	private final String variableName;

	/**
	 * Output Variable Name
	 */
	private final String outputVariableName;

	/**
	 * Date Parsers
	 */
	private final List<TitleFilenameDateParser> dateParsers = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param filenameDatePartParser Parser Definition
	 */
	public TitleFilenameDatePartParser(FilenameDatePartParser filenameDatePartParser) {
		variableName = filenameDatePartParser.getVariableName();
		if (filenameDatePartParser.getOutputVariableName() != null) {
			outputVariableName = filenameDatePartParser.getOutputVariableName();
		} else {
			outputVariableName = variableName;
		}

		for (DateParser dateParser : filenameDatePartParser.getDateParser()) {
			dateParsers.add(new TitleFilenameDateParser(dateParser));
		}
	}

	/**
	 * Returns the variableName
	 * 
	 * @return variableName
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Returns the outputVariableName
	 * 
	 * @return outputVariableName
	 */
	public String getOutputVariableName() {
		return outputVariableName;
	}

	/**
	 * Parse Date
	 * 
	 * @param part Part
	 * @return Parsed date or null
	 */
	public ZonedDateTime parseDate(String part) {
		for (TitleFilenameDateParser dateParser : dateParsers) {
			ZonedDateTime parsedDate = dateParser.parseDate(part);
			if (parsedDate != null) {
				return parsedDate;
			}
		}
		return null;
	}
}
