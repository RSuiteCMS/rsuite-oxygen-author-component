package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.EditorComponentProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers.SchemaCache;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteDocumentHandler implements IDocumentHandler {

	private Logger logger = Logger.getLogger(getClass());

	private SchemaCache schemaCache;

	public RSuiteDocumentHandler(SchemaCache schemaCache) {
		this.schemaCache = schemaCache;
	}

	@Override
	public String modifyDocumentBeforeLoad(String content) throws IOException {
		return content;
	}

	@Override
	public String modifyDocumentBeforeSave(String content) throws IOException {
		return content;
	}

	@Override
	public void beforeOpenDocument(IDocumentURI documentUri,
			OxygenOpenDocumentParmaters parameters,
			EditorComponentProvider editorComponentProvider) {

		try {
			schemaCache.waitForSchemaDownload(documentUri.getEditedDocumentId(),
							parameters
									.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_ID));
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

	}

	@Override
	public void afterOpenDocument(IDocumentURI documentUri,
			OxygenOpenDocumentParmaters parameters,
		EditorComponentProvider editorComponentProvider) {		
	}

	@Override
	public void beforeDocumentInitialization(IDocumentURI documentUri,
			OxygenOpenDocumentParmaters parameters) {
				schemaCache.requestSchemaDownload(documentUri.getEditedDocumentId(),
						parameters
						.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_ID));
		
	}

	

}
