package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

@XmlRootElement(name = "item")
public class TreeItemElement implements IReposiotryResource {

	@XmlAttribute(name = "text")
	private String displayText;

	private boolean hasChild;

	@XmlAttribute(name = "id")
	private String id;

	private String rsuiteId;

	private boolean isContainer;
	
	private boolean isXml;
	
	private String iconName;
	
	private boolean contextDocument;
	
	private String resourceType;
	
	private List<UserdataElement> userDataList = new ArrayList<UserdataElement>();

	private Map<String, String> userDataMap = new HashMap<String, String>();

	public List<UserdataElement> getUserData() {
		return userDataList;
	}
	
	@XmlElement(name = "userdata")
	public void setUserData(List<UserdataElement> userDataList) {
		this.userDataList = userDataList;
	}

	@Override
	public String toString() {
		return displayText;
	}

	@XmlAttribute(name = "child")
	public void setChild(String child) {

		if (child != null && Integer.parseInt(child) > 0) {
			hasChild = true;
		}
	}

	@XmlAttribute(name = "im0")
	public void setIconName(String iconName) {
		this.iconName= iconName;
	}

	
	
	public String getResourceType() {
		return resourceType;
	}

	@XmlAttribute(name = "type")
	public void setResourceType(String resourceType) {
		if (resourceType !=null){
			String type = resourceType.toLowerCase();
			this.resourceType = resourceType.toLowerCase();
			
			if (type.indexOf("managed") == -1){
				isContainer = true;
			}else if (resourceType.indexOf("NONXML") == -1){
				isXml = true;
			}
		}
		
	}

	@Override
	public String getDisplayText() {
		return displayText;
	}

	@Override
	public boolean hasChilds() {
		return hasChild;
	}

	@Override
	public boolean isContainer() {
		return isContainer;
	}

	@Override
	public String getCMSid() {
		if (rsuiteId == null) {
			rsuiteId = id;
			for (UserdataElement userData : userDataList) {
				if (userData.getName().equalsIgnoreCase("rsuiteId")) {
					rsuiteId = userData.getValue();
					break;
				}
			}
		}

		return rsuiteId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getCustomMetadata(String metadataName) {
		String value = userDataMap.get(metadataName);			
		
		if (!userDataMap.containsKey(metadataName)){
			for (UserdataElement userData : userDataList) {
				
				if (userData.getName().equalsIgnoreCase(metadataName)) {
					value = userData.getValue();
					break;
				}
			}
			if (value != null){
				userDataMap.put(metadataName, value);
			}
			
		}
		
		return value;
	}

	@Override
	public String getIconName() {		
		return iconName;
	}

	@Override
	public Set<String> getCustomMetadataNames() {

		if (!userDataList.isEmpty()){
			for (UserdataElement userData : userDataList) {
				userDataMap.put(userData.getName(), userData.getValue());	
			}
		}
		
		return userDataMap.keySet();
	}

	public boolean isContextDocument() {
		return contextDocument;
	}

	public void setContextDocument(boolean contextDocument) {
		this.contextDocument = contextDocument;
	}

	@Override
	public boolean isXml() {
		return isXml;
	}

	@Override
	public String getCMSlink() {
		
		String type = "element";
		
		if (!isXml){
			type = "binary";
		}
		return "/rsuite/rest/v2/content/" + type + "/alias/" + getAlias();
	}
	
	private String getAlias(){
		String temp = getCustomMetadata("aliases");
		String alias = temp.split(",")[0];
		return alias;
	}

}
