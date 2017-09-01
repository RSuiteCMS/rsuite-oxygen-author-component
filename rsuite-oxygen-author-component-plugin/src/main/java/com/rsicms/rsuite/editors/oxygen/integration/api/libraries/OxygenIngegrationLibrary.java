package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import java.util.UUID;

import com.rsicms.rsuite.editors.oxygen.integration.domain.libraries.update.OxygenIntegrationJarNameUtils;

public class OxygenIngegrationLibrary {

	private String pluginId;
	
	private String path;
	
	private long size;
	
	private long crc;
	
	private String randomVersion;
	
	
	public OxygenIngegrationLibrary(String pluginId, String path, long size,
			long crc) {
		this.pluginId = pluginId;
		setUpPath(path);
		this.size = size;
		this.crc = crc;
	}



	public OxygenIngegrationLibrary(String pluginId, String path) {
		super();
		this.pluginId = pluginId;
		setUpPath(path);
	}

	private void setUpPath(String path) {
		
		this.path = path;
		
		if (!OxygenIntegrationJarNameUtils.hasJarVersion(path)){
			randomVersion = UUID.randomUUID().toString();
		}

	}

	public String getPluginId() {
		return pluginId;
	}

	public String getPath() {
		return path;
	}
	
	public String getRestPath(){
		
		if (randomVersion != null){
			return pluginId + "/" + path + "?" + randomVersion;
		}
		
		return pluginId + "/" + path;
	}
	
	public String getRestPathWithNoRandomVersion(){
		return pluginId + "/" + path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result
				+ ((pluginId == null) ? 0 : pluginId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OxygenIngegrationLibrary other = (OxygenIngegrationLibrary) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (pluginId == null) {
			if (other.pluginId != null)
				return false;
		} else if (!pluginId.equals(other.pluginId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OxygenIngegrationLibrary [pluginId=" + pluginId + ", path="
				+ path + "]";
	}


	public long getSize() {
		return size;
	}


	public long getCrc() {
		return crc;
	}
	
}
