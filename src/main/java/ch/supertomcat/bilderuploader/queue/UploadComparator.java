package ch.supertomcat.bilderuploader.queue;

import java.util.Comparator;

import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * This class is for sorting of the upload-queue
 */
public class UploadComparator implements Comparator<UploadFile> {
	/**
	 * Constructor
	 */
	public UploadComparator() {
	}

	@Override
	public int compare(UploadFile file1, UploadFile file2) {
		return Long.compare(file1.getDateTimeAdded(), file2.getDateTimeAdded());
	}
}
