package ch.supertomcat.bilderuploader.randomgenerator;

import java.util.Map;

/**
 * Random Generator
 */
public interface RandomGenerator {
	/**
	 * Generate Random
	 * 
	 * @param params Parameters
	 * @return Generated Random
	 */
	public String generateRandom(Map<String, String> params);
}
