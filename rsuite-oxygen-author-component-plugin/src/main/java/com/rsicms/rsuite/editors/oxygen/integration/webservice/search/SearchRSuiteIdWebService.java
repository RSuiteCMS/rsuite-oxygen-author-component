package com.rsicms.rsuite.editors.oxygen.integration.webservice.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.search.RSuiteIdSearch;
import com.rsicms.rsuite.editors.oxygen.integration.search.SearchSerlizer;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResult;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.JSonRemoteApiResult;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class SearchRSuiteIdWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(SearchRSuiteIdWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String phrase = args.getFirstValue("phrase");
		
		Collection<MoResult> resultsMO = new ArrayList<>();
		
		if (RSuiteIdSearch.isRSuiteIdSearch(phrase)){
			resultsMO = RSuiteIdSearch.rsuiteIdSearch(context, phrase);	
		}

		String json = "error";
		try {
			json = SearchSerlizer.serializeMOsToJSon(resultsMO);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		
		JSonRemoteApiResult result = new JSonRemoteApiResult(json);
		
		return result;

	}

	

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
