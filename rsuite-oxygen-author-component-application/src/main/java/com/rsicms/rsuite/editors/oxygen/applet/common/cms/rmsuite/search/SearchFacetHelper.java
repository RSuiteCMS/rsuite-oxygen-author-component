package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;

class SearchFacetHelper {

	static List<ResultItem> sendSearchRequest(ICmsURI cmsURI, URL url)
			throws IOException, JsonParseException {
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();

		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();

		JSonSearchResultParser resultParser = new JSonSearchResultParser(cmsURI);
		List<ResultItem> results = resultParser.parseResultItems(is);

		httpConnection.disconnect();

		return results;
	}
	
	static String encode(String toEncode) throws IOException {
		return URLEncoder.encode(toEncode, "utf-8");
	}
}
