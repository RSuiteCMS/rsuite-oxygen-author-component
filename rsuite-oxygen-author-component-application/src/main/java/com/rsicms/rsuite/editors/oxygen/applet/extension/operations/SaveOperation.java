package com.rsicms.rsuite.editors.oxygen.applet.extension.operations;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;

public class SaveOperation implements AuthorOperation {

	@Override
	public String getDescription() {		
		return "Save document";
	}

	@Override
	public void doOperation(AuthorAccess authorAccess, ArgumentsMap argumentsMap)
			throws IllegalArgumentException, AuthorOperationException {
		
		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);
		
		context.getMainComponent().saveActiveDocument();		 		
	}

	@Override
	public ArgumentDescriptor[] getArguments() {
		return null;
	}

}
