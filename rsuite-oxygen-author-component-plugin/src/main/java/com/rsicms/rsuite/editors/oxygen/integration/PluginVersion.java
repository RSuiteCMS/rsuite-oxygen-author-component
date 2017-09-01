package com.rsicms.rsuite.editors.oxygen.integration;

public class PluginVersion {

	private static String pluginVersion;
	
	private static String oxygenVersion;

	public static String getPluginVersion() {
		return pluginVersion;
	}

	public static void setPluginVersion(String pluginVersion) {
		PluginVersion.pluginVersion = pluginVersion;
	}

	public static String getOxygenVersion() {
		return oxygenVersion;
	}

	public static void setOxygenVersion(String oxygenVersion) {
		PluginVersion.oxygenVersion = oxygenVersion;
	}
}
