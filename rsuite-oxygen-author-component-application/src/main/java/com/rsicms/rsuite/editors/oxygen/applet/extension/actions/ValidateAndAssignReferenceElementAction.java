package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang.StringUtils;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.access.AuthorWorkspaceAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;

public class ValidateAndAssignReferenceElementAction implements ActionListener {

	private LookupDialog lookupDialog;

	private InsertReferenceElement element;

	private ISelectableComponent lookupComponent;

	private AuthorWorkspaceAccess workspace;

	public ValidateAndAssignReferenceElementAction(AuthorAccess authorAccess,
			LookupDialog lookupDialog, InsertReferenceElement element,
			ISelectableComponent lookupComponent) {
		this.lookupDialog = lookupDialog;
		this.element = element;
		this.lookupComponent = lookupComponent;
		workspace = authorAccess.getWorkspaceAccess();
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		ISelectedReferenceNode targetNode = lookupComponent
				.getSelectedReferenceNode();
		if (targetNode != null){
			validateReference(targetNode);
		}
	}

	public void validateReference(ISelectedReferenceNode selectedReferenceNode) {

		if (selectedReferenceNode != null) {

			String linkValue = selectedReferenceNode.getLinkValue(element
					.getReferenceHandler());
			IReferenceTargetElement targetElement = selectedReferenceNode
					.getReferenceTargetElement();

			if (StringUtils.isEmpty(linkValue)) {
				workspace
						.showErrorMessage("Attribute link value cannot be empty");
				return;
			}

			if (element.isConfRef() && targetElement == null) {
				workspace
						.showErrorMessage("For conref you must select a target element");
				return;
			}

			lookupDialog.setSelectedRefernce(selectedReferenceNode);
			lookupDialog.dispose();

		} else {
			workspace.showErrorMessage("Incorrect element has been selected");
		}

	}
}
