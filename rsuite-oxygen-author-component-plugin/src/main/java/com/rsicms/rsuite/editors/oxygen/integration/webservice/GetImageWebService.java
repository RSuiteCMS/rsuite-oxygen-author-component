package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VariantDescriptor;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.RemoteBinaryStreamResult;

/**
 * Web service to find an image by name and return the binary object
 * <p>
 * This web service is used by the various content previews to display the
 * images
 * </p>
 */
public class GetImageWebService implements RemoteApiHandler {
	private static Log log = LogFactory.getLog(GetImageWebService.class);

	/**
	 * Service parameter alias name of the image to display This parameter is
	 * required.
	 */
	public static final String PARAM_IMAGE_ALIAS = "image";

	public static final String PARAM_IMAGE_ID = "imageId";

	/**
	 * Service parameter image image to display This parameter is optional.
	 */
	public static final String PARAM_VARIANT = "variant";

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
		logArgs(args);

		try {
			String imageAlias = args.getFirstValue(PARAM_IMAGE_ALIAS);
			String variant = args.getFirstValue(PARAM_VARIANT);
			String imageId = args.getFirstValue(PARAM_IMAGE_ID);

			ManagedObject imageMo = getImageMo(context, imageAlias, imageId);

			return new RemoteBinaryStreamResult(getImageStream(context,
					imageMo, variant));
		} catch (Throwable e) {
			log.error(e.getLocalizedMessage(), e);
			return new MessageDialogResult(
					MessageType.ERROR, "Image Error",
					e.getLocalizedMessage() + ".");
		}
	}

	private ManagedObject getImageMo(RemoteApiExecutionContext context,
			String imageAlias, String imageId) throws RSuiteException {

		User user = context.getSession().getUser();
		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject mo = null;

		if (!StringUtils.isBlank(imageAlias)) {

			mo = moSvc.getObjectByAlias(user, imageAlias);
			if (mo == null) {
				throw new RSuiteException("No image found with name == "
						+ imageAlias);
			}

			return mo;
		}

		if (!StringUtils.isBlank(imageId)) {

			mo = moSvc.getManagedObject(user, imageId);
			if (mo == null) {
				throw new RSuiteException("No image found with id == "
						+ imageId);
			}

			if (!mo.isOriginalMO()) {
				mo = moSvc.getManagedObject(user, mo.getTargetId());
			}

			return mo;

		}

		throw new RSuiteException("Please specify image alias or id");

	}

	private InputStream getImageStream(RemoteApiExecutionContext context,
			ManagedObject mo, String variant) throws RSuiteException {
		InputStream imageInputStream = null;

		if (variant != null) {
			VariantDescriptor[] variants = mo.getVariants();
			for (VariantDescriptor variantDesc : variants) {
				if (variant.equalsIgnoreCase(variantDesc.getVariantName())) {
					imageInputStream = new ByteArrayInputStream(
							variantDesc.getContent());
					break;
				}
			}
		}

		if (imageInputStream == null) {
			imageInputStream = mo.getInputStream();
		}

		return imageInputStream;
	}

	/**
	 * Log service arguments.
	 * 
	 * @param args
	 *            Argument list.
	 */
	private static void logArgs(CallArgumentList args) {
		log.info("Remote API Arguments:");
		for (String name : args.getNames()) {
			log.info("[" + name + "] " + args.getValues(name));
		}
	}
}
