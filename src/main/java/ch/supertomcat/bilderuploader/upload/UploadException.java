package ch.supertomcat.bilderuploader.upload;

/**
 * Upload Exception
 */
public class UploadException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message Message
	 * @param cause Cause
	 */
	public UploadException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message Message
	 */
	public UploadException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause Cause
	 */
	public UploadException(Throwable cause) {
		super(cause);
	}
}
