package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.util.regex.Pattern;

import ch.supertomcat.bilderuploader.filenameparser.PatternReplacement;

/**
 * Pattern Replacement
 */
public class TitleFilenamePatternReplacement {
	/**
	 * Pattern
	 */
	private final Pattern pattern;

	/**
	 * Replacement
	 */
	private final String replacement;

	/**
	 * Constructor
	 * 
	 * @param patternReplacement Definition
	 */
	public TitleFilenamePatternReplacement(PatternReplacement patternReplacement) {
		pattern = Pattern.compile(patternReplacement.getPattern());
		replacement = patternReplacement.getReplacement();
	}

	/**
	 * Replace part
	 * 
	 * @param part Part
	 * @return Replaced part
	 */
	public String replace(String part) {
		return pattern.matcher(part).replaceAll(replacement);
	}
}
