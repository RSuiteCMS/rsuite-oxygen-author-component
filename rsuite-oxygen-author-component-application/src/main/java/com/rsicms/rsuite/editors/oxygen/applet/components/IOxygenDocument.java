package com.rsicms.rsuite.editors.oxygen.applet.components;

import ro.sync.ecss.extensions.api.AuthorDocumentController;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;

public interface IOxygenDocument {

	String getSerializedDocument();
	
	IDocumentURI getDocumentUri();

	IDocumentCustomization getDocumentCustomization();
	
	void restoreFrameTitle();
	
	boolean isModified();
	
	AuthorDocumentController getOxygenDocumentController();
}
