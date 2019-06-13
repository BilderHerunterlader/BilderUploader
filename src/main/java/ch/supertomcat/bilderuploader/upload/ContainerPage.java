package ch.supertomcat.bilderuploader.upload;

import org.apache.http.StatusLine;

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
	 * Constructor
	 * 
	 * @param success True if successful, false otherwise
	 * @param page Page Source Code
	 * @param redirectedURL Redirected URL if request was redirected or null
	 * @param statusLine Status Line
	 */
	public ContainerPage(boolean success, String page, String redirectedURL, StatusLine statusLine) {
		this.success = success;
		this.page = page;
		this.redirectedURL = redirectedURL;
		this.statusLine = statusLine;
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

	@Override
	public String toString() {
		return "ContainerPage [success=" + success + ", redirectedURL=" + redirectedURL + ", statusLine=" + statusLine + "]";
	}
}
