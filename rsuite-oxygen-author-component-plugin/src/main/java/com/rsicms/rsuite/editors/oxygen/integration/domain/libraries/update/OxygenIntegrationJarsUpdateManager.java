package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

import static com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarNameUtils.*;
import static com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarUpdateOperation.ADD;
import static com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarUpdateOperation.DELETE;
import static com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarUpdateOperation.UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIngegrationLibrary;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIntegrationLibraryManager;

public class OxygenIntegrationJarsUpdateManager {

	private OxygenIntegrationLibraryManager libraryManager;

	public OxygenIntegrationJarsUpdateManager(
			OxygenIntegrationLibraryManager libraryManager) {
		this.libraryManager = libraryManager;
	}

	public List<OxygenIntegrationJarUpdateInformation> getFileListToUpdate(
			Map<String, OxygenIntegrationLocalJarInformation> clientJarList)
			throws RSuiteException {

		List<OxygenIntegrationJarUpdateInformation> jarsToUpdate = new ArrayList<>();

		List<OxygenIngegrationLibrary> integrationLibraries = libraryManager
				.getIntegrationLibraries();

		for (OxygenIngegrationLibrary integrationLibrary : integrationLibraries) {
			String pathToPlugin = integrationLibrary.getPath();
			String jarName = FilenameUtils.getName(pathToPlugin);
			OxygenIntegrationLocalJarInformation clientJar = clientJarList
					.get(jarName);

			if (clientJar == null) {
				jarsToUpdate.add(new OxygenIntegrationJarUpdateInformation(
						integrationLibrary.getRestPathWithNoRandomVersion(), integrationLibrary.getSize(), ADD));
			} else {
				boolean needToUpdate = checkIfClientJarNeedToBeUpdated(
						integrationLibrary, clientJar);

				if (needToUpdate) {
					jarsToUpdate.add(new OxygenIntegrationJarUpdateInformation(
							integrationLibrary.getRestPathWithNoRandomVersion(), integrationLibrary.getSize(), UPDATE));
				}

				clientJarList.remove(jarName);
			}
		}

		for (String clientJarName : clientJarList.keySet()) {
			jarsToUpdate.add(new OxygenIntegrationJarUpdateInformation(
					clientJarName, DELETE));
		}

		return jarsToUpdate;

	}

	private boolean checkIfClientJarNeedToBeUpdated(
			OxygenIngegrationLibrary serverJar,
			OxygenIntegrationLocalJarInformation clientJar) {

		String clientVersion = getJarVersionFromPath(clientJar.getFileName());
		String serverVersion = getJarVersionFromPath(FilenameUtils.getName(serverJar.getPath()));
		
		if (clientVersion != null && serverVersion != null
				&& clientVersion.equalsIgnoreCase(serverVersion)) {
			return false;
		}
		
		
		if (clientJar.getFileSize() == serverJar.getSize()
				&& clientJar.getCrc() == serverJar.getCrc()) {
			return false;

		}

		return true;
	}

}
