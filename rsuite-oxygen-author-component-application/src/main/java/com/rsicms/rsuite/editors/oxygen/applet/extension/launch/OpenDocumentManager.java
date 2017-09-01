package com.rsicms.rsuite.editors.oxygen.applet.extension.launch;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneFrame;

public class OpenDocumentManager {

	private OxygenMainComponent component;

	private OxygenStandaloneFrame mainFrame;

	public OpenDocumentManager(OxygenMainComponent component) {
		this.component = component;
	}
	
	public OpenDocumentManager(OxygenStandaloneFrame frame, OxygenMainComponent component) {
		this.component = component;
		this.mainFrame = frame;
	}

	public void openDocumentInANewTab(String documentId)
			throws OxygenIntegrationException {
		OxygenOpenDocumentParmaters oxygenOpenDocumentParmaters = getOpenDocumentParametersForMo(
				component.getCmsCustomization().getCmsActions(), documentId);
		component.openDocumentInNewTab(oxygenOpenDocumentParmaters);

	}

	private static OxygenOpenDocumentParmaters getOpenDocumentParametersForMo(
			ICmsActions cmsActions, String moIdToOpen)
			throws OxygenIntegrationException {

		return cmsActions.getOpenDocumentParmaters(moIdToOpen);
	}

	public void bringApplicationToFront() {
		if (mainFrame != null){
			mainFrame.bringFrameToFront();
		}
		
		
	}
	
	

}
