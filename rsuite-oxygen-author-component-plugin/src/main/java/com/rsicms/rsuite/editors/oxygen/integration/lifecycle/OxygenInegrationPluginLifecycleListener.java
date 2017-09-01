package com.rsicms.rsuite.editors.oxygen.integration.lifecycle;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.api.extensions.PluginLifecycleListener;
import com.rsicms.rsuite.editors.oxygen.integration.PluginVersion;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIntegrationLibraryManager;

public class OxygenInegrationPluginLifecycleListener implements
		PluginLifecycleListener {

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public void start(ExecutionContext context, Plugin plugin) {

		String version = plugin.getVersion();
		int index = version.indexOf("oxy");
		if (index > 0) {
			version = version.substring(index + 3);
			String[] parts = version.split(" ");
			version = parts[0];

			parts = version.split("-");

			String oxygenVersion = parts[0];
			String integrationVersion = parts[1];

			PluginVersion.setOxygenVersion(oxygenVersion);
			PluginVersion.setPluginVersion(integrationVersion);
			try {
				OxygenIntegrationLibraryManager
						.reloadOxygenPluginLibraries(plugin);
			} catch (IOException e) {
				logger.error(e, e);
			}
		}

	}

	@Override
	public void stop(ExecutionContext context, Plugin plugin) {
		// No stop-time actions.

	}

}
