package com.rsicms.rsuite.editors.oxygen.applet.components.views;


import java.awt.BorderLayout;

import javax.swing.JPanel;


/**
 * Container for an author component additional view.
 */
@SuppressWarnings("serial")
public class OxygenAdditionalViewContainer extends JPanel {
	
	private AdditionalView viewType;
	
	
	/**
	 * Constructor.
	 * 
	 * @param title The view title.
	 * @param additionalViewComp Additional view component.
	 */
	public OxygenAdditionalViewContainer(AdditionalView viewType) {
		setLayout(new BorderLayout());
		this.viewType= viewType; 
	}


	public AdditionalView getViewType() {
		return viewType;
	}
	
	
}
