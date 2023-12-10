package ch.supertomcat.bilderuploader.upload;

import java.util.List;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.StatusLine;

/**
 * Container Page
 */
public class ContainerPage {
	/**
	 * True if successful, false otherwise
	 */
	private final boolean success;

	/**
	 * Page Source Code
	 */
	private final String page;

	/**
	 * Redirected URL if request was redirected or null
	 */
	private final String redirectedURL;

	/**
	 * Status Line
	 */
	private final StatusLine statusLine;

	/**
	 * Response Headers
	 */
	private final List<Header> responseHeaders;

	/**
	 * Constructor
	 * 
	 * @param success True if successful, false otherwise
	 * @param page Page Source Code
	 * @param redirectedURL Redirected URL if request was redirected or null
	 * @param statusLine Status Line
	 * @param responseHeaders Response Headers
	 */
	public ContainerPage(boolean success, String page, String redirectedURL, StatusLine statusLine, List<Header> responseHeaders) {
		this.success = success;
		this.page = page;
		this.redirectedURL = redirectedURL;
		this.statusLine = statusLine;
		this.responseHeaders = responseHeaders;
	}

	/**
	 * Returns the success
	 * 
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return Page Source Code
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @return True if the request was redirected, false otherwise
	 */
	public boolean isRedirected() {
		return redirectedURL != null;
	}

	/**
	 * @return Redirected URL if request was redirected or null
	 */
	public String getRedirectedURL() {
		return redirectedURL;
	}

	/**
	 * Returns the statusLine
	 * 
	 * @return statusLine
	 */
	public StatusLine getStatusLine() {
		return statusLine;
	}

	/**
	 * Returns the responseHeaders
	 * 
	 * @return responseHeaders
	 */
	public List<Header> getResponseHeaders() {
		return responseHeaders;
	}

	@Override
	public String toString() {
		return "ContainerPage [success=" + success + ", redirectedURL=" + redirectedURL + ", statusLine=" + statusLine + "]";
	}
}
