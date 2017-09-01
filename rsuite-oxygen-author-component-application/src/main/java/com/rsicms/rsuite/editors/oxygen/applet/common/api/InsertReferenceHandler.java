package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;


public interface InsertReferenceHandler {

	public String getLinkValue(ISelectedReferenceNode selectedNode, String linkValue);
	
	public String getXmlFragment(ISelectedReferenceNode selectedNode, String defaultXmlFragment);
	
	public void beforeInsertFragment(AuthorAccess paramAuthorAccess, InsertReferenceElement refenceElement, ISelectedReferenceNode selectedNode) throws AuthorOperationException;
	
	public void afterInsertFragment(AuthorAccess paramAuthorAccess,  InsertReferenceElement refenceElement, ISelectedReferenceNode selectedNode) throws AuthorOperationException;
}
