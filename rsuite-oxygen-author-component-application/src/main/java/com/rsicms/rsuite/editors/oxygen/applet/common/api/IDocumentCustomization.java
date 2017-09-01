package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;

public interface IDocumentCustomization {

	IDocumentHandler getDocumentHandler();
	
	ILookupFactory getLookupFactory();
	
	IComponentInitializeHandler getIComponentInitializeHandler();
		
}
