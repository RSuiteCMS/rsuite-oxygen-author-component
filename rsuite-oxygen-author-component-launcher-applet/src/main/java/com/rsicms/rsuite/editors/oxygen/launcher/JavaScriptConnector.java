package com.rsicms.rsuite.editors.oxygen.launcher;

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

public class JavaScriptConnector {
	
	private static final String JS_PRE = "javascript:"; 
	private AppletContext appletContext;
	
	public JavaScriptConnector() {
	}
	
	public JavaScriptConnector(AppletContext appletContext) {
		this.appletContext = appletContext;
		this.executeJavascript("Oxygen.set('appletReady', true)");
	}
	
	private void executeJavascript(String script) {
		try {
			URL jsCommand = new URL(JS_PRE + "(function () {" + script + "}())");
			appletContext.showDocument(jsCommand);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeLauncherDialog(){
		try {
			executeJavascript("testFromJS();");
		}catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
