package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveProgressListener;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public interface ICmsActions {

	public void saveDocument(IDocumentURI documentUri, SaveProgressListener progressListener, byte[] content) throws OxygenIntegrationException ;
	
	public void checkInDocument(IDocumentURI documentUri, String versionType, String versionNote) throws OxygenIntegrationException ;
	
	public String loadDocument(IDocumentCustomization customization, IDocumentURI documentUri) throws OxygenIntegrationException;

	public boolean logInToCms(String text, String string) throws OxygenIntegrationException;

	public boolean isSessionValid() throws OxygenIntegrationException;

	OxygenOpenDocumentParmaters getOpenDocumentParmaters(String documentId) throws OxygenIntegrationException;
}
