package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;

public class JSonSearchResultParser {

	private JsonFactory jsonFactory = new JsonFactory();
	
	private ICmsURI cmsURI;
	
	public JSonSearchResultParser(ICmsURI cmsURI) {
		this.cmsURI = cmsURI;
	}

	List<ResultItem> parseResultItems(InputStream is) throws IOException,
			JsonParseException {
		List<ResultItem> results = new ArrayList<ResultItem>();

		JsonParser jp = jsonFactory.createParser(is);

		JsonToken token;

		while ((token = jp.nextToken()) != null) {

			if (token == JsonToken.START_OBJECT) {
				results.add(parseResultItemObject(jp));
			}
		}
		jp.close();
		return results;
	}

	private ResultItem parseResultItemObject(JsonParser jp)
			throws JsonParseException, IOException {

		ResultItem resultItem = new ResultItem();

		JsonToken nextToken = jp.nextToken();
		
		while (nextToken != JsonToken.END_OBJECT && nextToken != null) {
			nextToken = jp.nextToken();

			String currentName = jp.getCurrentName();

			if ("dateCreated".equals(currentName)) {
				resultItem.setDateCreated(jp.getValueAsString());
			} else if ("id".equals(currentName)) {
				resultItem.setRsuiteId(jp.getValueAsString());
			} else if ("dateModified".equals(currentName)) {
				resultItem.setDateModified(jp.getValueAsString());
			} else if ("displayName".equals(currentName)) {
				resultItem.setDisplayName(jp.getValueAsString());
			}

			else if ("localName".equals(currentName)) {
				String value = jp.getValueAsString();
				resultItem.setLocalName(value);
				resultItem.setKind(value);
			} else if ("parentId".equals(currentName)) {
				resultItem.setParentId(jp.getValueAsString(null));
			} else if ("xml".equals(currentName)) {
				resultItem.setXml(jp.getValueAsBoolean());
			} else if ("aliases".equals(currentName)) {

				Map<String, String> aliases = new HashMap<String, String>();
				parseAliases(jp, aliases);
				resultItem.setAliases(aliases);
			}

		}
		
		resultItem.setCmsURI(cmsURI);

		return resultItem;

	}
	
	private static void parseAliases(JsonParser jp, Map<String, String> aliases)
			throws IOException, JsonParseException {
		JsonToken nextToken = jp.nextToken();

		if (nextToken == JsonToken.START_OBJECT) {

			while ((nextToken = jp.nextToken()) != JsonToken.END_OBJECT) {
				aliases.put(jp.getCurrentName(), jp.getValueAsString());			
			}

		}
	}
}
