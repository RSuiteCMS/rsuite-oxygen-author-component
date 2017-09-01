package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.handler;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RSuiteCachedEntityResolver;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RsuiteCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RsuiteDefaultURLMapper;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers.SchemaCache;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteRefernceHandler implements InsertReferenceHandler {

	private SchemaCache schemaCache;

	private ICmsURI cmsURI;

	private Logger logger = Logger.getLogger(getClass());

	private RSuiteCachedEntityResolver rsuiteEntityResolver;

	public RSuiteRefernceHandler(RsuiteCustomizationFactory customizationFactory) {
		this.schemaCache = customizationFactory.getSchemaCache();
		this.cmsURI = customizationFactory.getCmsURI();
		EntityResolver resolver = customizationFactory.getEntityResolver();

		if (resolver instanceof RSuiteCachedEntityResolver) {

			rsuiteEntityResolver = ((RSuiteCachedEntityResolver) resolver);
		}
	}

	@Override
	public String getLinkValue(ISelectedReferenceNode selectedNode,
			String linkValue) {
		return linkValue;
	}

	@Override
	public String getXmlFragment(ISelectedReferenceNode selectedNode,
			String defaultXmlFragment) {
		return defaultXmlFragment;
	}

	@Override
	public void beforeInsertFragment(AuthorAccess paramAuthorAccess,
			InsertReferenceElement refenceElement,
			ISelectedReferenceNode selectedNode)
			throws AuthorOperationException {
		if (refenceElement.isConfRef()) {

			String rsuiteId = selectedNode.getRepositoryResource().getCMSid();

			String schemaId = getSchemaIdForReferencedResource(rsuiteId);

			try {
				schemaCache.downloadSchemas(rsuiteId, schemaId);
			} catch (Exception e) {
				OxygenUtils.handleException(logger, e);
			}

		}

	}

	private String getSchemaIdForReferencedResource(String rsuiteId) {
		String infoUrl = cmsURI.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.mo.info" + "?";

		infoUrl += cmsURI.getSessionKeyParam();

		infoUrl += "&rsuiteId=" + rsuiteId;

		OxygenOpenDocumentParmaters documentParameters = RsuiteDefaultURLMapper
				.obtainOpenDocumentParameters(infoUrl, cmsURI);

		String schemaId = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_ID);

		String schemaPublicId = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID);
		
		String schemaSystemId = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID);

		if (rsuiteEntityResolver != null) {
			rsuiteEntityResolver.addPublicSchemaIdMapping(schemaPublicId,
					schemaId);
			rsuiteEntityResolver.addSystemSchemaIdMapping(schemaSystemId, schemaId);
		}

		return schemaId;
	}

	@Override
	public void afterInsertFragment(AuthorAccess paramAuthorAccess,
			InsertReferenceElement refenceElement,
			ISelectedReferenceNode selectedNode)
			throws AuthorOperationException {

	}

}
