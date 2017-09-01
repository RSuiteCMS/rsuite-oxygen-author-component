package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IModifiableCmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.bookmarks.RSuiteRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.handler.RSuiteDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.handler.RSuiteRefernceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.RsuiteOxygenLookUp;

public class RSuiteCmsCustomization implements ICmsCustomization {

	private ICmsActions cmsActions;
	
	private IDocumentHandler documentHandler;
	
	private InsertReferenceHandler referenceHandler;
	
	private IRepositoryBookmarks repositoryBookmarks;
	
	private ICmsURI cmsUri;
	
	public RSuiteCmsCustomization(RsuiteCustomizationFactory customizationFactory, IModifiableCmsURI cmsURI) throws OxygenIntegrationException {
		cmsActions = RSuiteObjectFactory.getRSuiteActions(cmsURI, customizationFactory.getVersion());
		documentHandler = new RSuiteDocumentHandler(customizationFactory.getSchemaCache());
		referenceHandler = new RSuiteRefernceHandler(customizationFactory);
		repositoryBookmarks = new RSuiteRepositoryBookmarks(customizationFactory.getCmsURI());
		this.cmsUri = customizationFactory.getCmsURI();
	}


	@Override
	public ICmsActions getCmsActions() {
		return cmsActions;
	}


	@Override
	public IDocumentHandler getSystemDocumentHandler() {
		return documentHandler;
	}


	@Override
	public ISchemaAwareCustomizationFactory getDefaultSchemaCustomizationFactory() {
		return new RSuiteDefaultSchemaCustomizationFactory();
	}

	@Override
	public InsertReferenceHandler getSystemReferenceHandler() {	
		return referenceHandler;
	}


	@Override
	public IRepositoryBookmarks getRepositoryBookmarks() {
		return repositoryBookmarks;
	}


	@Override
	public ITreeOxygenLookUp getRepositoryTreeLookup() {
		return new RsuiteOxygenLookUp(cmsUri);
	}
}
