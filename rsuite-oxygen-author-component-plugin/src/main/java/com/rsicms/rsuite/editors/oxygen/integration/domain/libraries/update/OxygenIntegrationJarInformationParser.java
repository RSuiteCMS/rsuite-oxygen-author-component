package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

import java.util.HashMap;
import java.util.Map;

import com.rsicms.rsuite.editors.oxygen.integration.utils.Base64Coder;

public class OxygenIntegrationJarInformationParser {

	public static Map<String, OxygenIntegrationLocalJarInformation> parseEncodedLocalFileList(
			String localFileInformationList) {
		Map<String, OxygenIntegrationLocalJarInformation> localFilesMap = new HashMap<String, OxygenIntegrationLocalJarInformation>();
		
		
		String[] localFileList = localFileInformationList.split("\n");
		
		for (String localFileLine : localFileList){
			
			if (localFileLine.trim().isEmpty()){
				continue;
			}
			
			String[] localFileInfo = localFileLine.split("\\|");
			
			OxygenIntegrationLocalJarInformation localFile = new OxygenIntegrationLocalJarInformation(localFileInfo);
			localFilesMap.put(localFile.getFileName(), localFile);
		}
		
		return localFilesMap;
	}
	
}
