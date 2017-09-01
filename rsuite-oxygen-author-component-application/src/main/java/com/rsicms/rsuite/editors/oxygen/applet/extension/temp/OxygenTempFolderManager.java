package com.rsicms.rsuite.editors.oxygen.applet.extension.temp;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class OxygenTempFolderManager {

	private File tempFolder;

	public OxygenTempFolderManager(String baseUri) throws OxygenIntegrationException {
		String baseUriFolder = baseUri.replaceAll("[:/\\s+-]", "_");
		
		String userHome = "user.home";        
		String path = System.getProperty(userHome);
		
		File rsiFolder = new File(path, ".com.rsicms.oxygen");
		tempFolder = new File(rsiFolder,  baseUriFolder);

		if (!tempFolder.exists() && !createTempFolder()){
			throw new OxygenIntegrationException("Unable to create temp folder: " + tempFolder.getAbsolutePath());
		}
		
	}

	private boolean createTempFolder() {
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			@Override
			public Boolean run() {
				return tempFolder.mkdirs();				
			}
		}).booleanValue();
	}

	public File getTempFolder() {
		return tempFolder;
	}

	
}
