package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.OpenDocumentParametersGenerator;

public class OpenDocumentParametersWebService extends DefaultRemoteApiHandler {

	public Log logger = LogFactory.getLog(getClass());

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			String openDocumentParamaters = OpenDocumentParametersGenerator
					.getOpenDocumentParametersInXmlFormat(context, args);

			// TODO check out?
			boolean checkOut = args.getFirstBoolean("checkOut", false);
			return createXMLResult(openDocumentParamaters);

		} catch (Exception e) {
			logger.error(e, e);
			return createXMLResult("<error>" + e.getLocalizedMessage()
					+ "</error>");
		}

	}

	private XmlRemoteApiResult createXMLResult(String xml) {
		XmlRemoteApiResult result = new XmlRemoteApiResult(xml);
		result.setContentType("text/xml");
		return result;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
