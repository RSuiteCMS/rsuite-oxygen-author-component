package com.rsicms.rsuite.editors.oxygen.integration.api.common;

public interface OxygenConstants {


	String RSUITE_OXYGEN_PLUGIN_NAME = "rsuite-oxygen-applet-integration";
	
	String RSUITE_RESOURSE_PLUGIN_PREFIX = "rsuite:/res/plugin/" + RSUITE_OXYGEN_PLUGIN_NAME + "/";
	
	String APPLET_TAG_XSLT_PATH = RSUITE_RESOURSE_PLUGIN_PREFIX + "editor/applet.classic.html.tag.xsl";
	
	String RSUITE_REST_CONTENT = "/rsuite/rest/v2/content/binary/id/";
	
	String RSUITE_REST_STATIC = "/rsuite/rest/v2/static/";

	String TRANSFORM_PARAM_RSUITE_CUSTOM_JARS = "rsuite.custom.jars";
	
}
