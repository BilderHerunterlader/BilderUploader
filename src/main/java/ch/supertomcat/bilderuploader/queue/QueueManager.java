package ch.supertomcat.bilderuploader.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.database.sqlite.UploadQueueSQLiteDB;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.upload.UploadFileListener;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import jakarta.xml.bind.JAXBException;

/**
 * Queue Manager
 */
public class QueueManager implements UploadFileListener {
	/**
	 * Logger for this class
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Synchronization Object for files
	 */
	private final Object syncObject = new Object();

	/**
	 * Queue
	 */
	private List<UploadFile> files = new ArrayList<>();

	/**
	 * listeners
	 */
	private List<QueueManagerListener> listeners = new CopyOnWriteArrayList<>();

	private boolean uploadsStopped = true;

	private UploadQueueSQLiteDB queueSQLiteDB;

	private final SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 * @param hosterManager Hoster Manager
	 * @throws JAXBException
	 */
	public QueueManager(SettingsManager settingsManager, HosterManager hosterManager) throws JAXBException {
		this.settingsManager = settingsManager;
		queueSQLiteDB = new UploadQueueSQLiteDB(ApplicationProperties.getProperty("DatabasePath") + "/BU-Uploads.sqlite", settingsManager, hosterManager);

		List<UploadFile> filesFromDB = queueSQLiteDB.getAllEntries();
		for (UploadFile fileFromDB : filesFromDB) {
			UploadFileState status = fileFromDB.getStatus();

			if (status == UploadFileState.WAITING || status == UploadFileState.UPLOADING || status == UploadFileState.ABORTING) {
				fileFromDB.setStatus(UploadFileState.SLEEPING);
			}

			fileFromDB.removeAllListener();
			fileFromDB.addListener(this);
			files.add(fileFromDB);
		}
	}

	/**
	 * Saves and closes the database
	 */
	public void closeDatabase() {
		logger.info("Closing Queue Database");
		queueSQLiteDB.closeAllDatabaseConnections();
	}

	/**
	 * Returns an array containing the Queue
	 * 
	 * @return Queue-Array
	 */
	public List<UploadFile> getQueue() {
		synchronized (syncObject) {
			return new ArrayList<>(files);
		}
	}

	/**
	 * @return Queue-Size
	 */
	public int getQueueSize() {
		synchronized (syncObject) {
			return files.size();
		}
	}

	/**
	 * @param file File
	 * @return Index
	 */
	public int indexOfFile(UploadFile file) {
		synchronized (syncObject) {
			return files.indexOf(file);
		}
	}

	/**
	 * @param index Index
	 * @return File
	 */
	public UploadFile getFileByIndex(int index) {
		synchronized (syncObject) {
			if (index < 0 || index >= files.size()) {
				return null;
			}
			return files.get(index);
		}
	}

	/**
	 * @param file File
	 */
	public void addFile(UploadFile file) {
		synchronized (syncObject) {
			if (files.contains(file)) {
				return;
			}
			files.add(file);
			queueSQLiteDB.insertEntry(file);
			file.removeAllListener();
			file.addListener(this);
			for (QueueManagerListener l : listeners) {
				l.fileAdded(file);
			}
		}
		if (settingsManager.getUploadSettings().isAutoStartUploads()) {
			for (QueueManagerListener l : listeners) {
				l.startUpload(file);
			}
		}
	}

	/**
	 * @param fileList File List
	 */
	public void addFiles(List<UploadFile> fileList) {
		List<UploadFile> filesAdded = new ArrayList<>();
		synchronized (syncObject) {
			for (UploadFile file : fileList) {
				if (!(files.contains(file))) {
					files.add(file);
					filesAdded.add(file);
					queueSQLiteDB.insertEntry(file);
					file.removeAllListener();
					file.addListener(this);
				}
			}
			for (QueueManagerListener l : listeners) {
				l.filesAdded(filesAdded);
			}
		}
		if (settingsManager.getUploadSettings().isAutoStartUploads()) {
			for (QueueManagerListener l : listeners) {
				l.startUpload(filesAdded);
			}
		}
	}

	/**
	 * Updates the given file
	 * 
	 * @param file File
	 */
	public void updateFile(UploadFile file) {
		queueSQLiteDB.updateEntry(file);
	}

