package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.service.PluginManager;

public class PluginPropertiesUtils {

	private static final String PLUGIN_PROPERTIES_FILE_NAME = "plugin.properties";

	public static PluginPropertyResult getUniquePluginProperty(
			ExecutionContext context, String propertyName)
			throws RSuiteException {
		List<PluginPropertyResult> results = getPluginPropertyList(context,
				propertyName, null);

		if (results.size() > 1) {

			StringBuilder pluginList = new StringBuilder();

			for (PluginPropertyResult result : results) {
				pluginList.append(result.getPluginId()).append(" ");
			}

			throw new RSuiteException("Property " + propertyName
					+ " is defined in more than one plugin");
		}

		checkForEmptyResult(propertyName, results);

		return results.get(0);
	}

	private static void checkForEmptyResult(String propertyName,
			List<PluginPropertyResult> results) throws RSuiteException {
		if (results.size() == 0) {
			throw new RSuiteException("Property " + propertyName
					+ " is not defined");
		}
	}

	public static PluginPropertyResult getPluginProperty(
			ExecutionContext context, String propertyName)
			throws RSuiteException {
		return getPluginProperty(context, propertyName, null);
	}

	public static PluginPropertyResult getPluginProperty(
			ExecutionContext context, String propertyName,
			String priorityProperty) throws RSuiteException {

		List<PluginPropertyResult> results = getPluginPropertyList(context,
				propertyName, priorityProperty);

		Collections.sort(results);
		
		PluginPropertyResult result = null;
		
		if (results.size() > 0){
			result = results.get(results.size() -1);
		}

		return result;
	}

	public static List<PluginPropertyResult> getPluginPropertyList(
			ExecutionContext context, String propertyName,
			String priorityProperty) throws RSuiteException {

		PluginManager pluginManager = context.getPluginManager();

		List<PluginPropertyResult> results = new ArrayList<PluginPropertyResult>();

		String[] pluginIds = pluginManager.getPluginIds();

		for (String pluginId : pluginIds) {
			Plugin plugin = pluginManager.get(pluginId);

			InputStream propertyIs = plugin
					.getResourceAsStream(PLUGIN_PROPERTIES_FILE_NAME);

			try {
				if (propertyIs != null) {
					Properties props = new Properties();

					props.load(propertyIs);
					String propertyValue = props.getProperty(propertyName);

					String priorityValue = "0";

					if (priorityProperty != null) {
						priorityValue = props.getProperty(priorityProperty,
								priorityValue);

						if (!StringUtils.isNumeric(priorityValue.trim())) {
							throw new RSuiteException(
									"Invalid value for property "
											+ priorityProperty + " in plugin "
											+ pluginId
											+ ". Value must be a number");
						}
					}

					if (propertyValue != null){
						results.add(new PluginPropertyResult(pluginId, props,
								propertyName, propertyValue, priorityValue));
					}
					
				}
								
			} catch (IOException e) {
				throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
						"Unable to load properties from " + pluginId, e);
			}
		}

		return results;
	}

}
