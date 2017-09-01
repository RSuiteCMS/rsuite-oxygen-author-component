package com.rsicms.rsuite.editors.oxygen.integration.search;

import org.apache.commons.lang.StringUtils;

public class SearchXMLType implements Comparable<SearchXMLType> {

	private String name;

	private String namespaceUri;

	public SearchXMLType(String name, String namespaceUri) {
		super();
		this.name = name;
		this.namespaceUri = namespaceUri;
	}

	public String getName() {
		return name;
	}

	public String getNamespaceUri() {
		return namespaceUri;
	}

	@Override
	public int compareTo(SearchXMLType o) {
		if (o != null) {

			if (name == o.getName()) {
				return 0;
			} else if (o.getName() != null) {
				return o.getName().compareTo(name);
			}

		}
		return 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(namespaceUri)){
			sb.append(namespaceUri).append(":");
		}
		
		sb.append(name);
		
		return sb.toString();
	}
}
