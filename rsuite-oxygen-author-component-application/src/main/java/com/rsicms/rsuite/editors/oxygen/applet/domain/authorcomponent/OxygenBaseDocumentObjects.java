package com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

public class OxygenBaseDocumentObjects {

	private EditorComponentProvider editorComponentProvider;

	private AuthorAccess authorAccess;
	
	private WSAuthorComponentEditorPage editorPage;

	public OxygenBaseDocumentObjects(
			EditorComponentProvider editorComponentProvider,
			AuthorAccess authorAccess, WSAuthorComponentEditorPage editorPage) {
		super();
		this.editorComponentProvider = editorComponentProvider;
		this.authorAccess = authorAccess;
		this.editorPage = editorPage;
	}

	public EditorComponentProvider getEditorComponentProvider() {
		return editorComponentProvider;
	}

	public AuthorAccess getAuthorAccess() {
		return authorAccess;
	}

	public WSAuthorComponentEditorPage getEditorPage() {
		return editorPage;
	}

	
}
