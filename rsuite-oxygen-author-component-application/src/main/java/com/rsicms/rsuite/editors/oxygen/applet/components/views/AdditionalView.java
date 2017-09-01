package com.rsicms.rsuite.editors.oxygen.applet.components.views;

public enum AdditionalView {

	OUTLINE("outline", "Outline"), ATTRIBUTES("attributes", "Attributes"), ELEMENTS(
			"elements", "Elements"), REVIEW("review", "Review"), VALIDATION_PROBLEMS(
			"validationProblems", "Validation problems"), DITA_MAP_MANAGER(
			"ditaMapManager", "Dita Map Manager");

	private String id;

	private String menuDescription;

	private AdditionalView(String id, String menuDescription) {
		this.id = id;
		this.menuDescription = menuDescription;
	}

	public String getId() {
		return id;
	}

	public String getMenuDescription() {
		return menuDescription;
	}

	public static AdditionalView fromString(String text) {
		if (text != null) {
			for (AdditionalView enumVal : AdditionalView.values()) {
				if (text.equalsIgnoreCase(enumVal.id)) {
					return enumVal;
				}
			}
		}

		return null;
	}
}
