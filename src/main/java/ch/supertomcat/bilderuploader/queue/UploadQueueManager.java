package ch.supertomcat.bilderuploader.queue;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import ch.supertomcat.bilderuploader.settings.BUSettingsListener;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.settingsconfig.ConnectionSettings;
import ch.supertomcat.bilderuploader.upload.FileUploadResult;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.bilderuploader.upload.UploadManager;
import ch.supertomcat.supertomcatutils.queue.QueueManagerBase;
import ch.supertomcat.supertomcatutils.queue.QueueTask;
import ch.supertomcat.supertomcatutils.queue.Restriction;

/**
 * This class manages the upload-slots
 */
public class UploadQueueManager extends QueueManagerBase<UploadFile, FileUploadResult> {
	/**
	 * Listeners
	 */
	private List<UploadQueueManagerListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Queue Manager
	 */
	private final QueueManager queueManager;

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param queueManager Queue Manager
	 * @param settingsManager Settings Manager
	 * @param proxyManager Proxy Manager
	 */
	public UploadQueueManager(QueueManager queueManager, SettingsManager settingsManager, ProxyManager proxyManager) {
		super(new UploadManager(proxyManager, settingsManager), settingsManager.getConnectionSettings().getMaxConnections(), settingsManager.getConnectionSettings().getMaxConnectionsPerHost());
		this.queueManager = queueManager;
		this.settingsManager = settingsManager;

		this.settingsManager.addSettingsListener(new BUSettingsListener() {
			@Override
			public void settingsChanged() {
				ConnectionSettings connectionSettings = settingsManager.getConnectionSettings();
				setMaxConnectionCount(connectionSettings.getMaxConnections());
				setMaxConnectionCountPerHost(connectionSettings.getMaxConnectionsPerHost());
			}

			@Override
			public void lookAndFeelChanged() {
				// Nothing to do
			}
		});

		this.queueManager.addListener(new QueueManagerListener() {
			@Override
			public void increaseSessionUploadedFiles() {
				increaseSessionFiles();
			}

			@Override
			public void increaseSessionUploadedBytes(long size) {
				increaseSessionBytes(size);
			}

			@Override
			public void filesRemoved(int[] removedIndeces) {
				// Nothing to do
			}

			@Override
			public void filesAdded(List<UploadFile> files) {
				// Nothing to do
			}

			@Override
			public void fileStatusChanged(UploadFile file, int index) {
				// Nothing to do
			}

			@Override
			public void fileRemoved(UploadFile file, int index) {
				// Nothing to do
			}

			@Override
			public void fileProgressChanged(UploadFile file, int index) {
				// Nothing to do
			}

			@Override
			public void fileHosterChanged(UploadFile file, int index) {
				// Nothing to do
			}

			@Override
			public void fileDeactivatedChanged(UploadFile file, int index) {
				// Nothing to do
			}

			@Override
			public void fileAdded(UploadFile file) {
				// Nothing to do
			}

			@Override
			public void startUpload(UploadFile file) {
				addTaskToQueue(file);
			}

			@Override
			public void startUpload(List<UploadFile> files) {
				addTasksToQueue(files);
			}

			@Override
			public void stopUpload(boolean cancelAlreadyExecutingTasks) {
				cancelTasks(cancelAlreadyExecutingTasks);
			}
		});

		init();
	}

	/**
	 * Adds a listener
	 * 
	 * @param l Listener
	 */
	public void addDownloadQueueManagerListener(UploadQueueManagerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Removes a listener
	 * 
	 * @param l Listener
	 */
	public void removeDownloadQueueManagerListener(UploadQueueManagerListener l) {
		listeners.remove(l);
	}

	@Override
	public synchronized void increaseSessionFiles() {
		super.increaseSessionFiles();
		for (UploadQueueManagerListener listener : listeners) {
			listener.sessionUploadedFilesChanged(sessionFiles);
		}
	}

	@Override
	public synchronized void increaseSessionBytes(long bytes) {
		super.increaseSessionBytes(bytes);
		for (UploadQueueManagerListener listener : listeners) {
			listener.sessionUploadedBytesChanged(sessionBytes);
		}
	}

	@Override
	protected void updateOpenSlots() {
		super.updateOpenSlots();
		for (UploadQueueManagerListener listener : listeners) {
			listener.queueChanged(queue.size(), openSlots, maxConnectionCount);
		}
	}

	@Override
	protected Restriction getRestrictionForTask(UploadFile task) {
		return new UploadRestriction(task.getHoster());
	}

	@Override
	public void addTaskToQueue(UploadFile task) {
		task.setStatus(UploadFileState.WAITING);
		super.addTaskToQueue(task);
	}

	@Override
	public void addTasksToQueue(List<UploadFile> tasks) {
		for (UploadFile task : tasks) {
			task.setStatus(UploadFileState.WAITING);
		}
		super.addTasksToQueue(tasks);
	}

	@Override
	protected void addTaskToExecutingTasks(QueueTask<UploadFile, FileUploadResult> task) {
		task.getTask().setStatus(UploadFileState.UPLOADING);
		super.addTaskToExecutingTasks(task);
	}

	@Override
	protected int compareTasks(UploadFile t1, UploadFile t2) {
		int comparison = super.compareTasks(t1, t2);
		if (comparison == 0) {
			return Long.compare(t1.getDateTimeAdded(), t2.getDateTimeAdded());
		}
		return comparison;
	}

	@Override
	protected void removedTaskFromQueue(UploadFile task, boolean executeFailure) {
		if (executeFailure) {
			task.setStatus(UploadFileState.FAILED);
			task.increaseFailedCount();
			if (task.getFailedCount() >= settingsManager.getUploadSettings().getMaxFailedCount()) {
				task.setDeactivated(true);
			}
		} else {
			task.setStatus(UploadFileState.SLEEPING);
		}
	}

	@Override
	protected void completedTaskCallable(QueueTask<UploadFile, FileUploadResult> task) {
		try {
			boolean resultAvailable = task.getFuture().get() != null;
			if (!resultAvailable) {
				if (task.getFuture().isCancelled()) {
					task.getTask().setStatus(UploadFileState.SLEEPING);
				} else {
					task.getTask().setStatus(UploadFileState.FAILED);
				}
			}
		} catch (ExecutionException e) {
			logger.error("File upload failed", e);
			task.getTask().setStatus(UploadFileState.FAILED);
		} catch (CancellationException e) {
			logger.info("File upload cancelled");
			task.getTask().setStatus(UploadFileState.SLEEPING);
		} catch (InterruptedException e) {
			logger.error("Interrupt while waiting for result, this should not happen!", e);
			task.getTask().setStatus(UploadFileState.FAILED);
		}
	}
}
