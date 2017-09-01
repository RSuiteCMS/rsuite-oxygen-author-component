package com.rsicms.rsuite.editors.oxygen.applet.components.helpers;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.MathFlowConfiguration;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;

public class OxygenLicenseUtils {

	private static Logger logger = Logger.getLogger(OxygenLicenseUtils.class);
	
	public static void setMathFlowLicenses(AuthorComponentFactory authorComponentFactory, OxygenConfiguration oxygenConfiguration) {
		MathFlowConfiguration mathFlowConfiguration = oxygenConfiguration.getMathFlowConfiguration();
		
		if (mathFlowConfiguration != null){
			String editorLicenseKey = mathFlowConfiguration.getEditorLicenseKey();
			
			if (StringUtils.isNotBlank(editorLicenseKey)){
				authorComponentFactory.setMathFlowFixedLicenseKeyForEditor(editorLicenseKey);
			}
			
			String composerLicenseKey = mathFlowConfiguration.getEditorLicenseKey();
			if (StringUtils.isNotBlank(composerLicenseKey)){
				authorComponentFactory.setMathFlowFixedLicenseKeyForEditor(composerLicenseKey);
			}
		}
	}
	
	
	public static String getLicenseKey(ICustomizationFactory customizationFactory, ICmsURI cmsUri) {		
		String license = null;
		try{
		
			license = customizationFactory.getLicenseKey(cmsUri);
		}catch(IOException e){
			OxygenUtils.handleException(logger, e);
		}
		
		return license;
	}

}
