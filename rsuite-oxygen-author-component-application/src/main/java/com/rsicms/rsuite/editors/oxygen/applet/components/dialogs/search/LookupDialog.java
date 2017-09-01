package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;

public abstract class LookupDialog extends JDialog {

	private ISelectedReferenceNode selectedRefernce;
	
	/** Uid **/
	private static final long serialVersionUID = -7812511791932890359L;

	public LookupDialog(Window window) {
		super(window, ModalityType.APPLICATION_MODAL);
		
	}
	
	public LookupDialog(Frame frame) {
		super(frame);
		
	}
	
	public ISelectedReferenceNode showDialog(){
		
		pack();
		setVisible(true);
		
		return selectedRefernce;
	}

	public void setSelectedRefernce(ISelectedReferenceNode selectedRefernce) {
		this.selectedRefernce = selectedRefernce;
	}
		
}
