package com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class OpenNewDocumentAction {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(OpenNewDocumentAction.class);

	private OxygenMainComponent mainComponent;
	
	public OpenNewDocumentAction(OxygenMainComponent mainComponent) {
		this.mainComponent = mainComponent;
	}

	public void openNewDocument(IReposiotryResource iReposiotryResource) {

		try {
			OxygenOpenDocumentParmaters openDocumentParameters = getOpenDocumentParameters(iReposiotryResource);
			mainComponent.openDocumentInNewTab(openDocumentParameters);
		} catch (Exception e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}
	}

	private OxygenOpenDocumentParmaters getOpenDocumentParameters(
			IReposiotryResource iReposiotryResource) throws OxygenIntegrationException {

		 			
		 ICmsActions cmsActions = mainComponent.getCmsCustomization().getCmsActions();
		
		 String id = iReposiotryResource.getCMSid();
		 String rsuiteReferenceMetadata = "rsuiteReferenceId";
		 if (iReposiotryResource.getCustomMetadataNames().contains(rsuiteReferenceMetadata)){
			 id = iReposiotryResource.getCustomMetadata(rsuiteReferenceMetadata); 
		 }
		
		return cmsActions.getOpenDocumentParmaters(id);
	}
}
