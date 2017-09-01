package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IModifiableCmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteURI implements IModifiableCmsURI {

	private static final String SKEY_PARAM = "skey=";

	private String hostURI;

	private String sessionKey;

	private String baseURI;

	private String configurationURI;
	
	public RSuiteURI(String baseURI, String sessionKey) {
		this.baseURI = baseURI;
		this.sessionKey = sessionKey;
		this.setUpURIs();
	}

	public RSuiteURI(OxygenOpenDocumentParmaters documentParameters) {

		this.baseURI = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.BASE_URI);

		String documentURI = documentParameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);

		sessionKey = documentURI.substring(documentURI.indexOf(SKEY_PARAM)
				+ SKEY_PARAM.length());

		setUpURIs();
	}

	private void setUpURIs() {
		
		hostURI = baseURI.substring(0, baseURI.indexOf("/rsuite/rest"));
		
		this.configurationURI = hostURI
				+ "/rsuite/rest/v1/api/rsuite.oxygen.configuration?" + getSessionKeyParam();				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getHostURI()
	 */
	@Override
	public String getHostURI() {
		return hostURI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getSessionKey()
	 */
	@Override
	public String getSessionKey() {
		return sessionKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getLookUpUri()
	 */
	@Override
	public String getLookUpUri() {
		return hostURI + "/rsuite/rest/v1/api/rsuite.oxygen.tree.control";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getSessionKeyParam
	 * ()
	 */
	@Override
	public String getSessionKeyParam() {
		return SKEY_PARAM + sessionKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.common.ICmsURI#getImagePreviewUri
	 * (java.lang.String)
	 */
	@Override
	public String getImagePreviewUri(String imageId) {
		return hostURI
				+ "/rsuite/rest/v1/api/rsuite.oxygen.image.preview?imageId="
				+ imageId + "&" + getSessionKeyParam();
	}

	public String getImagePreviewUriByAlias(String imageAlias) {
		
		return hostURI
				//+ "/rsuite/rest/v1/api/rsuite.oxygen.image.preview?image="
				+ imageAlias				
				+ "?" + getSessionKeyParam();
	}

	@Override
	public String getBaseURI() {
		return baseURI;
	}

	@Override
	public String getConfigurationURI() {
		return configurationURI;
	}

	@Override
	public void updateSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;		
	}

}
