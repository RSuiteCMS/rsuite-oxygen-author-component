package com.rsicms.rsuite.editors.oxygen.integration.webservice;


import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.api.configuration.OxygenConfigurationUtils;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class ConfigurationWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(ConfigurationWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String xml = "<response />";
		
		try {
			InputStream is = OxygenConfigurationUtils.readConfiguration(context);
			if (is != null) {
				xml = IOUtils.toString(is, "utf-8");
			}

		} catch (IOException e) {
			log.error("Unable to parse configuration");
		}

		XmlRemoteApiResult result = new XmlRemoteApiResult(xml);
		result.setContentType("text/xml");
		return result;

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
