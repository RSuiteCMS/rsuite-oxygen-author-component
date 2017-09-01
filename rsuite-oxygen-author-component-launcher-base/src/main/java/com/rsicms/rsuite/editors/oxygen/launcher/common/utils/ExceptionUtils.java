package com.rsicms.rsuite.editors.oxygen.launcher.common.utils;


public class ExceptionUtils {

	public static void handleException(String message, Exception e) {
		e.printStackTrace();
	}
	
	public static void handleException(Exception e) {		
		e.printStackTrace();
	}
}
