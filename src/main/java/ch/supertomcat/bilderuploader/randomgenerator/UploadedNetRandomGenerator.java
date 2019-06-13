package ch.supertomcat.bilderuploader.randomgenerator;

import java.util.Map;

/**
 * Random Generator for Uploaded.net
 */
public class UploadedNetRandomGenerator implements RandomGenerator {
	private static char[] con = new char[] { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };
	private static char[] voc = new char[] { 'a', 'e', 'i', 'o', 'u' };

	@Override
	public String generateRandom(Map<String, String> params) {
		StringBuilder sbRandom = new StringBuilder();
		int len = Integer.parseInt(params.get("len"));
		for (int i = 0; i < len / 2; i++) {
			int c = (int)(Math.ceil(Math.random() * 1000) % 20);
			int v = (int)(Math.ceil(Math.random() * 1000) % 5);
			sbRandom.append(con[c]);
			sbRandom.append(voc[v]);
		}
		return sbRandom.toString();
	}
}
