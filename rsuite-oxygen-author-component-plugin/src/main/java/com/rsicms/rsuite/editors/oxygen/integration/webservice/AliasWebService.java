package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.utils.MoUtils;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class AliasWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(AliasWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String rsuiteId = args.getFirstValue("rsuiteId");

		ManagedObject mo = MoUtils.getTopManagedObject(context, rsuiteId);
		
		String xml = "<aliases type=\"list\">";

		if (mo != null) {
			for (Alias alias : mo.getAliases()) {
				xml += "<map>";
				xml += "<text>" + alias.getText() + "</text>";
				xml += "<type>" + alias.getType() + "</type>";
				xml += "</map>";
			}
		}

		xml += "</aliases>";

		XmlRemoteApiResult result = new XmlRemoteApiResult(xml);
		result.setContentType("text/xml");
		return result;

	}
	


	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
