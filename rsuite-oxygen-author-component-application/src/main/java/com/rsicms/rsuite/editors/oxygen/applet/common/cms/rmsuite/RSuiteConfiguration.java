package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import java.io.IOException;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsConfiguration;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;

public class RSuiteConfiguration implements ICmsConfiguration {

	private ICmsURI rsuiteURI;
	
	public RSuiteConfiguration(OxygenAppletStartupParmaters parameters) throws IOException {
		createCmsURI(parameters);
	}

	private void createCmsURI(OxygenAppletStartupParmaters parameters) {
		String baseUri = parameters.getParameterValue(OxygenAppletStartupParmatersNames.BASE_URI);
		String sessionKey = parameters.getParameterValue(OxygenAppletStartupParmatersNames.SESSION_KEY);
		rsuiteURI = new RSuiteURI(baseUri, sessionKey);
	}

	@Override
	public ICmsURI getCmsUri() {
		return rsuiteURI;
	}

}
