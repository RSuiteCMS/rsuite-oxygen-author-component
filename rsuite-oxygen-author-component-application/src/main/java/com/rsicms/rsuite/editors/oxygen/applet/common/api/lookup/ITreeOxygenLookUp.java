package com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup;

import java.util.List;

public interface ITreeOxygenLookUp {

	List<IReposiotryResource> getRootChildren()  throws Exception;

	List<IReposiotryResource> getChildren(String id)  throws Exception;
	
	String getRootDisplayText();
	
	String getRootIcon();
}

