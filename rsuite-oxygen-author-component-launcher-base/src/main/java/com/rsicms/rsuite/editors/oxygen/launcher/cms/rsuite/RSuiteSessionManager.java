package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpConnector;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpResponse;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenLauncherParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.session.CmsSessionManager;

public class RSuiteSessionManager implements CmsSessionManager {

	private String rsuiteURI;

	private HttpConnector httpConnector = new HttpConnector();

	private volatile String sessionKey;

	private RSuiteLocalSession localSession;

	public RSuiteSessionManager(String rsuiteURI, String sessionKey) {
		this.rsuiteURI = rsuiteURI;
		this.sessionKey = sessionKey;
	}

	public RSuiteSessionManager(RSuiteLocalSession localSession,
			OxygenLauncherParameters parameters)
			throws OxygenApplicationException {
		rsuiteURI = parameters.getHostAddress();
		sessionKey = parameters.getSessionKey();
		this.localSession = localSession;

		getLocalSesssion(localSession);
	}

	private void getLocalSesssion(RSuiteLocalSession localSession)
			throws OxygenApplicationException {

		if (localSession != null) {
			String localSessionValue = localSession.getLocalSession();

			if (localSessionValue != null) {
				sessionKey = localSessionValue;
			}
		}
	}

	public RSuiteSessionManager(OxygenApplicationParameters launcherParameters) {
		rsuiteURI = launcherParameters.getHostAddress();
		sessionKey = launcherParameters.getSessionKey();
	}

	@Override
	public boolean isValidSession() throws OxygenApplicationException {
		String checkSessionUri = rsuiteURI
				+ "/rsuite/rest/v1/api/rsuite.oxygen.check.session?skey="
				+ sessionKey + "&" + UUID.randomUUID().toString();
		HttpResponse response = httpConnector.sendGetRequest(checkSessionUri);

		if (response.getCode() >= 200 && response.getCode() < 300) {
			sessionKey = response.getResponseText();
			persistLocalSession(sessionKey);
			return true;
		}

		return false;
	}

	@Override
	public boolean createNewSession(String projectName, String userName,
			String password) throws OxygenApplicationException {

		try {

			Map<String, String> formData = new HashMap<String, String>();
			formData.put("user", userName);
			formData.put("pass", password);

			HttpResponse response = httpConnector.sendPostRequest(rsuiteURI
					+ "/rsuite/rest/v2/user/session", formData);

			String sessionKey = RSuiteHttpResponseParser
					.parseResponseSaveResponse(response.getResponseText(),
							"key");
			this.sessionKey = sessionKey;

			persistLocalSession(sessionKey);

		} catch (Exception e) {

			e.printStackTrace();

			if (e.getMessage() != null
					&& e.getMessage().contains(
							"Unknown user ID/password combination")) {
				return false;
			}

			throw new OxygenApplicationException("Unable to log in to CMS: "
					+ e.getMessage(), e);
		}

		return true;
	}

	private void persistLocalSession(String sessionKey)
			throws OxygenApplicationException {
		if (localSession != null) {
			localSession.persistLocalSession(sessionKey);
		}
	}

	public String getSessionKey() {
		return sessionKey;
	}

}
