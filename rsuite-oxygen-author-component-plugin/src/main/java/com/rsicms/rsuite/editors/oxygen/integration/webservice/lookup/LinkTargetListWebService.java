package com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.TransformationUtils;

/**
 * Web service to find an image by name and return the binary object
 * <p>
 * This web service is used by the various content previews to display the
 * images
 * </p>
 */
public class LinkTargetListWebService implements RemoteApiHandler {

	private static final String XSL_TARGET_ELEMENT_LIST = "/xsl/xml2targetList.xsl";

	public static final String PARAM_ID = "id";

	/**
	 * Service parameter alias name of the image to display This parameter is
	 * required.
	 */
	public static final String PARAM_IMAGE_ALIAS = "alias";

	public static final String PARAM_IMAGE_ID = "id";

	/**
	 * Old misspelled initialization method for older versions of RSuite.
	 */
	public void initalize(RemoteApiDefinition def) {
		initialize(def);
	}

	/**
	 * Initialize handler.
	 */
	public void initialize(RemoteApiDefinition def) {
	}

	/**
	 * Process request.
	 * 
	 * @param context
	 *            Execution context.
	 * @param args
	 *            Service parameters provided by client.
	 */
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		try {

			String moId = args.getFirstValue(PARAM_ID);

			if (StringUtils.isBlank(moId)) {
				throw new RSuiteException("Parameter id is required");
			}

			ManagedObjectService moService = context.getManagedObjectService();
			User user = context.getSession().getUser();

			ManagedObject mo = moService.getManagedObject(user, moId);

			if (mo == null) {
				throw new RSuiteException("Mo with given id doesn't exist");
			}

			if (!mo.isOriginalMO()) {
				mo = moService.getManagedObject(user, mo.getTargetId());

			}

			String pluginId = PluginUtils.getPluginId(this.getClass());

			String xslURI = PluginUtils.constructPluginResourceUri(pluginId,
					XSL_TARGET_ELEMENT_LIST);
			String xml = TransformationUtils.transformDocument(context, mo,
					xslURI);

			return new XmlRemoteApiResult(xml);
		} catch (Throwable e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to get target list", e);
		}
	}

}
