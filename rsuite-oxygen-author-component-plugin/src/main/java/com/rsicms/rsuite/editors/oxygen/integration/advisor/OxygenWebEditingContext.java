package com.rsicms.rsuite.editors.oxygen.integration.advisor;

import java.util.HashMap;
import java.util.Map;

import com.reallysi.rsuite.api.Session;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenWebEditingContext;

public class OxygenWebEditingContext implements IOxygenWebEditingContext {

	private Session session;
	
	private String hostAddress;
	
	private String customPluginId;
	
	private Map<String, String> additionalLaunchParameters = new HashMap<String, String>();
	
	public OxygenWebEditingContext(Session session, String hostAddress, String customPluginId) {
		this.session = session;
		this.hostAddress = hostAddress;
		this.customPluginId = customPluginId;
	}
	
	public void addLaunchParamater(String name, String value){
		additionalLaunchParameters.put(name, value);
	}
		
	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public String getHostAddress() {
		return hostAddress;
	}

	@Override
	public String getCustomPluginId() {
		return customPluginId;
	}

	public void setCustomPluginId(String customPluginId) {
		this.customPluginId = customPluginId;
	}

}
