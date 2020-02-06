package ch.supertomcat.bilderuploader.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.supertomcat.bilderuploader.hosterconfig.Hoster;

/**
 * Container class, which represents a file
 */
public class UploadFile {
	/**
	 * Database ID
	 */
	private int id = 0;

	/**
	 * File
	 */
	private File file;

	/**
	 * Hoster
	 */
	private Hoster hoster;

	/**
	 * File Size
	 */
	private long size;

	/**
	 * DateTime of Add
	 */
	private long dateTimeAdded;

	/**
	 * Mime Type
	 */
	private String mimeType;

	/**
	 * Status of the upload
	 */
	private UploadFileState status = UploadFileState.SLEEPING;

	/**
	 * Error-Message
	 */
	private String errMsg = "";

	/**
	 * Count of failures
	 */
	private int failedCount = 0;

	/**
	 * Download deactivated
	 */
	private boolean deactivated = false;

	/**
	 * File Upload Result
	 */
	private FileUploadResult fileUploadResult;

	/**
	 * Progress
	 */
	private UploadFileProgress progress = new UploadFileProgress();

	/**
	 * Listener
	 */
	private transient List<UploadFileListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param file File
	 * @param hoster Hoster
	 */
	public UploadFile(File file, Hoster hoster) {
		this.file = file;
		this.hoster = hoster;
		this.size = file.length();
		this.dateTimeAdded = System.currentTimeMillis();
		try {
			this.mimeType = Files.probeContentType(file.toPath());
			if (this.mimeType == null) {
				this.mimeType = "application/octet-stream";
			}
		} catch (IOException e) {
			this.mimeType = "application/octet-stream";
		}
	}

	/**
	 * Constructor
	 * 
	 * @param id Database ID
	 * @param file File
	 * @param hoster Hoster
	 * @param size File Size
	 * @param dateTimeAdded Added DateTime
	 * @param mimeType Mime Type
	 * @param status Status
	 * @param errMsg Error Message
	 * @param failedCount Failed Count
	 * @param deactivated Deactivated
	 * @param fileUploadResult File Upload Result
	 */
	public UploadFile(int id, File file, Hoster hoster, long size, long dateTimeAdded, String mimeType, UploadFileState status, String errMsg, int failedCount, boolean deactivated,
			FileUploadResult fileUploadResult) {
		this.id = id;
		this.file = file;
		this.hoster = hoster;
		this.size = size;
		this.dateTimeAdded = dateTimeAdded;
		this.mimeType = mimeType;
		this.status = status;
		this.errMsg = errMsg;
		this.failedCount = failedCount;
		this.deactivated = deactivated;
		this.fileUploadResult = fileUploadResult;
	}

	/**
	 * Returns the filesize
	 * 
	 * @return Filesize
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * Sets the filesize
	 * 
	 * @param size Filesize
	 */
	public void setSize(long size) {
		if (size >= 0) {
			this.size = size;
		}
	}

	/**
	 * @return Statustext
	 */
	public String getStatusText() {
		return status.getText();
	}

	/**
	 * Returns the status
	 * 
	 * @return Status
	 */
	public UploadFileState getStatus() {
		return status;
	}

	/**
	 * @param status Status
	 */
	public void setStatus(UploadFileState status) {
		setStatus(status, "");
	}

	/**
	 * @param status Status
	 * @param errMsg Error-Message
	 */
	public void setStatus(UploadFileState status, String errMsg) {
		this.status = status;
		this.errMsg = errMsg;
		for (UploadFileListener listener : listeners) {
			listener.statusChanged(this);
		}
	}

	/**
	 * Returns the count of failures
	 * 
	 * @return Count of failures
	 */
	public int getFailedCount() {
		return failedCount;
	}

	/**
	 * Increases the failedCount
	 */
	public void increaseFailedCount() {
		failedCount++;
	}

	/**
	 * Returns if the download is deactivated or not
	 * 
	 * @return Deactivated
	 */
	public boolean isDeactivated() {
		return deactivated;
	}

	/**
	 * Sets deactivated
	 * 
	 * @param deactivated Deactivated
	 */
	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
		if (!deactivated) {
			this.failedCount = 0;
		}
		for (UploadFileListener listener : listeners) {
			listener.deactivatedChanged(this);
		}
	}

	/**
	 * Returns the Error-Message
	 * 
	 * @return Error-Message
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * Returns the id
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the file
	 * 
	 * @return file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the mimeType
	 * 
	 * @return mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns the hoster
	 * 
	 * @return hoster
	 */
	public Hoster getHoster() {
		return hoster;
	}

	/**
	 * Sets the hoster
	 * 
	 * @param hoster hoster
	 */
	public void setHoster(Hoster hoster) {
		this.hoster = hoster;
		for (UploadFileListener listener : listeners) {
			listener.hosterChanged(this);
		}
	}

	/**
	 * Returns the fileUploadResult
	 * 
	 * @return fileUploadResult
	 */
	public FileUploadResult getFileUploadResult() {
		return fileUploadResult;
	}

	/**
	 * Sets the fileUploadResult
	 * 
	 * @param fileUploadResult fileUploadResult
	 */
	public void setFileUploadResult(FileUploadResult fileUploadResult) {
		this.fileUploadResult = fileUploadResult;
		for (UploadFileListener listener : listeners) {
			listener.resultChanged(this);
		}
	}

	/**
	 * Returns the dateTimeAdded
	 * 
	 * @return dateTimeAdded
	 */
	public long getDateTimeAdded() {
		return dateTimeAdded;
	}

	/**
	 * Returns the progress
	 * 
	 * @return progress
	 */
	public UploadFileProgress getProgress() {
		return progress;
	}

	/**
	 * Progress Updated
	 */
	public void progressUpdated() {
		for (UploadFileListener listener : listeners) {
			listener.progressChanged(this);
		}
	}

	/**
	 * Adds a listener
	 * 
	 * @param listener Listener
	 */
	public void addListener(UploadFileListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a listener
	 * 
	 * @param listener Listener
	 */
	public void removeListener(UploadFileListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes all listeners
	 */
	public void removeAllListener() {
		listeners.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UploadFile other = (UploadFile)obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
}
