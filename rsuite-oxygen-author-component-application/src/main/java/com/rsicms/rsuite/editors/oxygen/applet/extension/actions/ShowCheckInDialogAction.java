package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.CheckInDialog;

public class ShowCheckInDialogAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	private SaveAndCheckInAction action;
	
	private OxygenMainComponent component;

	public ShowCheckInDialogAction(String toolTipText, Icon icon,
			OxygenMainComponent component) {
		super(toolTipText, icon);
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

//		OxygenDocument document = component.getActiveDocumentComponent();
//		AuthorAccess authorAccess = document.getAuthorAccess();
//		
//		OxygenEditorContext context = OxygenComponentRegister
//				.getRegisterOxygenEditorContext(authorAccess);
		
		
		action = new SaveAndCheckInAction(component);
		JDialog customDialog = new CheckInDialog(component.getParentFrame(),
				"Save and check in", action);
		customDialog.pack();
		customDialog.setVisible(true);
	}

}
