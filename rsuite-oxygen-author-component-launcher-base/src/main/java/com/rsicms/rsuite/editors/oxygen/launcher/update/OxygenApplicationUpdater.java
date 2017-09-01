package com.rsicms.rsuite.editors.oxygen.launcher.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;


import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.ExceptionUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.FileUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.IOUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileOperation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdate;

public class OxygenApplicationUpdater {

	private static final int BUFFER_SIZE = 4096;

	private volatile long totalBytesToDownload = 0;

	private volatile long bytesDownloaded;

	private List<FileToUpdate> filesToUpdate;

	private OxygenApplicationCmsUpdater cmsUpdater;

	private File oxygenApplicationHomeFolder;

	private boolean hasError = false;

	public OxygenApplicationUpdater(File oxygenApplicationHomeFolder,
			OxygenApplicationCmsUpdater cmsUpdater) {
		super();
		this.oxygenApplicationHomeFolder = oxygenApplicationHomeFolder;
		this.cmsUpdater = cmsUpdater;

	}
	
	private void countBytesToDownload(){
		for (FileToUpdate fileToUpdate : filesToUpdate) {
			totalBytesToDownload += fileToUpdate.getSize();
		}
		
	}

	public void updateOxygenApplication(OxygenApplicationUpdaterProgressHandler progressHandler) throws Exception {
		
		OxygenApplicationUpdateInformation updateInformation = new OxygenApplicationUpdateInformation(
				oxygenApplicationHomeFolder, cmsUpdater);
		filesToUpdate = updateInformation.getFilesToUpdate();
		countBytesToDownload();
		
		for (FileToUpdate fileToUpdate : filesToUpdate) {
			FileOperation operation = fileToUpdate.getFileOperation();
			File file = new File(oxygenApplicationHomeFolder,
					fileToUpdate.getFileName());
			FileUtils.deleteQuietly(file);

			if (FileOperation.DELETE != operation) {
				downloadFile(fileToUpdate, file, progressHandler);
			}
		}
		
	}
	
	private void downloadFile(FileToUpdate fileToUpdate, File file, OxygenApplicationUpdaterProgressHandler progressHandler) {
		
		String url = cmsUpdater.createUrlForFile(fileToUpdate);
		
		try {

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			FileOutputStream outputStream = new FileOutputStream(file);
			InputStream inputStream = HttpUtils.downloadFile(url);

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
				bytesDownloaded += bytesRead;
				if (progressHandler != null){
					progressHandler.updateProgress(bytesDownloaded, totalBytesToDownload);
				}
			}

			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		} catch (Exception e) {
			hasError = true;
			ExceptionUtils.handleException("Unable to download " +  url, e);
		}
	}

	public long getTotalBytesToDownload() {
		return totalBytesToDownload;
	}

	public long getBytesDownloaded() {
		return bytesDownloaded;
	}

	public boolean hasError() {
		return hasError;
	}

}
