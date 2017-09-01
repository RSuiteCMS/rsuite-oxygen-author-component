package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.linktarget;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;

@XmlRootElement(name = "link")
public class LinkElementItem implements IReferenceTargetElement {


	private String uri;
	
	private String title;
	
	private String elementName;
	
	private String targetId;
	
	private String editedResourceId;

	public String getURI() {
		if (editedResourceId != null && uri.startsWith(editedResourceId) && uri.indexOf('#') > -1){
			return uri.substring(uri.indexOf('#'));
		}
		
		return uri;
	}

	@XmlAttribute(name = "ID")
	public void setURI(String uri) {
		this.uri = uri;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@XmlAttribute(name = "title")
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getElementName() {
		return elementName;
	}

	@XmlAttribute(name = "element")
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	@Override
	public String getTargetId() {
		return targetId;
	}

	@XmlAttribute(name="targetId")
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(elementName).append(" [");
		sb.append(targetId).append("]");
		
		return  sb.toString();
	}

	public void setEditedResourceId(String editedResourceId) {
		this.editedResourceId = editedResourceId;
	}

	
	
	
	
}
