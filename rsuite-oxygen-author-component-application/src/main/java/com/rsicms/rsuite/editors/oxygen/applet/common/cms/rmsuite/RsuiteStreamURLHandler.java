package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

//TODO review if necessary

public class RsuiteStreamURLHandler extends URLStreamHandler{

	private RSuiteURI uri;
	
	
	
	public RsuiteStreamURLHandler(RSuiteURI uri) {
		super();
		this.uri = uri;
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		
		//String query = url.toString();
		String infoUrl = uri.getHostURI() + "/rsuite/rest/v1/content/";
		
//		if (query.startsWith("rsuite:/res/alias/")){
//			query = "alias/" + query.replace("rsuite:/res/alias/", "");
//		}else if (query.startsWith("rsuite:/res/id/")){
//			query = query.replace("rsuite:/res/id/", "");
//		}
//		
//		
		
//		
//		infoUrl += query;
		
		if (!infoUrl.contains(uri.getSessionKeyParam())){
			infoUrl += "?" + uri.getSessionKeyParam();
		}
		
		URL rsuilteURL = new URL(infoUrl);

		return rsuilteURL.openConnection();
	}
	
}
