package com.rsicms.rsuite.editors.oxygen.launcher.update;

import java.util.List;
import java.util.UUID;

import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteSessionManager;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http.HttpUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileInformation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileInformationHelper;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdate;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdateParser;

public class OxygenApplicationRSuiteUpdater implements
		OxygenApplicationCmsUpdater {

	private RSuiteSessionManager sessionManager;

	private static final String UPDATE_WS_URL_PART = "rsuite/rest/v1/api/rsuite.oxygen.application.jar.info";

	private static final String DOWNLOAD_WS_URL_PART = "/rsuite/rest/v2/static";
	
	private String hostURI;
	
	public OxygenApplicationRSuiteUpdater(String baseURI, RSuiteSessionManager sessionManager) {
		super();
		this.sessionManager = sessionManager;
		hostURI = baseURI.substring(0, baseURI.indexOf("/rsuite/rest"));
	}

	@Override
	public List<FileToUpdate> obtainFilesToUpdate(
			List<FileInformation> localFiles) throws OxygenApplicationException {

		String requestMessage = FileInformationHelper
				.serializeAndBase64EncodeLocalFiles(localFiles);
		String responseMessage = obtainFilesToUpdate(requestMessage);
		return FileToUpdateParser
				.parseBase64EncodedFilesToUpdate(responseMessage);
	}
	
	private String obtainFilesToUpdate(String requestMessage)
			throws OxygenApplicationException {
		String requestPath = hostURI + "/" + UPDATE_WS_URL_PART + "?skey="
				+ sessionManager.getSessionKey() + "&localFiles=" + requestMessage + "&" + UUID.randomUUID().toString();
		return HttpUtils.sendGetRequest(requestPath);
	}

	@Override
	public String createUrlForFile(FileToUpdate fileToUpdate) {
		return hostURI + DOWNLOAD_WS_URL_PART + "/" + fileToUpdate.getUrl() + "?skey="
				+ sessionManager.getSessionKey();
	}

}
