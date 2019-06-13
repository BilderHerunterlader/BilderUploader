package ch.supertomcat.bilderuploader.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;

/**
 * FilterOutputStream which calculates the progress
 */
public class UploadProgressFilterOutputStream extends FilterOutputStream {
	private static final int PROGRESS_UPDATE_SIZE = 102400;

	/**
	 * Listener
	 */
	private final UploadProgressListener listener;

	/**
	 * Total Size or -1 if unknown
	 */
	private final long totalSize;

	/**
	 * Flag if total size is known
	 */
	private final boolean totalSizeKnown;

	/**
	 * Completed Size
	 */
	private long completedSize = 0;

	/**
	 * Completed Size since last update
	 */
	private long completedSizeSinceLastUpdate = 0;

	/**
	 * Progress Update Count
	 */
	private int progressUpdateCount = 1;

	/**
	 * timeStarted
	 */
	private long overallTimeStarted = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);

	/**
	 * timeStarted
	 */
	private long timeStarted = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);

	/**
	 * Constructor
	 * 
	 * @param out OutputStream
	 * @param listener Listener
	 * @param totalSize Total size of the upload
	 */
	public UploadProgressFilterOutputStream(OutputStream out, UploadProgressListener listener, long totalSize) {
		super(out);
		this.listener = listener;
		this.totalSize = totalSize;
		this.totalSizeKnown = this.totalSize > 0;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		completedSize += len;
		completedSizeSinceLastUpdate += len;
		if (completedSize > progressUpdateCount * PROGRESS_UPDATE_SIZE || (totalSizeKnown && completedSize >= totalSize)) {
			long now = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			listener.progress(totalSize, completedSize, calculatePercent(), UnitFormatUtil.getBitrate(completedSizeSinceLastUpdate, totalSize, timeStarted, now));

			progressUpdateCount++;
			completedSizeSinceLastUpdate = 0;
			timeStarted = now;
		}
		if (totalSizeKnown && completedSize >= totalSize) {
			long now = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			listener.complete(totalSize, completedSize, now - overallTimeStarted);
		}
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
		completedSize++;
		completedSizeSinceLastUpdate++;
		if (totalSizeKnown && completedSize >= totalSize) {
			long now = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			listener.complete(totalSize, completedSize, now - overallTimeStarted);
		}
	}

	/**
	 * Calculate percent
	 * 
	 * @return Calculated percent
	 */
	private float calculatePercent() {
		if (completedSize <= 0) {
			return 0;
		}
		return ((float)completedSize / totalSize) * 100;
	}
}
