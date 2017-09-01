package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

public enum OxygenIntegrationJarUpdateOperation {

	ADD("a"), DELETE("d"), UPDATE("u");
	
	private String operationCode;
	
	OxygenIntegrationJarUpdateOperation(String operationCode){
		this.operationCode = operationCode;
	}

	public String getOperationCode() {
		return operationCode;
	}
	
	
}
