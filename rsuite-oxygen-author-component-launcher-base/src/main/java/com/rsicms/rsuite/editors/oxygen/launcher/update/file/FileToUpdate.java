package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

public class FileToUpdate {

	private String fileName;
	
	private long size;

	private FileOperation fileOperation;
	
	private String url;

	public FileToUpdate(String url, long size, FileOperation fileOperation) {
		this.fileName = extractFileNameFromUrl(url);
		this.size = size;
		this.fileOperation = fileOperation;
		this.url = url;
	}

	private String extractFileNameFromUrl(String url) {
		int questionMarkIndex = url.indexOf("?");
		String fileName = url;
		if (questionMarkIndex > -1){
			fileName = fileName.substring(0, questionMarkIndex);
		}
		
		int slashLastIndex = fileName.lastIndexOf("/");
		if (slashLastIndex > -1){
			fileName = fileName.substring(slashLastIndex + 1);
		}
		
		return fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getSize() {
		return size;
	}

	public FileOperation getFileOperation() {
		return fileOperation;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return fileName + " | " + size + " | " + fileOperation;
	}
	
}
