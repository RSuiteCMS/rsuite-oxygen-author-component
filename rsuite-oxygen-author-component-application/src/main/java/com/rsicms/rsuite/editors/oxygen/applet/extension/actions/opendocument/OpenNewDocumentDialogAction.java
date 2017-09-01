package com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.OpenDocumentTreeLookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;

public class OpenNewDocumentDialogAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	private OxygenMainComponent component;

	public OpenNewDocumentDialogAction(OxygenMainComponent component){
		this.component = component;
	}
	
	public OpenNewDocumentDialogAction(String toolTipText, Icon icon,
			OxygenMainComponent component) {
		super(toolTipText, icon);
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		JFrame mainFrame = component.getParentFrame();
		ICmsCustomization cmsCustomization = component.getCmsCustomization();
		ICmsURI cmsUri = null;
		
		OpenNewDocumentAction openDocumentAction = new OpenNewDocumentAction(component);

		LookupDialog customDialog = new OpenDocumentTreeLookupDialog(mainFrame, cmsUri, cmsCustomization, openDocumentAction);
		customDialog.showDialog();
	}

}
