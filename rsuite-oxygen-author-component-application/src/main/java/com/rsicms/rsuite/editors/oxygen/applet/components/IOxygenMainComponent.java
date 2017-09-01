package com.rsicms.rsuite.editors.oxygen.applet.components;

import javax.swing.JFrame;

public interface IOxygenMainComponent {

	IOxygenDocument getDocumentComponent(int i);
	
	JFrame getParentFrame();
	
	void saveActiveDocument();
	
	void closeDocumentFromTab(int tabIndex);
	
}
