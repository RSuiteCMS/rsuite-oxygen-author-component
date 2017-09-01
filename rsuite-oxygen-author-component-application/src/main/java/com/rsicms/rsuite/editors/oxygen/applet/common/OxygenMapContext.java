package com.rsicms.rsuite.editors.oxygen.applet.common;

import javax.swing.JFrame;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDitaMapDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;

public class OxygenMapContext {

	private JFrame mainFrame;
	
	private OxygenMainComponent mainComponent;
	
	private OxygenDitaMapDocument mapDocument;
	
	public OxygenMapContext(OxygenMainComponent mainComponent, OxygenDitaMapDocument mapDocument) {
		super();
		this.mainFrame = mainComponent.getParentFrame();
		this.mainComponent = mainComponent;
		
		this.mapDocument = mapDocument;
	}

	public JFrame getMainFrame(){
		return mainFrame;
	}

	public OxygenMainComponent getMainComponent() {
		return mainComponent;
	}

	public OxygenDitaMapDocument getMapDocument() {
		return mapDocument;
	}
		
}
