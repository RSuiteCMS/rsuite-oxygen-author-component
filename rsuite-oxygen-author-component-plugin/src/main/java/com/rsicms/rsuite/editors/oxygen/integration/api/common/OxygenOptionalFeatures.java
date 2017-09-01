package com.rsicms.rsuite.editors.oxygen.integration.api.common;

public enum OxygenOptionalFeatures {

	MATH_ML_MATHFLOW("com.dessci.mathflow");
	
	private String featureId;
	
	private OxygenOptionalFeatures(String featureId){
		this.featureId = featureId;
	}

	public String getFeatureId() {
		return featureId;
	}
		
}
