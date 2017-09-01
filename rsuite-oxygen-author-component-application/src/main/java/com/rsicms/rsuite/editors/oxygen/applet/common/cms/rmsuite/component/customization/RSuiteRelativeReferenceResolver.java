package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.component.customization;

import java.net.URL;

import ro.sync.exml.workspace.api.util.RelativeReferenceResolver;

public class RSuiteRelativeReferenceResolver implements RelativeReferenceResolver {

	public String makeRelative(URL baseURL, URL childURL) {

		String relativeURL = childURL.toString();
		int indexOf = relativeURL.indexOf("/rsuite/rest/v2/content/");
		if (indexOf > -1) {
			relativeURL = relativeURL.substring(indexOf);
		}

		return relativeURL;
	}

}
