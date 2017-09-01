package com.rsicms.rsuite.editors.oxygen.integration.webservice.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.TextRemoteApiResult;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class RSuiteSessionCheckWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory.getLog(RSuiteSessionCheckWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		return new TextRemoteApiResult(context.getSession().getKey());

	}
}
