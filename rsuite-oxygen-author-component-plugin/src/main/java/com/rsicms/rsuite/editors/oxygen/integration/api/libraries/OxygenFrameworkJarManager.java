package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_OVERWRITE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertiesUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertyResult;

public class OxygenFrameworkJarManager {

	public static List<OxygenIngegrationLibrary> collectJarsFromFrameworksPlugins(
			ExecutionContext context) throws RSuiteException {
		List<PluginPropertyResult> resultPropertyList = PluginPropertiesUtils
				.getPluginPropertyList(context,
						PROPERTY_OXYGEN_FRAMEWORK.getProperty(), null);

		PluginPropertyResult overwriteProperty = PluginPropertiesUtils
				.getPluginProperty(context,
						PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_OVERWRITE
								.getProperty());

		Set<String> frameworksIdsToOverwrite = getFrameworksIdsToOverwrite(overwriteProperty);

		List<OxygenIngegrationLibrary> oxygenFrameworkJars = new ArrayList<>();

		for (PluginPropertyResult property : resultPropertyList) {

			String frameworkId = property
					.getPluginProperty(PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID
							.getProperty());

			if (StringUtils.isEmpty(frameworkId)) {
				throw new RSuiteException("Missing "
						+ PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID.getProperty()
						+ " in plugin framework " + property.getPluginId());
			}

			if (frameworksIdsToOverwrite.contains(frameworkId)) {
				continue;
			}

			Plugin plugin = context.getPluginManager().get(
					property.getPluginId());

			OxygenIngegrationLibrary oxygenLibrary = OxygenIntegrationLibraryFinder
					.getOxygenIngegrationLibrary(plugin,
							property.getPropertyValue());
			oxygenFrameworkJars.add(oxygenLibrary);
		}

		return oxygenFrameworkJars;
	}

	private static Set<String> getFrameworksIdsToOverwrite(
			PluginPropertyResult overwriteProperty) {
		Set<String> frameworksIdsToOverwrite = new HashSet<String>();

		if (overwriteProperty != null) {

			for (String id : overwriteProperty.getPropertyValue().split(",")) {
				id = id.trim();
				if (StringUtils.isNotEmpty(id)) {
					frameworksIdsToOverwrite.add(id);
				}
			}
		}

		return frameworksIdsToOverwrite;
	}
}
