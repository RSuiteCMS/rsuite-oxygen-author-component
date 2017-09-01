package com.rsicms.rsuite.editors.oxygen.applet.parameters;

public enum OxygenOpenDocumentParmatersNames {

	DOCUMENT_URI("documentUri"), BASE_URI("baseUri"), TITLE("title"), SCHEMA_SYSTEM_ID(
			"schemaSystemId"), SCHEMA_PUBLIC_ID("schemaPublicId"), SCHEMA_ID(
			"schemaId"), DITA_MAP_MANAGER("ditaMapManager", false), XPATH_START_LOCATION(
			"xpathStartLocation", false), MO_REFERENCE_ID("moReferenceId",
			false);

	private String name;

	private boolean required = true;

	private OxygenOpenDocumentParmatersNames(String name) {
		this.name = name;
	}

	private OxygenOpenDocumentParmatersNames(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
	}

	public static OxygenOpenDocumentParmatersNames fromName(String name) {

		if (name != null) {
			for (OxygenOpenDocumentParmatersNames enumValue : OxygenOpenDocumentParmatersNames
					.values()) {
				if (name.equalsIgnoreCase(enumValue.name)) {
					return enumValue;
				}
			}
		}
		return null;
	}

}
