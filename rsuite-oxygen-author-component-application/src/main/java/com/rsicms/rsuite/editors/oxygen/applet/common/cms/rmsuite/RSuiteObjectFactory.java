package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IModifiableCmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.v4.RSuiteActions_v4;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.v4.RSuiteDocumentURI_v4;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class RSuiteObjectFactory {

	public static ICmsActions getRSuiteActions(IModifiableCmsURI cmsURI, RSuiteVersion version) throws OxygenIntegrationException{
		if (version == RSuiteVersion.RSUITE_4){
			return new RSuiteActions_v4(cmsURI);
		}
		
		throw new OxygenIntegrationException("Unsupported version " + version);
	}
	
	
	public static IDocumentURI getRSuiteURI(RSuiteVersion version, ICmsURI cmsURI, OxygenOpenDocumentParmaters documentParameters){
		if (version == RSuiteVersion.RSUITE_4){
			return new RSuiteDocumentURI_v4(cmsURI, documentParameters);
		}
		
		throw new RuntimeException("Unsupported version " + version);
	}
}
