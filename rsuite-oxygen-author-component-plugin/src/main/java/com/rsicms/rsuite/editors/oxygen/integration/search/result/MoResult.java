package com.rsicms.rsuite.editors.oxygen.integration.search.result;

import java.util.HashMap;
import java.util.Map;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.utils.MoUtils;

public class MoResult {

	private String id;
	
	private String displayName;
	
	private String dateCreated;
	
	private String dateModified;
	
	private String localName;
	
	private boolean isXml = true;
	
	private Map<String, String> aliases;
	
	private String parentId;

	public MoResult() {
	}
	
	public MoResult(ExecutionContext context, ManagedObject managedObject) throws RSuiteException {

		id = managedObject.getId();
		displayName = managedObject.getDisplayName();
		dateCreated = String.valueOf(managedObject.getDtCreated().getTime() / 1000); 
		dateModified = String.valueOf(managedObject.getDtModified().getTime() / 1000);
		localName = managedObject.getLocalName();
		isXml = !managedObject.isNonXml();
		
		aliases = new HashMap<>(); 
		
		for (Alias alias : managedObject.getAliases()){
			aliases.put(alias.getText(), alias.getType());
		}
		
		ManagedObject topMo = MoUtils.getTopManagedObject(context, managedObject.getId());
		
		if (managedObject.getId() != topMo.getId()){
			parentId = topMo.getId();
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public boolean isXml() {
		return isXml;
	}

	public void setXml(boolean isXml) {
		this.isXml = isXml;
	}

	public Map<String, String> getAliases() {
		return aliases;
	}

	public void setAliases(Map<String, String> aliases) {
		this.aliases = aliases;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}
