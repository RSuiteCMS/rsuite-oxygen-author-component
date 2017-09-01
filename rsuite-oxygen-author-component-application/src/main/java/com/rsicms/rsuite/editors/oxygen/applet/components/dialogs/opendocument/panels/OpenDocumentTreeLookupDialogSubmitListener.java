package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.apache.commons.lang.StringUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentAction;

public class OpenDocumentTreeLookupDialogSubmitListener implements ActionListener {

	private JDialog parentDialog;

	private OpenNewDocumentAction openDocumentAction;
	
	private ISelectableComponent selectableComponent;
	
	public OpenDocumentTreeLookupDialogSubmitListener(JDialog parentDialog, ISelectableComponent selectableComponent, OpenNewDocumentAction openDocumentAction) {
		this.parentDialog = parentDialog;
		this.openDocumentAction = openDocumentAction;
		this.selectableComponent = selectableComponent;
	}


	@Override
	public void actionPerformed(ActionEvent e) {		
		
		ISelectedReferenceNode selectedReferenceNode = selectableComponent.getSelectedReferenceNode();
		
		if (!selectedReferenceNode.getRepositoryResource().isXml()){
			OxygenMainComponent.getCurrentInstance().getDialogHelper().showWarningMessage("Unable to open the MO. The selected MO is a not XML");
			return;
		}
		
		String checkedOutBy = selectedReferenceNode.getRepositoryResource().getCustomMetadata("checkedOutBy");
		
		if (StringUtils.isNotBlank(checkedOutBy)){
			OxygenMainComponent.getCurrentInstance().getDialogHelper().showWarningMessage("Unable to open the MO. The selected MO is already checked out by  " + checkedOutBy);
			return;
		}
		
		parentDialog.dispose();
		
		openDocumentAction.openNewDocument(selectedReferenceNode.getRepositoryResource());		
	}

}
