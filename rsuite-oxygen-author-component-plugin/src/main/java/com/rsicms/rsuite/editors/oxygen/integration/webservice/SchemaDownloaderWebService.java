package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.utils.SchemaUtils;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class SchemaDownloaderWebService extends DefaultRemoteApiHandler
		implements OxygenConstants {

	public static Log log = LogFactory.getLog(SchemaDownloaderWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		ManagedObject mo = validateInputParameters(context, args);

		ByteArrayOutputStream outStream = SchemaUtils.archiveSchemasForGivenMo(
				context, mo);

		ByteSequenceResult result = new ByteSequenceResult(
				outStream.toByteArray());
		
		result.setSuggestedFileName("rsuite_schema.zip");
		return result;

	}

	private ManagedObject validateInputParameters(
			RemoteApiExecutionContext context, CallArgumentList args)
			throws RSuiteException {
		Session session = context.getSession();
		User user = session.getUser();
		String rsuiteId = args.getFirstValue("rsuiteId");

		ManagedObjectService moService = context.getManagedObjectService();
		ManagedObject mo = null;

		if (rsuiteId != null) {
			mo = moService.getManagedObject(user, rsuiteId);
		}

		if (mo == null) {
			throw new RSuiteException("Unable to find mo with id" + rsuiteId);
		}

		if (mo.isNonXml()) {
			throw new RSuiteException("XML mo is required id: " + rsuiteId);
		}

		return mo;
	}

}
