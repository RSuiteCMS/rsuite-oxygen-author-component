package com.rsicms.rsuite.editors.oxygen.applet.common.references;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;

public class InsertReferenceHandlerDefault implements InsertReferenceHandler {

	@Override
	public String getXmlFragment(ISelectedReferenceNode selectedNode, String defaulXmlFragment) {
		return defaulXmlFragment;
	}
	
	@Override
	public String getLinkValue(ISelectedReferenceNode selectedNode, String linkValue) {
		return linkValue;
	}

	@Override
	public void beforeInsertFragment(AuthorAccess paramAuthorAccess,
			InsertReferenceElement refenceElement,
			ISelectedReferenceNode selectedNode)
			throws AuthorOperationException {
	}

	@Override
	public void afterInsertFragment(AuthorAccess paramAuthorAccess,
			InsertReferenceElement refenceElement,
			ISelectedReferenceNode selectedNode)
			throws AuthorOperationException {
	}
	
}


