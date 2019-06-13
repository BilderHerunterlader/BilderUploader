package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.util.ArrayList;
import java.util.List;

import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * Container for Title Filename Parser
 */
public class TitleFilenameParserContainer {
	/**
	 * Parsers
	 */
	private final List<TitleFilenameParser> parsers;

	/**
	 * Generated Titles
	 */
	private final List<TitleInfo> titles = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param parsers Parsers
	 */
	public TitleFilenameParserContainer(List<TitleFilenameParser> parsers) {
		this.parsers = parsers;
	}

	/**
	 * Try to create the title by given filenames
	 * 
	 * @param filesToAnalyze Files to analyze
	 * @return TitleInfo if a title could be parsed, null otherwise
	 */
	public TitleInfo getTitleByFilenames(List<UploadFile> filesToAnalyze) {
		for (TitleFilenameParser parser : parsers) {
			TitleInfo titleInfo = parser.getTitleByFilenames(filesToAnalyze);
			if (titleInfo != null) {
				titles.add(titleInfo);
				return titleInfo;
			}
		}
		return null;
	}

	/**
	 * Returns the titles
	 * 
	 * @return titles
	 */
	public List<TitleInfo> getTitles() {
		return titles;
	}
}
