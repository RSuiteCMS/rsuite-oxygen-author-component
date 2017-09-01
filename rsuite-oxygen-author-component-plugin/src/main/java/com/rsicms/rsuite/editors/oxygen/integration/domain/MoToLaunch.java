package com.rsicms.rsuite.editors.oxygen.integration.domain;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;

public class MoToLaunch {

	private ManagedObject moToLunch;
	
	private String launchXpath;
	
	private String moReferenceId;

	public MoToLaunch(ManagedObject originalMo,String xpath, String moReferenceId) {
		launchXpath = xpath;
		moToLunch = originalMo;
		this.moReferenceId = moReferenceId;
	}

	public ManagedObject getManagedObject() {
		return moToLunch;
	}

	public String getLaunchXpath() {
		return launchXpath;
	}

	public String getMoReferenceId() {
		return moReferenceId == null ? "" : moReferenceId;
	}

	public String getSchemaId(){
		return moToLunch.getSchemaInfo().getSchemaId();
	}
	
	public String getSchemaSystemId(){
		return moToLunch.getSchemaInfo().getSystemLocation();
	}
	
	public String getSchemaPublicId(){
		return moToLunch.getSchemaInfo().getPublicId();
	}
	
	public String getTitle() throws RSuiteException{
		return moToLunch.getDisplayName();
	}
	
	public String getId() throws RSuiteException{
		return moToLunch.getId();
	}
	
}
