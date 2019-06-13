package ch.supertomcat.bilderuploader.upload;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * Wrapper for HttpEntity to get the progress
 */
public class UploadProgressEntityWrapper extends HttpEntityWrapper {
	/**
	 * Listener
	 */
	private final UploadProgressListener listener;

	/**
	 * Constructor
	 * 
	 * @param entity HttpEntity
	 * @param listener Listener
	 */
	public UploadProgressEntityWrapper(HttpEntity entity, UploadProgressListener listener) {
		super(entity);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new UploadProgressFilterOutputStream(outstream, listener, getContentLength()));
	}
}
