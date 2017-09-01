package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

public class RsuiteSearch {

	private String searchId;
	
	private int resultCount;

	public RsuiteSearch(String searchId, int resultCount) {
		super();
		this.searchId = searchId;
		this.resultCount = resultCount;
	}

	public String getSearchId() {
		return searchId;
	}

	public int getResultCount() {
		return resultCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Search id: ");
		sb.append(searchId);
		sb.append("\nresult count: ");
		sb.append(resultCount);
		return sb.toString();
	}
}
