package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.appletclassic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.rsicms.rsuite.editors.oxygen.integration.PluginVersion;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenIntegrationAdvisorLibraryListCreator;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.domain.PluginWebContentManager;

class ClassicAppletTransformParamGenerator {

	private static final String TRANSFORM_PARAM_UUID = "uuid";

	private static final String TRANSFORM_PARAM_RSUITE_SCHEMA_PUBLIC_ID = "rsuite.schemaPublicId";

	private static final String TRANSFORM_PARAM_RSUITE_SCHEMA_SYSTEM_ID = "rsuite.schemaSystemId";

	private static final String TRANSFORM_PARAM_RSUITE_CUSTOMIZATION_CLASS = "rsuite.customization.class";

	private static final String TRANSFORM_PARAM_RSUITE_JAR_LIST = "rsuite.jar.list";

	private static final String TRANSFORM_PARAM_RSUITE_CUSTOM_ARGUMENTS = "rsuite.custom.arguments";

	private static final String TRANSFORM_PARAM_RSUITE_TITLE = "rsuite.title";

	private static final String TRANSFORM_PARAM_RSUITE_SCHEMA_ID = "rsuite.schemaId";

	private static final String TRANSFORM_PARAM_DOCUMENTURI = "rsuite.documenturi";

	private static final String TRANSFORM_PARAM_RSUITE_PROJECT = "rsuite.project";

	private static final String TRANSFORM_PARAM_RSUITE_USERNAME = "rsuite.username";

	private static final String TRANSFORM_PARAM_RSUITE_SESSIONKEY = "rsuite.sessionkey";

	private static final String TRANSFORM_PARAM_RSUITE_SERVERURL = "rsuite.serverurl";

	private static final String TRANSFORM_PARAM_BASE_OXYGEN_URL = "rsuite.base.oxygen.url";

	private static final String TRANSFORM_PARAM_XPATH_START_LOCATION = "rsuite.xpathStartLocation";

	private static final String TRANSFORM_PARAM_APPLET_VERSION = "applet.version";

	private static final String TRANSFORM_PARAM_OXYGEN_VERSION = "oxygen.version";

	public static Map<String, String> createTransformParameters(
			OxygenIntegrationAdvisorLibraryListCreator libraryList,
			PluginWebContentManager pluginManager,
			IOxygenWebEditingContext webContext,
			IOxygenIntegrationAdvisor advisor, MoToLaunch moToLunch)
			throws RSuiteException {

		Map<String, String> transformParams = new HashMap<String, String>();

		String serverUrl = webContext.getHostAddress();
		Session session = webContext.getSession();

		transformParams.putAll(createDocumentParameters(webContext, moToLunch));

		transformParams.put(
				TRANSFORM_PARAM_BASE_OXYGEN_URL,
				serverUrl + OxygenConstants.RSUITE_REST_STATIC
						+ pluginManager.getPluginId());
		transformParams.put(TRANSFORM_PARAM_RSUITE_SERVERURL, serverUrl);
		transformParams
				.put(TRANSFORM_PARAM_RSUITE_SESSIONKEY, session.getKey());
		transformParams.put(TRANSFORM_PARAM_RSUITE_USERNAME, session.getUser()
				.getUserId());

		transformParams.put(TRANSFORM_PARAM_RSUITE_PROJECT,
				advisor.getProjectName());

//		transformParams.put(TRANSFORM_PARAM_RSUITE_APPLET_MODE,
//				String.valueOf(webContext.isAppletMode()));

		transformParams.put(TRANSFORM_PARAM_APPLET_VERSION,
				PluginVersion.getPluginVersion());
		transformParams.put(TRANSFORM_PARAM_OXYGEN_VERSION,
				PluginVersion.getOxygenVersion());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String uuid = sdf.format(new Date());

		transformParams.put(TRANSFORM_PARAM_UUID, uuid);

		// OxygenIntegrationAdvisorLibraryListCreator libraryList = new
		// OxygenIntegrationAdvisorLibraryListCreator(
		// context);
		String jarsList = libraryList.createOxygenJarsList(webContext,
				advisor.getCustomJars(), advisor.getOptionalFeatures());

		transformParams.put(TRANSFORM_PARAM_RSUITE_JAR_LIST, jarsList);

		if (advisor.getOxygenCustomizationClass() != null) {
			transformParams.put(TRANSFORM_PARAM_RSUITE_CUSTOMIZATION_CLASS,
					advisor.getOxygenCustomizationClass());
		}

		if (advisor.getCustomRunArguments() != null) {

			transformParams.put(TRANSFORM_PARAM_RSUITE_CUSTOM_ARGUMENTS,
					StringUtils.join(advisor.getCustomRunArguments(), ';'));
		}

		return transformParams;
	}

	private static Map<? extends String, ? extends String> createDocumentParameters(
			IOxygenWebEditingContext webContext, MoToLaunch moToLunch) throws RSuiteException {

		Map<String, String> transformParams = new HashMap<String, String>();
		String serverUrl = webContext.getHostAddress();
		Session session = webContext.getSession();

		ManagedObject mo = moToLunch.getManagedObject();
		String schemaId = mo.getSchemaInfo().getSchemaId();
		String schemaSystemId = mo.getSchemaInfo().getSystemLocation();
		String schemaPublicId = mo.getSchemaInfo().getPublicId();

		String moId = mo.getId();

		String documentUri = serverUrl + OxygenConstants.RSUITE_REST_CONTENT
				+ moId + "?skey=" + session.getKey() + "&includeSchema=true";
		transformParams.put(TRANSFORM_PARAM_DOCUMENTURI, documentUri);

		if (moToLunch.getTitle() != null) {
			transformParams.put(TRANSFORM_PARAM_RSUITE_TITLE,
					moToLunch.getTitle());
		}
		
		if (StringUtils.isNotBlank(moToLunch.getLaunchXpath())) {
			transformParams.put(TRANSFORM_PARAM_XPATH_START_LOCATION,
					moToLunch.getLaunchXpath());
		}

		transformParams.put(TRANSFORM_PARAM_RSUITE_SCHEMA_ID, schemaId);
		transformParams.put(TRANSFORM_PARAM_RSUITE_SCHEMA_PUBLIC_ID,
				schemaPublicId);
		transformParams.put(TRANSFORM_PARAM_RSUITE_SCHEMA_SYSTEM_ID,
				schemaSystemId);

		return transformParams;
	}

}
