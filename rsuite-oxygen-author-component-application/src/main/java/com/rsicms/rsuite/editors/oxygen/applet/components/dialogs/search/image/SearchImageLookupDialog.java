package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.image;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JFrame;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;

public class SearchImageLookupDialog extends LookupDialog {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	public SearchImageLookupDialog(Window window , AuthorAccess authorAccess,
			InsertReferenceElement element) {
		super(window);
		intialize(authorAccess, element);
	}
	
	public SearchImageLookupDialog(JFrame mainFrame, AuthorAccess authorAccess,
			InsertReferenceElement element) {		
		super(mainFrame);

		intialize(authorAccess, element);
	}

	private void intialize(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		add(new SearchImagePanel(this, authorAccess, element));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Search Image");

		setModal(true);	
		setPreferredSize(new Dimension(1200, 650));
		setLocation(100, 100);
	}

}
