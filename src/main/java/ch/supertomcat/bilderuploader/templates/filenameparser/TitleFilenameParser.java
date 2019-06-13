package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ch.supertomcat.bilderuploader.filenameparser.FilenameDatePartParser;
import ch.supertomcat.bilderuploader.filenameparser.FilenameParser;
import ch.supertomcat.bilderuploader.filenameparser.FilenamePartParser;
import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * Parser for creating a title out of filenames
 */
public class TitleFilenameParser {
	/**
	 * File
	 */
	private final File file;

	/**
	 * Splitter
	 */
	private final TitleFilenameSplitter titleFilenameSplitter;

	/**
	 * Part Parsers
	 */
	private final Map<String, TitleFilenamePartParser> partParsers = new HashMap<>();

	/**
	 * Date Parsers
	 */
	private final Map<String, TitleFilenameDatePartParser> datePartParsers = new HashMap<>();

	/**
	 * Title Filename Formatter or null
	 */
	private final TitleFilenameFormatter titleFilenameFormatter;

	/**
	 * Constructor
	 * 
	 * @param xmlFile XML File
	 * @param filenameParser Parser Definition
	 */
	public TitleFilenameParser(File xmlFile, FilenameParser filenameParser) {
		this.file = xmlFile;
		titleFilenameSplitter = new TitleFilenameSplitter(filenameParser.getFilenameSplitter());

		for (FilenamePartParser filenamePartParser : filenameParser.getFilenamePartParser()) {
			partParsers.put(filenamePartParser.getVariableName(), new TitleFilenamePartParser(filenamePartParser));
		}

		for (FilenameDatePartParser filenameDatePartParser : filenameParser.getFilenameDatePartParser()) {
			datePartParsers.put(filenameDatePartParser.getVariableName(), new TitleFilenameDatePartParser(filenameDatePartParser));
		}

		titleFilenameFormatter = new TitleFilenameFormatter(filenameParser.getTitleFormatter());
	}

	/**
	 * Try to create the title by given filenames
	 * 
	 * @param filesToAnalyze Files to analyze
	 * @return TitleInfo if a title could be parsed, null otherwise
	 */
	public TitleInfo getTitleByFilenames(List<UploadFile> filesToAnalyze) {
		for (UploadFile file : filesToAnalyze) {
			Map<String, String> filenameParts = titleFilenameSplitter.splitFilename(file.getFile().getName());
			if (filenameParts == null) {
				return null;
			}

			for (Map.Entry<String, TitleFilenamePartParser> entry : partParsers.entrySet()) {
				String part = filenameParts.get(entry.getKey());
				String parsedPart = entry.getValue().parsePart(part);
				filenameParts.put(entry.getValue().getOutputVariableName(), parsedPart);
			}

			Map<String, ZonedDateTime> dateParts = new LinkedHashMap<>();
			for (Map.Entry<String, TitleFilenameDatePartParser> entry : datePartParsers.entrySet()) {
				String part = filenameParts.get(entry.getKey());
				ZonedDateTime parsedDate = entry.getValue().parseDate(part);
				dateParts.put(entry.getValue().getOutputVariableName(), parsedDate);
			}

			if (!filenameParts.isEmpty() || !dateParts.isEmpty()) {
				String formattedTitle = null;
				if (titleFilenameFormatter != null) {
					formattedTitle = titleFilenameFormatter.formatTitle(filenameParts);
				}
				return new TitleInfo(filenameParts, dateParts, formattedTitle);
			}
		}
		return null;
	}

	/**
	 * Returns the file
	 * 
	 * @return file
	 */
	public File getFile() {
		return file;
	}
}
