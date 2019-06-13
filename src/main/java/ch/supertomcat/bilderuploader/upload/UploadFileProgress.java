package ch.supertomcat.bilderuploader.upload;

/**
 * Upload File Progress
 */
public class UploadFileProgress {
	/**
	 * Bytes Total
	 */
	private long bytesTotal = 0;

	/**
	 * Bytes Uploaded
	 */
	private long bytesUploaded = 0;

	/**
	 * Percent
	 */
	private float percent = 0;

	/**
	 * Percent as int
	 */
	private int percentInt = 0;

	/**
	 * Upload Rate
	 */
	private double rate = 0;

	/**
	 * Constructor
	 */
	public UploadFileProgress() {
	}

	/**
	 * Returns the bytesTotal
	 * 
	 * @return bytesTotal
	 */
	public long getBytesTotal() {
		return bytesTotal;
	}

	/**
	 * Sets the bytesTotal
	 * 
	 * @param bytesTotal bytesTotal
	 */
	public void setBytesTotal(long bytesTotal) {
		this.bytesTotal = bytesTotal;
		calculatePercent();
	}

	/**
	 * Returns the bytesUploaded
	 * 
	 * @return bytesUploaded
	 */
	public long getBytesUploaded() {
		return bytesUploaded;
	}

	/**
	 * Sets the bytesUploaded
	 * 
	 * @param bytesUploaded bytesUploaded
	 */
	public void setBytesUploaded(long bytesUploaded) {
		this.bytesUploaded = bytesUploaded;
		calculatePercent();
	}

	/**
	 * Returns the percent
	 * 
	 * @return percent
	 */
	public float getPercent() {
		return percent;
	}

	/**
	 * Returns the percentInt
	 * 
	 * @return percentInt
	 */
	public int getPercentInt() {
		return percentInt;
	}

	/**
	 * Calculate percent
	 */
	private void calculatePercent() {
		if (bytesUploaded <= 0 || bytesTotal <= 0) {
			percent = 0;
			percentInt = 0;
		} else {
			percent = ((float)bytesUploaded / bytesTotal) * 100;
			percentInt = (int)percent;
		}
	}

	/**
	 * Returns the rate
	 * 
	 * @return rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * Sets the rate
	 * 
	 * @param rate rate
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
}
