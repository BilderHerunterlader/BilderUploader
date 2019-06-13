package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.supertomcat.bilderuploader.filenameparser.FilenameSplitter;
import ch.supertomcat.bilderuploader.filenameparser.VariableReplacement;

/**
 * Splitter for filenames
 */
public class TitleFilenameSplitter {
	/**
	 * Pattern
	 */
	private final Pattern pattern;

	/**
	 * Replacements
	 */
	private final List<VariableReplacement> replacements;

	/**
	 * Constructor
	 * 
	 * @param filenameSplitter Filename Splitter Definition
	 */
	public TitleFilenameSplitter(FilenameSplitter filenameSplitter) {
		pattern = Pattern.compile(filenameSplitter.getPattern());
		replacements = filenameSplitter.getVariableReplacement();
	}

	/**
	 * Split Filename into parts
	 * 
	 * @param filename Filename
	 * @return Filename Parts or null if pattern not machted
	 */
	public Map<String, String> splitFilename(String filename) {
		Matcher matcher = pattern.matcher(filename);
		if (matcher.matches()) {
			Map<String, String> parts = new LinkedHashMap<>();
			for (VariableReplacement replacement : replacements) {
				String value = matcher.replaceAll(replacement.getReplacement());
				parts.put(replacement.getVariableName(), value);
			}
			return parts;
		}
		return null;
	}
}
