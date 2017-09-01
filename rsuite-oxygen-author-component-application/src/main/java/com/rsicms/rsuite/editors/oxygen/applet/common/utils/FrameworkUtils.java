package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.commons.io.FilenameUtils;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.ClassPathResourcesAccess;

public class FrameworkUtils {

	public static  InputStream getFileFromFrameworkResourceFolder(
			final AuthorAccess authorAccess, final String frameworkId,
			final String fileName) {
		
		InputStream fileInputStream = AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
			public InputStream run() {
				
				try {					
					ClassPathResourcesAccess classPathResourcesAccess = authorAccess.getClassPathResourcesAccess();
					for (URL url : classPathResourcesAccess.getClassPathResources()){
						String path = FilenameUtils.separatorsToUnix(url.toString());
						if (path.contains(frameworkId + "/resources")){
								return new URL(url.toExternalForm() + "/" + fileName).openStream();
						}
					}
				} catch (Exception e) {
					
				}
				return null;
			}
		});
		return fileInputStream;
	}
}
