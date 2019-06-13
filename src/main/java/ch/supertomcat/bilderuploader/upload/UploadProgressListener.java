package ch.supertomcat.bilderuploader.upload;

/**
 * Upload Progress Listener
 */
public interface UploadProgressListener {
	/**
	 * Progress
	 * 
	 * @param bytesTotal Bytes Total or -1 if unknown
	 * @param bytesCompleted Bytes Completed
	 * @param percent Percent
	 * @param rate Rate or -1 if not known
	 */
	public void progress(long bytesTotal, long bytesCompleted, float percent, double rate);

	/**
	 * Complete
	 * 
	 * @param bytesTotal Bytes Total or -1 if unknown
	 * @param bytesCompleted Bytes Completed
	 * @param duration Duration
	 */
	public void complete(long bytesTotal, long bytesCompleted, long duration);

	/**
	 * @param state State
	 */
	public void statusChanged(UploadFileState state);
}
