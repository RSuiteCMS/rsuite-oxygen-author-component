package com.rsicms.rsuite.editors.oxygen.applet.common.utils.http;

public class HttpResponse {

	private int code;
	
	private String responseText;

	public HttpResponse(int code, String responseText) {
		super();
		this.code = code;
		this.responseText = responseText;
	}

	public int getCode() {
		return code;
	}

	public String getResponseText() {
		return responseText;
	}	
	
}
