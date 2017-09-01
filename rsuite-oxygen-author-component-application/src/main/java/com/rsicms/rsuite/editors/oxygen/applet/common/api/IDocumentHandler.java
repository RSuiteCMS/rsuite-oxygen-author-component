package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import java.io.IOException;

import ro.sync.ecss.extensions.api.component.EditorComponentProvider;

import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public interface IDocumentHandler {

	String modifyDocumentBeforeLoad(String contentToLoad) throws IOException;
	
	String modifyDocumentBeforeSave(String contentToSave)  throws IOException;
	
	void beforeDocumentInitialization(IDocumentURI documentUri, OxygenOpenDocumentParmaters parameters);
	
	void beforeOpenDocument(IDocumentURI documentUri, OxygenOpenDocumentParmaters parameters, EditorComponentProvider editorComponentProvider);
	
	void afterOpenDocument(IDocumentURI documentUri, OxygenOpenDocumentParmaters parameters, EditorComponentProvider editorComponentProvider);
}
