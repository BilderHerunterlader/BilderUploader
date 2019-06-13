package ch.supertomcat.bilderuploader.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;

/**
 * Hoster Test
 */
@SuppressWarnings("javadoc")
public class HosterTest {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testLoadHosts() {
		HosterManager hosterManager = new HosterManager();
		List<Hoster> hosts = hosterManager.getHosts();
		logger.info("Hosts: {}", hosts);

		assertEquals(hosterManager.getHosterFiles().size(), hosts.size());
	}
}
