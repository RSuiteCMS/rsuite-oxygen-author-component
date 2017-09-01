package com.rsicms.rsuite.editors.oxygen.applet;

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class HtmlAction {
	
	private static final String JS_PRE = "javascript:"; 
	
	private Logger logger = Logger
			.getLogger(this.getClass());
	
	private AppletContext appletContext;
	
	public HtmlAction() {
	}
	
	public HtmlAction(AppletContext appletContext) {
		this.appletContext = appletContext;
		this.executeJavascript("Oxygen.set('appletReady', true)");
	}
	
	private void executeJavascript(String script) {
		try {
			URL jsCommand = new URL(JS_PRE + "(function () {" + script + "}())");
			appletContext.showDocument(jsCommand);
		} catch (MalformedURLException e) {
			OxygenUtils.handleException(logger, e);
		}
	}
	
	public void setDirty(boolean dirty){
		executeJavascript("Oxygen.set('dirty', " + ( dirty ? "true" : "false" ) + ")");
	}
	
	public void closeWindow(){
		try {
			executeJavascript("Oxygen.close()");
		}catch (Throwable t) {
			logger.error(t, t);
		}
	}
	public void setActiveDocument(String docUri) {
		docUri = StringEscapeUtils.escapeJavaScript(docUri);
		executeJavascript("Oxygen.set('activeDocument', \"" + docUri + "\")");
	}
	public void setActiveDocument(IDocumentURI docUri) {
		setActiveDocument(docUri.getDocumentURI());
	}
}
