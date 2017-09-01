package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import static com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacetHelper.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;

public class OxygenSearchFacetUtil implements ISearchFacetUtil {

	private String skeyParam;

	private String host;

	private ICmsURI cmsURI;

	private JsonFactory jsonFactory = new JsonFactory();

	public OxygenSearchFacetUtil(ICmsURI cmsURI) {
		this.skeyParam = cmsURI.getSessionKeyParam();
		this.host = cmsURI.getHostURI();
		this.cmsURI = cmsURI;

	}

	public Map<String, String> getXmlDocTypes() throws Exception {

		return getSearchTypes("rsuite:xmlDoc");
	}

	private Map<String, String> getSearchTypes(String type) throws Exception {

		Map<String, String> moElements = new LinkedHashMap<String, String>();

		URL url = new URL(host
				+ "/rsuite/rest/v1/api/rsuite.oxygen.search.types?" + skeyParam
				+ "&type" + type);
		
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();

		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();

		JsonParser jp = jsonFactory.createParser(is);

		JsonToken token;

		while ((token = jp.nextToken()) != null) {

			if (token == JsonToken.START_OBJECT) {
				parseXmlTypeObject(jp, moElements);

			}
		}
		jp.close();

		httpConnection.disconnect();

		return moElements;
	}

	private static void parseXmlTypeObject(JsonParser jp,
			Map<String, String> moElements) throws JsonParseException,
			IOException {

		String name = null;
		String namespaceUri = null;

		while (jp.nextToken() != JsonToken.END_OBJECT) {
			jp.nextToken();

			if ("namespaceUri".equals(jp.getCurrentName())) {
				namespaceUri = jp.getValueAsString();
			} else if ("name".equals(jp.getCurrentName())) {
				name = jp.getValueAsString();
			}
		}

		String fullName = name;
		if (StringUtils.isNotBlank(namespaceUri)){
			fullName = namespaceUri + ":" + name;
		}
		
		moElements.put(fullName,name);

	}

	@Override
	public Map<String, String> getMediaTypes() throws Exception {
		throw new Exception("Method not supproted");
	}

	@Override
	public List<ResultItem> performFacetSearch(SearchFacet searchFacet)
			throws Exception {

		String parameters = "";

		for (String element : searchFacet.getElements()) {
			parameters += "&element=" + encode(element);
		}

		String pharse = searchFacet.getText();

		if (StringUtils.isNotBlank(pharse)) {
			parameters += "&phrase=" + encode(pharse);
		}

		URL url = new URL(host
				+ "/rsuite/rest/v1/api/rsuite.oxygen.search.searchXML?"
				+ skeyParam + parameters);
		
		return  SearchFacetHelper.sendSearchRequest(cmsURI, url);
	}



	

	
}
