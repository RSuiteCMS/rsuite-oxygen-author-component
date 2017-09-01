package com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector;

import java.util.Set;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

public class EditedRepositoryResource implements IReposiotryResource {

	private String editedResourceId;
	
	public EditedRepositoryResource(String editedResourceId) {
		this.editedResourceId = editedResourceId;
	}
	
	@Override
	public String getDisplayText() {
		return null;
	}

	@Override
	public boolean hasChilds() {
		return false;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public String getCMSid() {
		return editedResourceId;
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
		return null;
	}

	@Override
	public Set<String> getCustomMetadataNames() {
		return null;
	}

	@Override
	public boolean isXml() {
		return true;
	}

	@Override
	public String getCMSlink() {
		// TODO Auto-generated method stub
		return null;
	}

}
