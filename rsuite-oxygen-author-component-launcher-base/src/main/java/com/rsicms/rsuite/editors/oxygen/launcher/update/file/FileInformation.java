package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

public class FileInformation {

	private String fileName;

	private long fileSize;

	private long crc32;

	public FileInformation(String fileName, long fileSize, long crc32) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.crc32 = crc32;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getCrc32() {
		return crc32;
	}

	@Override
	public String toString() {
		StringBuilder objectInformation = new StringBuilder();
		objectInformation.append(fileName).append("|");
		objectInformation.append(fileSize).append("|");
		objectInformation.append(crc32);
		return objectInformation.toString();
	}
}
