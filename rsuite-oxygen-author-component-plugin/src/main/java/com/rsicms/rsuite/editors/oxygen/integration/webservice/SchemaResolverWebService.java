package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.SchemaInfo;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.service.SchemaService;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.TextRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class SchemaResolverWebService extends DefaultRemoteApiHandler {

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String systemId = args.getFirstValue("systemId");

		Collection<SchemaInfo> schemaInfoCollection = context
				.getSchemaService().getSchemaInfoValues();

		SchemaService schemaService = context.getSchemaService();

		Iterator<SchemaInfo> schemaInfoIt = schemaInfoCollection.iterator();

		StringBuilder sb = new StringBuilder();

		Map<String, String> locationMap = new HashMap<String, String>();

		while (schemaInfoIt.hasNext()) {

			SchemaInfo schemaInfo = schemaInfoIt.next();

			locationMap.put(schemaInfo.getSystemLocation(),
					schemaInfo.getSchemaId());

		}
		if (systemId != null) {

			String schemaId = locationMap.get(systemId);
			if (schemaId != null) {
				sb.append(schemaService.getSchemaContent(schemaId));
			}
		}

		return new TextRemoteApiResult(sb.toString());

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {

	}

}
