package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseDialogAction implements ActionListener {
	
	private Dialog dialog;
	
	public CloseDialogAction(Dialog dialog) {
		super();
		this.dialog = dialog;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.dispose();
	}

}
