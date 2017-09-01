package com.rsicms.rsuite.editors.oxygen.integration.webservice.save;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.helpers.utils.MoUtils;

/**
 * Save document in RSuite
 * 
 */
public class SaveWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(SaveWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String moId = args.getFirstString("moId");
		FileItem file = args.getFirstFile(moId + ".xml");

		if (moId != null) {
			moId = MoUtils.getRealMo(context, moId).getId();
		}

		ManagedObjectService managedObjectService = context
				.getManagedObjectService();
		User user = context.getSession().getUser();

		String resultContent = "<ok />";

		try {
			validateParameters(user, managedObjectService, moId, file);
			updateMo(user, managedObjectService, moId, file);
		} catch (RSuiteException e) {
			log.error(e, e);
			resultContent = createErrorResponse(e.getMessage());
		}

		return createResultObject(resultContent);

	}

	private XmlRemoteApiResult createResultObject(String resultContent) {
		XmlRemoteApiResult result = new XmlRemoteApiResult(resultContent);
		result.setContentType("text/xml");
		return result;
	}

	private void updateMo(User user, ManagedObjectService managedObjectService,
			String moId, FileItem file) throws RSuiteException {
		XmlObjectSource xmlObjectSource = new XmlObjectSource(file.get(),
				"utf-8");
		managedObjectService.update(user, moId, xmlObjectSource,
				new ObjectUpdateOptions());
	}

	private void validateParameters(User user,
			ManagedObjectService managedObjectService, String moId,
			FileItem file) throws RSuiteException {

		if (moId == null) {
			throw new RSuiteException(
					"moId web service argument is not present");
		}

		if (file == null) {
			throw new RSuiteException(
					"mo file content web service argument is not present");
		}

		ManagedObject managedObject = managedObjectService.getManagedObject(
				user, moId);

		if (managedObject == null) {
			throw new RSuiteException("MO with id " + moId + " doesn't exist");
		}

		if (!managedObject.isCheckedoutAuthor(user)) {
			throw new RSuiteException("MO is not checked out by "
					+ user.getUserId());
		}

	}

	private String createErrorResponse(String message) {
		StringBuilder errorResponse = new StringBuilder("<error><message>");
		errorResponse.append(message);
		errorResponse.append("</message></error>");
		return errorResponse.toString();
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
