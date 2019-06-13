package ch.supertomcat.bilderuploader.upload;

import java.util.EventListener;

/**
 * Listener to provide information about size, target path and filename and so on
 */
public interface UploadFileListener extends EventListener {
	/**
	 * ProgressBar has changed
	 * 
	 * @param file File
	 */
	public void progressChanged(UploadFile file);

	/**
	 * Hoster has changed
	 * 
	 * @param file File
	 */
	public void hosterChanged(UploadFile file);

	/**
	 * Status has changed
	 * 
	 * @param file File
	 */
	public void statusChanged(UploadFile file);

	/**
	 * Upload is deactivated
	 * 
	 * @param file File
	 */
	public void deactivatedChanged(UploadFile file);

	/**
	 * Result has changed
	 * 
	 * @param file File
	 */
	public void resultChanged(UploadFile file);
}
