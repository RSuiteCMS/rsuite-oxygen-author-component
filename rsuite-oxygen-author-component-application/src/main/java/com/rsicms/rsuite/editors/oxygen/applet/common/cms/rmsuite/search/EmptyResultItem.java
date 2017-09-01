package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

public class EmptyResultItem extends ResultItem {

	private static final EmptyResultItem instance = new EmptyResultItem();
	
	public EmptyResultItem() {
		super();
		setDisplayName("No Result");
		setRsuiteId("");
		setKind("");
		setLocalName("");
	}
	
	public static EmptyResultItem getInstance(){
		return instance;
	}
}
