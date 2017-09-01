package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.repository;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="userdata")
public class UserdataElement {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlValue
	private String value;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name: ").append(name);
		sb.append("\nvalue: ").append(value);
		
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	
}
