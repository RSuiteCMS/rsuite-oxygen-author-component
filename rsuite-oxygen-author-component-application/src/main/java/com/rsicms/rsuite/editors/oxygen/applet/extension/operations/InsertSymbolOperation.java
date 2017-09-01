package com.rsicms.rsuite.editors.oxygen.applet.extension.operations;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertSymbolAction;

public class InsertSymbolOperation implements AuthorOperation {

	@Override
	public String getDescription() {		
		return "Insert image";
	}

	@Override
	public void doOperation(AuthorAccess authorAccess, ArgumentsMap argumentsMap)
			throws IllegalArgumentException, AuthorOperationException {
 		InsertSymbolAction action = new InsertSymbolAction(authorAccess);
 		action.actionPerformed(null);
	}

	@Override
	public ArgumentDescriptor[] getArguments() {
		return null;
	}

}
