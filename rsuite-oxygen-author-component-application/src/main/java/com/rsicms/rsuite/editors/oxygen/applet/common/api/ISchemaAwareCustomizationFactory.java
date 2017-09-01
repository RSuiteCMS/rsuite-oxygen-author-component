package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public interface ISchemaAwareCustomizationFactory {

	IDocumentHandler createDocumentHandler(IDocumentURI cmsUri, OxygenOpenDocumentParmaters parameters);
	
	IComponentInitializeHandler createIComponentInitializeHandler(IDocumentURI cmsUri, OxygenOpenDocumentParmaters parameters);
	
	ILookupFactory createLookupFactory(IDocumentURI documentUri, OxygenOpenDocumentParmaters parameters);
	
	boolean matchSchema(String publicSchemaId);
	
}
	