package com.rsicms.rsuite.editors.oxygen.integration.domain.launch;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_ADVISOR_CLASS;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_ADVISOR_PRIORITY;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenIntegrationAdvisorDefault;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertiesUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertyResult;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginUtils;

public class OxygenIntegrationAdvisorFactory {

	public FindOxygenAdvisorResult findAndCreateOxygenIntegrationAdvisor(
			ExecutionContext context) throws RSuiteException {

		ClassLoader parentClassLoader = OxygenIntegrationAdvisorFactory.class
				.getClassLoader();

		String advisorRSuitePluginId = null;
		IOxygenIntegrationAdvisor advisor = null;

		PluginPropertyResult resultProperty = PluginPropertiesUtils
				.getPluginProperty(context,
						PROPERTY_OXYGEN_ADVISOR_CLASS.getProperty(),
						PROPERTY_OXYGEN_ADVISOR_PRIORITY.getProperty());

		try {

			if (resultProperty != null) {
				String pluginId = resultProperty.getPluginId();
				String className = resultProperty.getPropertyValue();

				@SuppressWarnings("rawtypes")
				Class clazz = PluginUtils.loadClassFromPlugin(context,
						parentClassLoader, pluginId, className);
				advisor = (IOxygenIntegrationAdvisor) clazz.newInstance();
				advisorRSuitePluginId = pluginId;
			}

		} catch (ClassNotFoundException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to load custom advisor", e);
		} catch (InstantiationException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to load custom advisor", e);
		} catch (IllegalAccessException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to load custom advisor", e);
		}

		if (advisor == null) {
			advisor = new OxygenIntegrationAdvisorDefault();
		}

		return new FindOxygenAdvisorResult(advisorRSuitePluginId, advisor);
	}

}
