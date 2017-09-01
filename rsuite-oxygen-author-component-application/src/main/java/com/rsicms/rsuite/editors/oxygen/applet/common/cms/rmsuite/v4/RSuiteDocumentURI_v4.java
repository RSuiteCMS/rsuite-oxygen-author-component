package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.v4;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteDocumentURI_v4 implements IDocumentURI {

	private static final String CONTENT_PART = "/content/binary/id/";

	private String editedDocumentId;

	private String documentURI;

	private ICmsURI cmsURI;

	private String saveURI;

	public RSuiteDocumentURI_v4(ICmsURI cmsURI,
			OxygenOpenDocumentParmaters documentParameters) {

		documentURI = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);
		editedDocumentId = documentURI.substring(documentURI
				.indexOf(CONTENT_PART) + CONTENT_PART.length());
		editedDocumentId = editedDocumentId.substring(0,
				editedDocumentId.indexOf('?'));
		this.cmsURI = cmsURI;

		saveURI = cmsURI.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.save.document" + "?"
				+ cmsURI.getSessionKeyParam();
		// documentURI.replace(CONTENT_PART, "/content/id/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getEditedDocumentId
	 * ()
	 */
	@Override
	public String getEditedDocumentId() {
		return editedDocumentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getSaveUri()
	 */
	@Override
	public String getSaveUri() {
		return saveURI;
	}

	@Override
	public String getDocumentURI() {
		return documentURI;
	}

	@Override
	public ICmsURI getCMSUri() {
		return cmsURI;
	}

}
