package ch.supertomcat.bilderuploader.queue;

import java.util.List;

import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * Listener for QueueManager
 */
public interface QueueManagerListener {
	/**
	 * @param file File
	 */
	public void fileAdded(UploadFile file);

	/**
	 * @param files Files
	 */
	public void filesAdded(List<UploadFile> files);

	/**
	 * @param file File
	 * @param index Index
	 */
	public void fileRemoved(UploadFile file, int index);

	/**
	 * @param removedIndeces Removed Indeces
	 */
	public void filesRemoved(int removedIndeces[]);

	/**
	 * Progress has changed
	 * 
	 * @param file File
	 * @param index Index
	 */
	public void fileProgressChanged(UploadFile file, int index);

	/**
	 * Hoster has changed
	 * 
	 * @param file File
	 * @param index Index
	 */
	public void fileHosterChanged(UploadFile file, int index);

	/**
	 * Status has changed
	 * 
	 * @param file File
	 * @param index Index
	 */
	public void fileStatusChanged(UploadFile file, int index);

	/**
	 * Upload is deactivated
	 * 
	 * @param file File
	 * @param index Index
	 */
	public void fileDeactivatedChanged(UploadFile file, int index);

	/**
	 * Session Uploaded Files increased
	 */
	public void increaseSessionUploadedFiles();

	/**
	 * Session Uploaded Bytes increased
	 * 
	 * @param size Size
	 */
	public void increaseSessionUploadedBytes(long size);

	/**
	 * Start Upload
	 * 
	 * @param file File
	 */
	public void startUpload(UploadFile file);

	/**
	 * Start Upload
	 * 
	 * @param files Files
	 */
	public void startUpload(List<UploadFile> files);

	/**
	 * Stop Upload
	 * 
	 * @param cancelAlreadyExecutingTasks Flag if already running tasks should be cancelled
	 */
	public void stopUpload(boolean cancelAlreadyExecutingTasks);
}
