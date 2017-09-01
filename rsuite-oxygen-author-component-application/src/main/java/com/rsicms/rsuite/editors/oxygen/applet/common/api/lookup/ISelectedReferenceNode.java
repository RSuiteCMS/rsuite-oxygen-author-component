package com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;


public interface ISelectedReferenceNode {

	IReposiotryResource getRepositoryResource();

	IReferenceTargetElement getReferenceTargetElement();
	
	boolean isContextNode();
	
	String getLinkValue(InsertReferenceHandler referenceHandler);

}