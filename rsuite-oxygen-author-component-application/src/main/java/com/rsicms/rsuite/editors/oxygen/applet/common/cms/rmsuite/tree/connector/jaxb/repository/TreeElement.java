package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.repository;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="tree")
public class TreeElement {

	@XmlElement(name = "item")
	List<TreeItemElement> items = new ArrayList<TreeItemElement>();

	
	public List<TreeItemElement> getInfoList() {
		return items;
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (TreeItemElement item : items){
			string.append(item).append("\n").append(" x: ").append(item.getCMSid());
		}
	
		return string.toString();
	}
	
	
	
}
