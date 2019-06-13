package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import ch.supertomcat.bilderuploader.filenameparser.PatternReplacement;
import ch.supertomcat.bilderuploader.filenameparser.TitleFormatter;

/**
 * Parser for Date in filename
 */
public class TitleFilenameFormatter {
	/**
	 * Title Format
	 */
	private final String titleFormat;

	/**
	 * Replacements
	 */
	private final List<TitleFilenamePatternReplacement> replacements = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param titleFormatter Formatter Definition
	 */
	public TitleFilenameFormatter(TitleFormatter titleFormatter) {
		titleFormat = titleFormatter.getFormat();

		for (PatternReplacement patternReplacement : titleFormatter.getPatternReplacement()) {
			replacements.add(new TitleFilenamePatternReplacement(patternReplacement));
		}
	}

	/**
	 * Format Title
	 * 
	 * @param values Variable Values
	 * @return Formatted Title
	 */
	public String formatTitle(Map<String, String> values) {
		String formattedTitle = StringSubstitutor.replace(titleFormat, values);
		for (TitleFilenamePatternReplacement replacement : replacements) {
			formattedTitle = replacement.replace(formattedTitle);
		}
		return formattedTitle;
	}
}
