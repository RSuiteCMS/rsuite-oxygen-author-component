package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import org.apache.commons.lang.StringUtils;

public class UriUtils {

	public static String addParameterToUri(String uri, String parameter){
		
		if (StringUtils.isBlank(parameter)){
			return uri;
		}
		
		StringBuilder sb = new StringBuilder(uri);
		
		if (uri.indexOf('?') > -1) {
			sb.append("&");
		} else {
			sb.append("?");
		}
		
		sb.append(parameter);
		
		return sb.toString();
	}
}
