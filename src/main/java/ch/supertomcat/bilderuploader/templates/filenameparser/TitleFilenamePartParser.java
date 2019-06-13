package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.util.ArrayList;
import java.util.List;

import ch.supertomcat.bilderuploader.filenameparser.FilenamePartParser;
import ch.supertomcat.bilderuploader.filenameparser.PatternReplacement;

/**
 * Parser for Date in filename
 */
public class TitleFilenamePartParser {
	/**
	 * Variable Name
	 */
	private final String variableName;

	/**
	 * Output Variable Name
	 */
	private final String outputVariableName;

	/**
	 * Replacements
	 */
	private final List<TitleFilenamePatternReplacement> replacements = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param filenamePartParser Parser Definition
	 */
	public TitleFilenamePartParser(FilenamePartParser filenamePartParser) {
		variableName = filenamePartParser.getVariableName();
		if (filenamePartParser.getOutputVariableName() != null) {
			outputVariableName = filenamePartParser.getOutputVariableName();
		} else {
			outputVariableName = variableName;
		}

		for (PatternReplacement patternReplacement : filenamePartParser.getPatternReplacement()) {
			replacements.add(new TitleFilenamePatternReplacement(patternReplacement));
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
	 * Parse part
	 * 
	 * @param part Part
	 * @return Parsed Part
	 */
	public String parsePart(String part) {
		String replacedPart = part;
		for (TitleFilenamePatternReplacement replacement : replacements) {
			replacedPart = replacement.replace(replacedPart);
		}
		return replacedPart;
	}
}
