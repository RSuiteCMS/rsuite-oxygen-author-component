package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;

import java.util.UUID;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpConnector;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpResponse;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParametersFactory;

public class RSuiteOxygenApplicationParametersFactory implements
		OxygenApplicationParametersFactory {

	private String rsuiteURI;
	
	private HttpConnector httpConnector = new HttpConnector();

	private RSuiteSessionManager sessionManager;
	
	
	
	public RSuiteOxygenApplicationParametersFactory(String rsuiteURI,
			RSuiteSessionManager sessionManager) {
		super();
		this.rsuiteURI = rsuiteURI;
		this.sessionManager = sessionManager;
	}



	@Override
	public OxygenApplicationParameters createParamaters() throws OxygenApplicationException {
		
		sessionManager.isValidSession();
		
		
		String parametersUri = rsuiteURI + "/rsuite/rest/v1/api/rsuite.oxygen.launch.base.parameters?skey=" + sessionManager.getSessionKey() +"&" + UUID.randomUUID().toString();
		HttpResponse response = httpConnector.sendGetRequest(parametersUri);
		
		if (response.getCode() >= 200 && response.getCode() < 300){
			String paramaterList = response.getResponseText();
			return new OxygenApplicationParameters(paramaterList);
		}
		throw new OxygenApplicationException("Unable to obtain startup parameters from RSuite. Please check if RSuite is running and run the launcher once again.");		
	}

}
