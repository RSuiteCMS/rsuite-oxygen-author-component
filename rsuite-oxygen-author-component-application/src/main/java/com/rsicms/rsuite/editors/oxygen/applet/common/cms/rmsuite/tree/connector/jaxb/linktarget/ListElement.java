package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.linktarget;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="list")
public class ListElement {

	List<LinkElementItem> items = new ArrayList<LinkElementItem>();

	@XmlElement(name = "link")
	public List<LinkElementItem> getTargetElementList() {
		return items;
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (LinkElementItem item : items){
			string.append(item).append("\n").append(" id: ").append(item.getURI());
		}
	
		return string.toString();
	}
	
	
	
}
