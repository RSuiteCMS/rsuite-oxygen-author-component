package com.rsicms.rsuite.editors.oxygen.launcher.update;

import static com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileHash.checksumBufferedInputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileInformation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileOperation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdate;

public class OxygenApplicationUpdateInformation {

	private File oxygenApplicationHomeFolder;

	private OxygenApplicationCmsUpdater applicationCmsUpdater;

	public OxygenApplicationUpdateInformation(File oxygenApplicationHomeFolder,
			OxygenApplicationCmsUpdater cmsUpdater) {
		this.oxygenApplicationHomeFolder = oxygenApplicationHomeFolder;
		this.applicationCmsUpdater = cmsUpdater;
	}

	public List<FileToUpdate> getFilesToUpdate() throws Exception {

		List<FileInformation> localFiles = getOxygenApplicationJars();
		return getFilesToUpdateFromCms(localFiles);
	}

	private List<FileToUpdate> getFilesToUpdateFromCms(
			List<FileInformation> localFiles) throws Exception {
		List<FileToUpdate> filesToUpdate = new ArrayList<>();

		List<FileToUpdate> initialFileToUpdate = sendRequestToCMS(localFiles);

		for (FileToUpdate fileToUpdate : initialFileToUpdate) {

			if (fileToUpdate.getFileOperation() == FileOperation.UPDATE) {
				checkFileBasedOnDetailHashCode(filesToUpdate, fileToUpdate);
			} else {
				filesToUpdate.add(fileToUpdate);
			}
		}

		return filesToUpdate;
	}

	private List<FileToUpdate> sendRequestToCMS(List<FileInformation> localFiles)
			throws OxygenApplicationException {
		return applicationCmsUpdater.obtainFilesToUpdate(localFiles);
	}

	private void checkFileBasedOnDetailHashCode(
			List<FileToUpdate> filesToUpdate, FileToUpdate fileToUpdate)
			throws Exception {

		filesToUpdate.add(fileToUpdate);
	}

	private List<FileInformation> getOxygenApplicationJars() throws IOException {

		List<FileInformation> localFiles = new ArrayList<>();

		for (File file : oxygenApplicationHomeFolder.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}

			long fileSize = file.length();

			long crc32ForFile = -1;

			if (!OxygenIntegrationJarNameUtils.hasJarVersion(file
					.getAbsolutePath())) {
				crc32ForFile = checksumBufferedInputStream(file
						.getAbsolutePath());
			}

			localFiles.add(new FileInformation(file.getName(), fileSize,
					crc32ForFile));
		}

		return localFiles;
	}
}
