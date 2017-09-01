package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

public enum FileOperation {

	ADD, DELETE, UPDATE;
	
	public static FileOperation fromStringValue(String value) {
		
		if ("d".equalsIgnoreCase(value)){
			return DELETE;
		}else if ("a".equals(value)){
			return ADD;
		}else if ("u".equals(value)){
			return UPDATE;
		}

		throw new IllegalArgumentException("Unable to create enum from + " + value);
	}
}
