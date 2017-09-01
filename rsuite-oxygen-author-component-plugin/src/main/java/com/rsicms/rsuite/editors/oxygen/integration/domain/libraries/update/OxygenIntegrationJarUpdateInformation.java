package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

public class OxygenIntegrationJarUpdateInformation {

	private String fileName;
	
	private long fileSize;
	
	private OxygenIntegrationJarUpdateOperation operation;

	public OxygenIntegrationJarUpdateInformation(String fileName, long fileSize, OxygenIntegrationJarUpdateOperation operation) {
		this.fileName = fileName;
		this.operation = operation;
		this.fileSize = fileSize;
	}
	
	
	public OxygenIntegrationJarUpdateInformation(String fileName,
			OxygenIntegrationJarUpdateOperation operation) {
		this.fileName = fileName;
		this.operation = operation;
	}


	@Override
	public String toString(){
		StringBuilder value = new StringBuilder(fileName);
		value.append(" | ").append(operation.getOperationCode());
		value.append(" | ");
		if (OxygenIntegrationJarUpdateOperation.DELETE != operation ){
			value.append(fileSize);
		}
		return value.toString();
		
	}
	
}
