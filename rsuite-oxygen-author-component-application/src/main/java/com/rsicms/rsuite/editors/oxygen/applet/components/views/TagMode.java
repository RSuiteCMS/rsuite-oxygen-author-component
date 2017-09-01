package com.rsicms.rsuite.editors.oxygen.applet.components.views;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public enum TagMode {

	NO_TAGS("No tags", "Author/No_tags"), PARTIAL_TAGS("Partial tags",
			"Author/Partial_tags"), FULL_TAGS("Full tags", "Author/Full_tags"), FULL_TAGS_WITH_ATTRIBUTES(
			"Full tags with attributes", "Author/Full_tags_with_attributes");

	private String oxygenActionName;

	private String menuDescription;

	private TagMode(String menuDescription, String oxygenActionName) {
		this.oxygenActionName = oxygenActionName;
		this.menuDescription = menuDescription;
	}

	public String getOxygenActionName() {
		return oxygenActionName;
	}

	public String getMenuDescription() {
		return menuDescription;
	}

	public static TagMode fromMenuName(String menuName) throws OxygenIntegrationException {
		menuName = menuName.toLowerCase().trim().replaceAll("\\s+", "_");

		if (equal(NO_TAGS, menuName)) {
			return NO_TAGS;
		} else if (equal(PARTIAL_TAGS, menuName)) {
			return PARTIAL_TAGS;
		} else if (equal(FULL_TAGS,menuName)) {
			return FULL_TAGS;
		} else if	 (equal(FULL_TAGS_WITH_ATTRIBUTES,menuName)) {
			return FULL_TAGS_WITH_ATTRIBUTES;
		}
		
		throw new OxygenIntegrationException("Name " +  menuName + " is not supported"); 
	}

	private static boolean equal(TagMode mode, String name){
		return mode.toString().equalsIgnoreCase(name);
	}
}
