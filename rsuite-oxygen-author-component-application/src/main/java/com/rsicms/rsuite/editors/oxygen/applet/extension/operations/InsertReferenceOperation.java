package com.rsicms.rsuite.editors.oxygen.applet.extension.operations;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertReferenceElementAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.LookupAction;

public abstract class InsertReferenceOperation implements AuthorOperation {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void doOperation(AuthorAccess authorAccess, ArgumentsMap argumentsMap)
			throws IllegalArgumentException, AuthorOperationException {

		try {

			InsertReferenceElement element = createInsertReferenceElement(authorAccess);

			if (canInsertElement(authorAccess, element)) {

				LookupAction action = new LookupAction(authorAccess, element);
				ISelectedReferenceNode selectReference = action
						.selectReference();

				if (selectReference != null){
				
					InsertReferenceElementAction insertAction = new InsertReferenceElementAction(
							authorAccess, element);
					insertAction.insertReferenceElement(selectReference);
				}				
			}

		} catch (RuntimeException e) {
			OxygenUtils.handleExceptionUI(logger, e);
		} catch (OxygenIntegrationException e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}
	}

	public boolean canInsertElement(AuthorAccess authorAccess,
			InsertReferenceElement element) {

		boolean canInsertInContext = OxygenUtils
				.checkIfCanInsertElementInCurrentContext(authorAccess, element);

		boolean canInsert = true;

		if (!canInsertInContext && element.isValidateInsertContext()) {
			authorAccess.getWorkspaceAccess().showInformationMessage(
					"Unable to insert " + element.getElementName()
							+ " element in this context");

			canInsert = false;
		}

		return canInsert;
	}

	@Override
	public ArgumentDescriptor[] getArguments() {
		return null;
	}

	protected abstract InsertReferenceElement createInsertReferenceElement(
			AuthorAccess authorAccess);
}
