package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IComponentInitializeHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.RSuiteLookupFactory;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class RSuiteDefaultSchemaCustomizationFactory implements
		ISchemaAwareCustomizationFactory {


	@Override
	public IDocumentHandler createDocumentHandler(IDocumentURI cmsUri,
			OxygenOpenDocumentParmaters parameters) {
		return null;
	}

	@Override
	public IComponentInitializeHandler createIComponentInitializeHandler(
			IDocumentURI cmsUri, OxygenOpenDocumentParmaters parameters) {
		return new RSuiteComponentInitializer();
	}

	@Override
	public ILookupFactory createLookupFactory(IDocumentURI documentUri,
			OxygenOpenDocumentParmaters parameters) {
		return new RSuiteLookupFactory(documentUri);
	}

	@Override
	public boolean matchSchema(String publicSchemaId) {
		return true;
	}

}
