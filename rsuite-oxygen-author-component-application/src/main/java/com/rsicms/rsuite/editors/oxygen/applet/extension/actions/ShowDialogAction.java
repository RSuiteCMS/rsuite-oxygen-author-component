package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;

public class ShowDialogAction extends AbstractAction {


	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	private OxygenMainComponent component;
	
	private final JDialog dialog;
	
	public ShowDialogAction(String toolTipText, Icon icon,
			OxygenMainComponent component, JDialog dialog) {
		super(toolTipText, icon);
		this.component = component;
		this.dialog = dialog;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent event) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		
	}

	


}
