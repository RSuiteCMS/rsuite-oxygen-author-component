package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_LICENSE_KEY_FILE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertiesUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertyResult;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginUtils;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.TextRemoteApiResult;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class LicenseWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(LicenseWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String licenseFileProprerty = PROPERTY_OXYGEN_LICENSE_KEY_FILE
				.getProperty();

		PluginPropertyResult resultProperty = PluginPropertiesUtils
				.getPluginProperty(context, licenseFileProprerty);

		String license = "";

		try {

			if (resultProperty == null) {
				ConfigurationProperties configurationProperties = context
						.getConfigurationProperties();
				String filePath = configurationProperties.getProperty(
						licenseFileProprerty, null);
				if (filePath != null){
					license = FileUtils.readFileToString(new File(filePath),
							"UTF-8");
				}
			} else {

				InputStream in = PluginUtils.loadResourceFromPlugin(context,
						resultProperty.getPluginId(),
						resultProperty.getPropertyValue());
				license = IOUtils.toString(in, "UTF-8");								
			}

		} catch (IOException e) {
			log.error("Unable to obtain license key", e);
		}

		return new TextRemoteApiResult(license);

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
