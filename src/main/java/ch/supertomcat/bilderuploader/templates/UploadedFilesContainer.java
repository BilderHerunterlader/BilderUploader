package ch.supertomcat.bilderuploader.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * Container for uploaded files for rendering templates
 * 
 * Note: The methods in this class are used by templates and there will be no references found in Eclipse. Do not remove any of those methods.
 */
public class UploadedFilesContainer {
	private final List<UploadFile> files;

	/**
	 * Constructor
	 * 
	 * @param files
	 */
	public UploadedFilesContainer(List<UploadFile> files) {
		this.files = files;
	}

	/**
	 * Returns the files
	 * 
	 * @return files
	 */
	public List<UploadFile> getFiles() {
		return files;
	}

	/**
	 * Returns the uploadResultTexts grouped by count
	 * 
	 * @param count Count
	 * @param filesToGroup Files
	 * @return uploadResultTexts
	 */
	public List<List<UploadFile>> createFileGroups(int count, List<UploadFile> filesToGroup) {
		List<List<UploadFile>> groupedLists = new ArrayList<>();
		int size = filesToGroup.size();
		int groups = size / count;
		int mod = size % count;
		if (mod != 0) {
			groups++;
		}

		for (int i = 0; i < groups; i++) {
			int endIndex;
			if (mod != 0 && i == groups - 1) {
				endIndex = i + mod;
			} else {
				endIndex = i + count;
			}
			groupedLists.add(filesToGroup.subList(i, endIndex));
		}

		return groupedLists;
	}

	/**
	 * @return Folder Names
	 */
	public List<String> getFolderNames() {
		return files.stream().map(x -> x.getFile().getAbsoluteFile().getParentFile().getName()).distinct().collect(Collectors.toList());
	}

	/**
	 * @return First Folder Name
	 */
	public String getFirstFolderName() {
		return files.stream().map(x -> x.getFile().getAbsoluteFile().getParentFile().getName()).findFirst().orElse("");
	}

	/**
	 * @param filesToGroup Files
	 * @return Files grouped by folder
	 */
	public Map<String, List<UploadFile>> groupFilesByFolder(List<UploadFile> filesToGroup) {
		return filesToGroup.stream().collect(Collectors.groupingBy(x -> x.getFile().getAbsoluteFile().getParentFile().getName()));
	}
}
