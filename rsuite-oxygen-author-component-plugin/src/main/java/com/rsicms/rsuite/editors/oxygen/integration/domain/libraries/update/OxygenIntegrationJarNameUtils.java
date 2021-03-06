package com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class OxygenIntegrationJarNameUtils {

	
	private static final String VERSION_MARKER = "__V";

	public static String getJarVersionFromPath(String path){
		String jarVersion = null;
		
		String baseName = FilenameUtils.getBaseName(path);
		
		int versionPosition = baseName.indexOf(VERSION_MARKER);
		
		if (versionPosition > -1){
			jarVersion = baseName.substring(versionPosition + VERSION_MARKER.length());
		}
		
		return jarVersion;
	}
	
	public static boolean hasJarVersion(String path){
		String version = getJarVersionFromPath(path);
		return StringUtils.isNotEmpty(version);
	}
}
