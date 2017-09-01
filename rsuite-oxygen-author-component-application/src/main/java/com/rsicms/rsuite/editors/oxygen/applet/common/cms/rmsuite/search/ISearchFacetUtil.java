package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import java.util.List;
import java.util.Map;

public interface ISearchFacetUtil {

	public abstract Map<String, String> getXmlDocTypes() throws Exception;

	public abstract Map<String, String> getMediaTypes() throws Exception;

	public abstract List<ResultItem> performFacetSearch(SearchFacet searchFacet)
			throws Exception;

}