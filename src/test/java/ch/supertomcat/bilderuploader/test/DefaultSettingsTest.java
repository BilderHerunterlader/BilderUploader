package ch.supertomcat.bilderuploader.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import ch.supertomcat.bilderuploader.settingsconfig.ObjectFactory;
import ch.supertomcat.bilderuploader.settingsconfig.Settings;

/**
 * Default Settings Test
 */
@SuppressWarnings("javadoc")
public class DefaultSettingsTest {
	@Test
	public void testTemplate() throws IOException, SAXException, JAXBException {
		Settings settings = loadSettingsFile("/ch/supertomcat/bilderuploader/settingsconfig/default-settings.xml");
		assertNotNull(settings.getDirectorySettings());
		assertNotNull(settings.getConnectionSettings());
		assertNotNull(settings.getUploadSettings());
		assertNotNull(settings.getGuiSettings());
		assertNotNull(settings.getLogLevel());
		assertNotNull(settings.getHosterSettings());
	}

	private Settings loadSettingsFile(String resourceFile) throws IOException, SAXException, JAXBException {
		try (InputStream in = getClass().getResourceAsStream(resourceFile)) {
			if (in == null) {
				throw new IllegalArgumentException("Resource not found: " + resourceFile);
			}

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(getClass().getResource("/ch/supertomcat/bilderuploader/settingsconfig/settings.xsd"));

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setSchema(schema);
			return (Settings)unmarshaller.unmarshal(in);
		}
	}
}
