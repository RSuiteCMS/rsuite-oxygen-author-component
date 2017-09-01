package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class OpenDocumentTreeLookupDialogCancelListener implements ActionListener {

	private JDialog parentDialog;

	public OpenDocumentTreeLookupDialogCancelListener(JDialog parentDialog) {
		this.parentDialog = parentDialog;
	}


	@Override
	public void actionPerformed(ActionEvent e) {		
		parentDialog.dispose();		
	}

}
