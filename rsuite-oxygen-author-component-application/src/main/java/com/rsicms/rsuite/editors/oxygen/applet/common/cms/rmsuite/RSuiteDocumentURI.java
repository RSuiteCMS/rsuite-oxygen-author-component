package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteDocumentURI implements IDocumentURI {

	private static final String CONTENT_PART = "/content/";

	private String editedDocumentId;

	private String documentURI;
	
	private ICmsURI cmsURI;

	public RSuiteDocumentURI(ICmsURI cmsURI, OxygenOpenDocumentParmaters documentParameters) {

		documentURI = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);
		editedDocumentId = documentURI.substring(documentURI
				.indexOf(CONTENT_PART) + CONTENT_PART.length());
		editedDocumentId = editedDocumentId.substring(0,
				editedDocumentId.indexOf('?'));
		this.cmsURI = cmsURI;
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


//	public String getImagePreviewUri(String imageId) {
//		return cmsURI.getHostURI()
//				+ "rsuite/rest/v1/api/rsuite.oxygen.image.preview?imageId="
//				+ imageId + "&" + cmsURI.getSessionKeyParam();
//	}
//
//	public String getImagePreviewUriByAlias(String imageAlias) {
//		return cmsURI.getHostURI()
//				+ "rsuite/rest/v1/api/rsuite.oxygen.image.preview?image="
//				+ imageAlias + "&" + cmsURI.getSessionKeyParam();
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getSaveUri()
	 */
	@Override
	public String getSaveUri() {
		return documentURI;
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
