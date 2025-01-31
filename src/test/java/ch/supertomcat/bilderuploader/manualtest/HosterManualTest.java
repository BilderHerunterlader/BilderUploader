package ch.supertomcat.bilderuploader.manualtest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.hoster.HosterSettingsDialog;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settingsconfig.HosterSettings;
import ch.supertomcat.bilderuploader.test.DummySettingsManager;
import ch.supertomcat.bilderuploader.upload.FileUploadResult;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.bilderuploader.upload.UploadManager;
import ch.supertomcat.bilderuploader.upload.UploadProgressListener;
import ch.supertomcat.supertomcatutils.gui.dialog.FileDialogUtil;
import jakarta.xml.bind.JAXBException;

/**
 * Hoster Test
 */
@SuppressWarnings("javadoc")
public class HosterManualTest {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testImageBamFamily() throws IOException, JAXBException {
		DummySettingsManager settingsManager = new DummySettingsManager();
		ProxyManager proxyManager = new ProxyManager(settingsManager);
		UploadManager uploadManager = new UploadManager(proxyManager, settingsManager);
		HosterManager hosterManager = new HosterManager();
		Hoster hoster = hosterManager.getHostByName("ImageBam-Family");
		assertNotNull(hoster);

		File file = FileDialogUtil.showFileOpenDialog(null, (File)null, null);
		if (file == null) {
			return;
		}

		FileUploadResult fileUploadResult = uploadManager.uploadFile(hoster, file, file.length(), new UploadProgressListener() {

			@Override
			public void progress(long bytesTotal, long bytesCompleted, float percent, double rate) {
				logger.info("Upload Percent: {}", percent);
			}

			@Override
			public void statusChanged(UploadFileState state) {
				// Nothing to do
			}

			@Override
			public void complete(long bytesTotal, long bytesCompleted, long duration) {
				// Nothing to do
			}
		});

		logger.info("Upload Result: {}", fileUploadResult.getUploadResultTexts());
	}

	@Test
	public void testImageBamAccount() throws IOException, JAXBException {
		HosterManager hosterManager = new HosterManager();
		Hoster hoster = hosterManager.getHostByName("ImageBam-Account");
		assertNotNull(hoster);

		HosterSettings hosterSettings = new HosterSettings();
		hosterSettings.setName("ImageBam-Account");

		DummySettingsManager settingsManager = new DummySettingsManager() {

			@Override
			public synchronized HosterSettings getHosterSettings(String hosterName) {
				return hosterSettings;
			}
		};

		if (!HosterSettingsDialog.showSettingsDialogAndSave(hoster, settingsManager)) {
			return;
		}

		ProxyManager proxyManager = new ProxyManager(settingsManager);
		UploadManager uploadManager = new UploadManager(proxyManager, settingsManager);

		File file = FileDialogUtil.showFileOpenDialog(null, (File)null, null);
		if (file == null) {
			return;
		}

		FileUploadResult fileUploadResult = uploadManager.uploadFile(hoster, file, file.length(), new UploadProgressListener() {

			@Override
			public void progress(long bytesTotal, long bytesCompleted, float percent, double rate) {
				logger.info("Upload Percent: {}", percent);
			}

			@Override
			public void statusChanged(UploadFileState state) {
				// Nothing to do
			}

			@Override
			public void complete(long bytesTotal, long bytesCompleted, long duration) {
				// Nothing to do
			}
		});

		logger.info("Upload Result: {}", fileUploadResult.getUploadResultTexts());
	}
}
