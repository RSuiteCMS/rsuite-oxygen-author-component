package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.io.FilenameUtils;


public class RsuiteStreamImageURLHandler extends URLStreamHandler{

	private RSuiteURI uri;
	
	
	public RsuiteStreamImageURLHandler(RSuiteURI uri) {
		super();
		this.uri = uri;
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
      String path = url.getPath();
      
      if (!path.startsWith("/rsuite/rest/v2/content/binary/alias")){
          path = "/rsuite/rest/v2/content/binary/alias/" + path;
          path = FilenameUtils.normalize(path);
      }
      
      URL rsuilteURL = new URL(uri.getImagePreviewUriByAlias(path));
      
      return rsuilteURL.openConnection();
	}

	
	
	
}
