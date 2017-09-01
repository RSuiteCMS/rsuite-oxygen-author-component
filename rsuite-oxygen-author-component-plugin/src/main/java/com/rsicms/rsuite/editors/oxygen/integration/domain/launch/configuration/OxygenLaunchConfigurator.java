package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.configuration;

import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.UserPropertiesCatalog;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenLaunchMethod;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertiesUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertyResult;

public class OxygenLaunchConfigurator {

	private static final String PROPERTY_NAME_OXYGEN_LAUNCH_TYPE = "oxygen.launch.type";

	public OxygenLaunchMethod getOxygenLaunchMethod(ExecutionContext context,
			User user) throws RSuiteException {

		OxygenLaunchMethod launchMethod = OxygenLaunchMethod.APPLET_CLASSIC;

		PluginPropertyResult launchProperty = PluginPropertiesUtils
				.getPluginProperty(context, PROPERTY_NAME_OXYGEN_LAUNCH_TYPE);
		if (launchProperty != null) {
			launchMethod = getLaunchTypeFromValue(launchProperty
					.getPropertyValue());
		}

		OxygenLaunchMethod userLaunchMethod = getUserSpecificLaunchType(
				context, user);

		if (userLaunchMethod != null) {
			launchMethod = userLaunchMethod;
		}

		return launchMethod;
	}

	private OxygenLaunchMethod getLaunchTypeFromValue(String propertyValue) {
		return OxygenLaunchMethod.valueOf(propertyValue.toUpperCase());
	}

	private OxygenLaunchMethod getUserSpecificLaunchType(
			ExecutionContext context, User user) throws RSuiteException {
		UserPropertiesCatalog userPropertiesCatalog = context.getUserService()
				.getUserPropertiesCatalog();
		Map<String, String> userProperties = userPropertiesCatalog
				.getProperties(user.getUserId());
		String userLaunchType = userProperties.get(PROPERTY_NAME_OXYGEN_LAUNCH_TYPE);

		if (userLaunchType != null) {
			return getLaunchTypeFromValue(userLaunchType);
		}

		return null;
	}
}
