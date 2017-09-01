package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

public class OxygenIntegrationLocalJarInformation {

	private String fileName;
	
	private long fileSize;
	
	private long crc;
	
	public OxygenIntegrationLocalJarInformation(String[] localFileInformation){
		fileName = localFileInformation[0].trim();
		fileSize =  Long.valueOf(localFileInformation[1].trim());
		crc = Long.valueOf(localFileInformation[2].trim());
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getCrc() {
		return crc;
	}
	
}
