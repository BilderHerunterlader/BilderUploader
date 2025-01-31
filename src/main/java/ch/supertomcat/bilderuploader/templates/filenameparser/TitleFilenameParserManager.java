package ch.supertomcat.bilderuploader.templates.filenameparser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ch.supertomcat.bilderuploader.filenameparser.FilenameParser;
import ch.supertomcat.bilderuploader.filenameparser.ObjectFactory;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Class for handling filename parsers
 */
public class TitleFilenameParserManager {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Folder
	 */
	private final File folder;

	/**
	 * Title Filename Parsers
	 */
	private final List<TitleFilenameParser> titleFilenameParsers = new ArrayList<>();

	/**
	 * Constructor
	 */
	public TitleFilenameParserManager() {
		this(new File(ApplicationProperties.getProperty("SettingsPath"), "filenameParsers"));
	}

	/**
	 * Constructor
	 * 
	 * @param folder Folder
	 */
	public TitleFilenameParserManager(File folder) {
		this.folder = folder;
		loadFilenameParsers();
	}

	/**
	 * Load Hosts
	 */
	private void loadFilenameParsers() {
		for (File xmlFile : getFilenameParserFiles()) {
			try (FileInputStream in = new FileInputStream(xmlFile); InputStream schemaIn = getClass().getResourceAsStream("filenameParser.xsd")) {
				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Source schemaSource = new StreamSource(schemaIn);
				Schema schema = sf.newSchema(schemaSource);
				unmarshaller.setSchema(schema);
				FilenameParser filenameParser = (FilenameParser)unmarshaller.unmarshal(in);

				TitleFilenameParser titleFilenameParser = new TitleFilenameParser(xmlFile, filenameParser);
				titleFilenameParsers.add(titleFilenameParser);
			} catch (JAXBException | IOException | SAXException e) {
				logger.error("Could not load hoster: {}", xmlFile.getAbsolutePath(), e);
			}
		}
	}

	/**
	 * Returns the folder
	 * 
	 * @return folder
	 */
	public File getFolder() {
		return folder;
	}

	/**
	 * Returns the titleFilenameParsers
	 * 
	 * @return titleFilenameParsers
	 */
	public List<TitleFilenameParser> getTitleFilenameParsers() {
		return titleFilenameParsers;
	}

	/**
	 * @return List of Filename Parser Files
	 */
	public List<File> getFilenameParserFiles() {
		File[] xmlFiles = folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(".xml");
			}
		});
		if (xmlFiles == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(Arrays.asList(xmlFiles));
	}

	/**
	 * @param filename Filename
	 * @return Title Filename Parser or null if not found
	 */
	public TitleFilenameParser getTitleFilenameParser(String filename) {
		for (TitleFilenameParser titleFilenameParser : titleFilenameParsers) {
			if (titleFilenameParser.getFile().getName().equals(filename)) {
				return titleFilenameParser;
			}
		}
		return null;
	}
}
