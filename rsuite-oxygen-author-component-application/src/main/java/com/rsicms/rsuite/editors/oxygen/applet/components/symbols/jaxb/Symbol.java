package com.rsicms.rsuite.editors.oxygen.applet.components.symbols.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "symbol")
public class Symbol  {


	private String displayName;
	
	private String value;
	
	private boolean isXML;
	
	
	public String getDisplayName() {
		return displayName == null ? value : displayName;
	}

	@XmlAttribute(name = "display")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	public String getValue() {
		return value;
	}

	@XmlValue
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		
		return getValue();
	}

	public boolean isXML() {
		return isXML;
	}

	@XmlAttribute(name = "xml")
	public void setXML(boolean isXML) {
		this.isXML = isXML;
	}
	
	
}