	/**
	 * Updates the given files
	 * 
	 * @param files Files
	 */
	public void updateFiles(List<UploadFile> files) {
		queueSQLiteDB.updateEntries(files);
	}

	/**
	 * @param file File
	 */
	public void removeFile(UploadFile file) {
		if (file.getStatus() == UploadFileState.WAITING || file.getStatus() == UploadFileState.UPLOADING || file.getStatus() == UploadFileState.ABORTING) {
			return;
		}

		synchronized (syncObject) {
			int index = files.indexOf(file);
			if (index >= 0) {
				files.remove(file);
				file.removeAllListener();
				queueSQLiteDB.deleteEntry(file);
				for (QueueManagerListener l : listeners) {
					l.fileRemoved(file, index);
				}
			}
		}
	}

	/**
	 * Removes files from the queue based on indices
	 * If there are uploads running this method will not do anything!
	 * 
	 * @param indices Indices
	 */
	public void removeFiles(int indices[]) {
		synchronized (syncObject) {
			for (int i = indices.length - 1; i > -1; i--) {
				if ((indices[i] < 0) || (indices[i] >= files.size())) {
					continue;
				}
				UploadFile file = files.get(indices[i]);
				file.removeAllListener();
				queueSQLiteDB.deleteEntry(file);
				files.remove(indices[i]);
			}

			for (QueueManagerListener l : listeners) {
				l.filesRemoved(indices);
			}
		}
	}

	/**
	 * Start uploads
	 */
	public void startUpload() {
		List<UploadFile> list;
		uploadsStopped = false;
		synchronized (syncObject) {
			list = new ArrayList<>(files.stream().filter(x -> !x.isDeactivated() && (x.getStatus() == UploadFileState.SLEEPING || x.getStatus() == UploadFileState.FAILED))
					.collect(Collectors.toList()));
		}
		for (QueueManagerListener l : listeners) {
			l.startUpload(list);
		}
	}

	/**
	 * Stop uploads
	 */
	public void stopUpload() {
		uploadsStopped = true;
		for (QueueManagerListener l : listeners) {
			l.stopUpload(false);
		}
	}

	/**
	 * Returns the downloadsStopped
	 * 
	 * @return downloadsStopped
	 */
	public boolean isUploadsStopped() {
		return uploadsStopped;
	}

	/**
	 * @param l Listener
	 */
	public void addListener(QueueManagerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * @param l Listener
	 */
	public void removeListener(QueueManagerListener l) {
		listeners.remove(l);
	}

	/**
	 * Returns the Synchronization Object for QueueMananger.files
	 * 
	 * @return Synchronization Object for QueueMananger.files
	 */
	public Object getSyncObject() {
		return syncObject;
	}

	@Override
	public void progressChanged(UploadFile file) {
		synchronized (syncObject) {
			int index = files.indexOf(file);
			for (QueueManagerListener l : listeners) {
				l.fileProgressChanged(file, index);
			}
		}
	}

	@Override
	public void hosterChanged(UploadFile file) {
		synchronized (syncObject) {
			int index = files.indexOf(file);
			updateFile(file);
			for (QueueManagerListener l : listeners) {
				l.fileHosterChanged(file, index);
			}
		}
	}

	@Override
	public void statusChanged(UploadFile file) {
		synchronized (syncObject) {
			int index = files.indexOf(file);
			updateFile(file);
			for (QueueManagerListener l : listeners) {
				l.fileStatusChanged(file, index);
			}
		}
		if (file.getStatus() == UploadFileState.COMPLETE) {
			if (settingsManager.getUploadSettings().isSaveLogs()) {
				// TODO LogManager
				// LogManager.instance().addPicToLog(file);
			}

			settingsManager.increaseOverallUploadedFiles(1);
			settingsManager.increaseOverallUploadedBytes(file.getSize());
			for (QueueManagerListener l : listeners) {
				l.increaseSessionUploadedBytes(file.getSize());
				l.increaseSessionUploadedFiles();
			}
			settingsManager.writeSettings(true);
		}
	}

	@Override
	public void deactivatedChanged(UploadFile file) {
		synchronized (syncObject) {
			int index = files.indexOf(file);
			updateFile(file);
			for (QueueManagerListener l : listeners) {
				l.fileDeactivatedChanged(file, index);
			}
		}
	}

	@Override
	public void resultChanged(UploadFile file) {
		synchronized (syncObject) {
			updateFile(file);
		}
	}
}
