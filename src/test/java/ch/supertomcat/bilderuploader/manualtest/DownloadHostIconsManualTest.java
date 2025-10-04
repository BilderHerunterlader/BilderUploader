package ch.supertomcat.bilderuploader.manualtest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.supertomcatutils.http.HTTPUtil;

/**
 * Hoster Test
 */
@SuppressWarnings("javadoc")
public class DownloadHostIconsManualTest {
	private static final Pattern FAVICON_PATTERN = Pattern.compile("<link rel=['\"]shortcut icon['\"].+?href=['\"](.+?\\.ico)['\"]");

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testDownloadHostIcons() {
		boolean overallSuccess = true;
		HosterManager hosterManager = new HosterManager();

		File folder = new File("hostIcons");

		for (Hoster hoster : hosterManager.getHosts()) {
			boolean success = false;
			String url = hoster.getHomePage();
			String displayIcon = hoster.getDisplayIcon();
			if (url == null || displayIcon == null) {
				continue;
			}
			File outputFile = new File(folder, displayIcon);

			try {
				String page = downloadHosterPage(url);
				if (page != null) {
					Matcher matcher = FAVICON_PATTERN.matcher(page);
					if (matcher.find()) {
						String icoURL = matcher.group(1);
						if (icoURL.startsWith("/")) {
							if (downloadHosterIcon(url + icoURL, outputFile)) {
								success = true;
							}
						} else {
							if (downloadHosterIcon(icoURL, outputFile)) {
								success = true;
							}
						}
					}
				}
			} catch (IOException e) {
				logger.error("Could not download hoster homepage: {}", hoster.getName(), e);
			}

			if (!success) {
				try {
					if (downloadHosterIcon(url + "/favicon.ico", outputFile)) {
						success = true;
					}
				} catch (IOException e) {
					logger.error("Could not download favicon for hoster: {}", hoster.getName(), e);
				}
			}

			if (!success) {
				overallSuccess = false;
			}
		}

		assertTrue(overallSuccess);
	}

	@SuppressWarnings("resource")
	private boolean downloadHosterIcon(String hosterIconURL, File outputFile) throws IOException {
		URL url = HTTPUtil.parseURL(hosterIconURL);
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(60000);
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				writeToFile(con.getInputStream(), outputFile);
				return true;
			} else {
				readAll(con.getInputStream());
				readAll(con.getErrorStream());
				return false;
			}
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	@SuppressWarnings("resource")
	private String downloadHosterPage(String hosterURL) throws IOException {
		URL url = HTTPUtil.parseURL(hosterURL);
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(60000);
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String encoding = con.getContentEncoding();
				return readToString(con.getInputStream(), encoding);
			} else {
				readAll(con.getInputStream());
				readAll(con.getErrorStream());
				return null;
			}
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	private void writeToFile(InputStream in, File outputFile) throws FileNotFoundException, IOException {
		try (FileOutputStream out = new FileOutputStream(outputFile)) {
			byte[] buffer = new byte[8192];
			int read;
			while ((read = in.read(buffer)) != -1) {
				if (read > 0) {
					out.write(buffer, 0, read);
					out.flush();
				}
			}
		}
	}

	private String readToString(InputStream in, String encoding) throws IOException {
		if (in == null) {
			return null;
		}
		Charset charset;
		if (encoding != null) {
			charset = Charset.forName(encoding);
		} else {
			charset = StandardCharsets.UTF_8;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		return new String(out.toByteArray(), charset);
	}

	private void readAll(InputStream in) throws IOException {
		if (in == null) {
			return;
		}
		byte[] buffer = new byte[8192];
		@SuppressWarnings("unused")
		int read;
		while ((read = in.read(buffer)) != -1) {
			// Nothing to do
		}
	}
}
