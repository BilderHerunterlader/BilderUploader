package ch.supertomcat.bilderuploader.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.templates.TemplateManager;

/**
 * Hoster Test
 */
@SuppressWarnings("javadoc")
public class TemplateTest {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testTemplate() throws IOException {
		TemplateManager templateManager = new TemplateManager();

		Map<String, Object> vars = new LinkedHashMap<>();
		vars.put("Test1", 1234.56f);
		vars.put("Testtest1", "Bla");
		vars.put("fileNames", Arrays.asList("AAA.txt", "BBB.txt", "CCC.txt", "DDD.txt"));

		String renderedTemplate = templateManager.renderTemplate("/templates/test.vm", vars);
		logger.info("Rendered Template:\n{}", renderedTemplate);

		String expectedResult = readResourceFile("/templates/renderedTest.txt");
		assertEquals(expectedResult, renderedTemplate);
	}

	private String readResourceFile(String resourceFile) throws IOException {
		try (InputStream in = getClass().getResourceAsStream(resourceFile)) {
			if (in == null) {
				throw new IllegalArgumentException("Resource not found: " + resourceFile);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			return new String(out.toByteArray(), StandardCharsets.UTF_8);
		}
	}
}
