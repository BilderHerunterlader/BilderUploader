package ch.supertomcat.bilderuploader.queue;

/**
 * UploadQueueManager Listener
 */
public interface UploadQueueManagerListener {
	/**
	 * Queue has changed.
	 * 
	 * @param queue Amount of downloads in the queue
	 * @param openSlots Free download-slots
	 * @param maxSlots Maximum download-slots
	 */
	public void queueChanged(int queue, int openSlots, int maxSlots);

	/**
	 * Changes of the count of downloaded files since application started
	 * 
	 * @param count Count of downloaded files
	 */
	public void sessionUploadedFilesChanged(int count);

	/**
	 * Changes of the count of downloaded bytes since application started
	 * 
	 * @param count Count of downloaded bytes
	 */
	public void sessionUploadedBytesChanged(long count);

	/**
	 * TODO The parameters don't make any sense
	 * 
	 * @param queue Queue
	 * @param openSlots Open Slots
	 * @param maxSlots Max Slots
	 */
	public void uploadsComplete(int queue, int openSlots, int maxSlots);

	/**
	 * @param rate Rate
	 */
	public void totalUploadRateCalculated(double rate);
}
