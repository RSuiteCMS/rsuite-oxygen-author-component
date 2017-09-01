package com.rsicms.rsuite.editors.oxygen.integration.api.configuration;


public enum SubMoPolicy {
	TOP_MO, PARENT_TOPIC_MO;
	
	public static SubMoPolicy fromString(String value){
		if ("parentTopicMo".equalsIgnoreCase(value)){
			return SubMoPolicy.PARENT_TOPIC_MO;
		}
		
		return SubMoPolicy.TOP_MO;
	}
}
