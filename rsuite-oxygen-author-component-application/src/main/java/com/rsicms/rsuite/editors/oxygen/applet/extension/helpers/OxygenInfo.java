package com.rsicms.rsuite.editors.oxygen.applet.extension.helpers;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class OxygenInfo {

	private Logger logger = Logger.getLogger(getClass());

	private String version = "N/A";

	public OxygenInfo() {
		try {

			URL url = getClass().getClassLoader().getResource(
					"Manifest-Version.MF");
			if (url != null) {
				Manifest manifest = new Manifest(url.openStream());

				String appletVersion = manifest.getMainAttributes().getValue(
						"Rsuite-Oxygen-Version");
				version = appletVersion;
			}

		} catch (IOException e) {
			OxygenUtils.handleException(logger, e);
		}

	}

	public String getVersion() {
		return version;
	}

}
