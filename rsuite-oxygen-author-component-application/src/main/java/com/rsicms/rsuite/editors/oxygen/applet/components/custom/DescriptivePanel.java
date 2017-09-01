package com.rsicms.rsuite.editors.oxygen.applet.components.custom;

import javax.swing.JPanel;

public class DescriptivePanel {

	private String title;
	
	private String toolTiptext;
	
	private JPanel panel;

	public DescriptivePanel(String title, String toolTiptext, JPanel panel) {
		super();
		this.title = title;
		this.toolTiptext = toolTiptext;
		this.panel = panel;
	}

	public String getTitle() {
		return title;
	}

	public String getToolTiptext() {
		return toolTiptext;
	}

	public JPanel getPanel() {
		return panel;
	}
	

}
