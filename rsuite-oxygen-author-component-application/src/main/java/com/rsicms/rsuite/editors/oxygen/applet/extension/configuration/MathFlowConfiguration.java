package com.rsicms.rsuite.editors.oxygen.applet.extension.configuration;

public class MathFlowConfiguration {

	private String editorLicenseKey;
	
	private String composerLicenseKey;

	public MathFlowConfiguration(String editorLicenseKey, String composerLicenseKey) {
		super();
		this.editorLicenseKey = editorLicenseKey;
		this.composerLicenseKey = composerLicenseKey;
	}

	public String getEditorLicenseKey() {
		return editorLicenseKey;
	}

	public String getComposerLicenseKey() {
		return composerLicenseKey;
	}

}
