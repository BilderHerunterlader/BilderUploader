package ch.supertomcat.bilderuploader.upload;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * File Upload Result
 */
@XmlRootElement(name = "fileUploadResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileUploadResult {
	/**
	 * Prepare Upload Result Texts
	 */
	@XmlElement(name = "prepareUploadResultTexts")
	private List<String> prepareUploadResultTexts;

	/**
	 * Upload Result Texts
	 */
	@XmlElement(name = "uploadResultTexts")
	private List<String> uploadResultTexts;

	/**
	 * Constructor
	 */
	public FileUploadResult() {
	}

	/**
	 * Constructor
	 * 
	 * @param prepareUploadResultTexts Prepare Upload Result Texts
	 * @param uploadResultTexts Upload Result Texts
	 */
	public FileUploadResult(List<String> prepareUploadResultTexts, List<String> uploadResultTexts) {
		this.prepareUploadResultTexts = prepareUploadResultTexts;
		this.uploadResultTexts = uploadResultTexts;
	}

	/**
	 * Returns the prepareUploadResultTexts
	 * 
	 * @return prepareUploadResultTexts
	 */
	public List<String> getPrepareUploadResultTexts() {
		return prepareUploadResultTexts;
	}

	/**
	 * Returns the uploadResultTexts
	 * 
	 * @return uploadResultTexts
	 */
	public List<String> getUploadResultTexts() {
		return uploadResultTexts;
	}
}
