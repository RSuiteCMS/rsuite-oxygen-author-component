package com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector;

import java.util.Set;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

public class RootRepositoryResource implements IReposiotryResource {

private String displayName;
	
	private String iconName;
	
	public RootRepositoryResource(String displayName, String iconName) {
		super();
		this.displayName = displayName;
		this.iconName = iconName;
	}

	@Override
	public String getDisplayText() {
		return displayName;
	}

	@Override
	public boolean hasChilds() {
		return true;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public String getCMSid() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getCustomMetadata(String metadataName) {
		return null;
	}

	@Override
	public String getIconName() {
		return iconName;
	}

	@Override
	public Set<String> getCustomMetadataNames() {
		return null;
	}

	@Override
	public boolean isXml() {
		return false;
	}

	@Override
		public String toString() {
			return displayName;
		}

	@Override
	public String getCMSlink() {
		return null;
	}
}
