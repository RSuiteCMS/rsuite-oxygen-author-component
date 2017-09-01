package com.rsicms.rsuite.editors.oxygen.integration.search;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResult;

public class SearchSerlizer {

	public static String serializeXmlTypesToJSon(List<SearchXMLType> xmlTypes)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper().setVisibility(
				PropertyAccessor.GETTER, Visibility.ANY);

		return mapper.writeValueAsString(xmlTypes);
	}
	
	public static String serializeMOsToJSon(Collection<MoResult> xmlTypes)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
		
		return mapper.writeValueAsString(xmlTypes);
	}
}
