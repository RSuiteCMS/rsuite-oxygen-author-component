package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;


public interface ICmsCustomization {
	
	ICmsActions getCmsActions();
	
	IDocumentHandler getSystemDocumentHandler();
	
	ISchemaAwareCustomizationFactory getDefaultSchemaCustomizationFactory();
	
	InsertReferenceHandler getSystemReferenceHandler();
	
	IRepositoryBookmarks getRepositoryBookmarks();
	
	ITreeOxygenLookUp getRepositoryTreeLookup();
}
