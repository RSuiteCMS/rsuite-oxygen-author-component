package com.rsicms.rsuite.editors.oxygen.integration.webservice.launch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIntegrationLibraryManager;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIntegrationLibraryManagerFactory;
import com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarInformationParser;
import com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarUpdateInformation;
import com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarsUpdateManager;
import com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationLocalJarInformation;
import com.rsicms.rsuite.editors.oxygen.integration.utils.Base64Coder;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.TextRemoteApiResult;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class OxygenJarInfoWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory.getLog(OxygenJarInfoWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String encodedLocalFileList = args.getFirstString("localFiles", "");

		String decodedlocalFileList = Base64Coder
				.decodeString(encodedLocalFileList);

		Map<String, OxygenIntegrationLocalJarInformation> localJarList = OxygenIntegrationJarInformationParser
				.parseEncodedLocalFileList(decodedlocalFileList);

		OxygenIntegrationLibraryManagerFactory libraryManagerFactory = getLibraryManagerFactory();
		OxygenIntegrationLibraryManager libraryManager = libraryManagerFactory
				.createOxygenIntegrationLibraryManager(context);

		OxygenIntegrationJarsUpdateManager jarUpdateManager = new OxygenIntegrationJarsUpdateManager(
				libraryManager);
		List<OxygenIntegrationJarUpdateInformation> jarsToUpdate = jarUpdateManager
				.getFileListToUpdate(localJarList);

		TextRemoteApiResult result = createResultObject(jarsToUpdate);

		return result;

	}

	protected OxygenIntegrationLibraryManagerFactory getLibraryManagerFactory() {
		return new OxygenIntegrationLibraryManagerFactory();
	}

	private TextRemoteApiResult createResultObject(
			List<OxygenIntegrationJarUpdateInformation> jarsToUpdate) {
		StringBuilder serializedJarToUpdateList = new StringBuilder();
		for (OxygenIntegrationJarUpdateInformation jarToUpdate : jarsToUpdate) {
			serializedJarToUpdateList.append(jarToUpdate).append("\n");
		}

		String encodedResponse = Base64Coder
				.encodeString(serializedJarToUpdateList.toString());

		TextRemoteApiResult result = new TextRemoteApiResult(encodedResponse);
		return result;
	}

	protected Map<String, OxygenIntegrationLocalJarInformation> parseEncodedLocalFileList(
			String encodedLocalFileList) {
		Map<String, OxygenIntegrationLocalJarInformation> localFilesMap = new HashMap<String, OxygenIntegrationLocalJarInformation>();
		String decodedlocalFileList = Base64Coder
				.decodeString(encodedLocalFileList);

		String[] localFileList = decodedlocalFileList.split("\n");

		for (String localFileLine : localFileList) {
			String[] localFileInfo = localFileLine.split("|");

			OxygenIntegrationLocalJarInformation localFile = new OxygenIntegrationLocalJarInformation(
					localFileInfo);
			localFilesMap.put(localFile.getFileName(), localFile);
		}

		return localFilesMap;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
