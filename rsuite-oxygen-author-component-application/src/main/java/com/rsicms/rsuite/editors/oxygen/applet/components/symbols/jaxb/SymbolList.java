package com.rsicms.rsuite.editors.oxygen.applet.components.symbols.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="symbols")
public class SymbolList {

	List<Symbol> items = new ArrayList<Symbol>();

	@XmlElement(name = "symbol")
	public List<Symbol> getTargetElementList() {
		return items;
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (Symbol item : items){
			string.append(item).append("\n");
		}
	
		return string.toString();
	}
	
	
	
}
