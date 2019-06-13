package ch.supertomcat.bilderuploader.hoster;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.hosterconfig.ObjectFactory;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;

/**
 * Class for handling hosters
 */
public class HosterManager {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Folder
	 */
	private final File folder;

	/**
	 * Hosts
	 */
	private final List<Hoster> hosts = new ArrayList<>();

	/**
	 * Selected Hoster
	 */
	private Hoster selectedHoster = null;

	/**
	 * Constructor
	 */
	public HosterManager() {
		this(new File(ApplicationProperties.getProperty("ApplicationPath"), "hosts/"));
	}

	/**
	 * Constructor
	 * 
	 * @param folder Folder
	 */
	public HosterManager(File folder) {
		this.folder = folder;
		loadHosts();
	}

	/**
	 * Load Hosts
	 */
	private void loadHosts() {
		for (File xmlFile : getHosterFiles()) {
			try (FileInputStream in = new FileInputStream(xmlFile);
					FileInputStream schemaIn = new FileInputStream(new File(ApplicationProperties.getProperty("ApplicationPath"), "hosts/hoster.xsd"))) {
				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Source schemaSource = new StreamSource(schemaIn);
				Schema schema = sf.newSchema(schemaSource);
				unmarshaller.setSchema(schema);
				Hoster hoster = (Hoster)unmarshaller.unmarshal(in);
				hosts.add(hoster);
			} catch (JAXBException | IOException | SAXException e) {
				logger.error("Could not load hoster: {}", xmlFile.getAbsolutePath(), e);
			}
		}

		if (!hosts.isEmpty()) {
			selectedHoster = hosts.get(0);
		}
	}

	/**
	 * Returns the hosts
	 * 
	 * @return hosts
	 */
	public List<Hoster> getHosts() {
		return hosts;
	}

	/**
	 * Get host by name
	 * 
	 * @param name Hoster Name
	 * @return Hoster or null if not found
	 */
	public Hoster getHostByName(String name) {
		return hosts.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * Returns the selectedHoster
	 * 
	 * @return selectedHoster
	 */
	public Hoster getSelectedHoster() {
		return selectedHoster;
	}

	/**
	 * Sets the selectedHoster
	 * 
	 * @param selectedHoster selectedHoster
	 */
	public void setSelectedHoster(Hoster selectedHoster) {
		this.selectedHoster = selectedHoster;
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
	 * @return List of Hoster Files
	 */
	public List<File> getHosterFiles() {
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
}
