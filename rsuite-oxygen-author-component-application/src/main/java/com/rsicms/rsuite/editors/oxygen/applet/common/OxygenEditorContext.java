package com.rsicms.rsuite.editors.oxygen.applet.common;

import javax.swing.JFrame;

import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.listeners.CaretListener;

public class OxygenEditorContext {

	private JFrame mainFrame;
	
	private OxygenMainComponent mainComponent;
	
	private CaretListener caretListener; 
	
	private OxygenDocument documentComponent;
	
	public OxygenEditorContext(OxygenMainComponent mainComponent, OxygenDocument documentComponent, CaretListener caretListener) {
		super();
		this.mainFrame = mainComponent.getParentFrame();
		this.mainComponent = mainComponent;
		this.caretListener = caretListener;
		this.documentComponent = documentComponent;
	}

	public JFrame getMainFrame(){
		return mainFrame;
	}

	public OxygenMainComponent getMainComponent() {
		return mainComponent;
	}
	
	public AuthorNode getPreviousNode(){
		return caretListener.getPreviousNode();
	}
	
	public AuthorElement getPreviousCrossRefElement(){
		return caretListener.getPreviousCrossRefElement();
	}

	public OxygenDocument getDocumentComponent() {
		return documentComponent;
	}
	
}
