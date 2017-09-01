package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import java.net.URL;

import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;


public interface IURLMapper {

	OxygenOpenDocumentParmaters mapURLToOpenParameters(URL urlToMap, ICmsURI cmsURI);
}
