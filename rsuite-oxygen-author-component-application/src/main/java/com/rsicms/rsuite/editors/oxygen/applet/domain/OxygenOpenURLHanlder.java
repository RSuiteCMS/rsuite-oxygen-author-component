package com.rsicms.rsuite.editors.oxygen.applet.domain;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IURLMapper;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

import ro.sync.ecss.extensions.api.component.listeners.OpenURLHandler;

public class OxygenOpenURLHanlder extends OpenURLHandler {

	private Logger logger = Logger.getLogger(getClass());

	private OxygenMainComponent mainComponent;

	final ICustomizationFactory customizationFactory;

	final ICmsURI cmsUri;

	public OxygenOpenURLHanlder(OxygenMainComponent mainComponent,
			ICustomizationFactory customizationFactory) {
		super();
		this.mainComponent = mainComponent;
		this.customizationFactory = customizationFactory;
		this.cmsUri = customizationFactory.getCmsURI();
	}

	@Override
	public void handleOpenURL(URL url) throws IOException {
		IURLMapper urlMapper = customizationFactory.getURLMapper();

		OxygenOpenDocumentParmaters parameters = urlMapper
				.mapURLToOpenParameters(url, cmsUri);

		try {
			mainComponent.openDocumentInNewTab(parameters, false);
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

	}

}
