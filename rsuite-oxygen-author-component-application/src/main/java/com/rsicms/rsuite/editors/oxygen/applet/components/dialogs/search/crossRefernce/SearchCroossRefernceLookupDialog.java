package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.crossRefernce;

import java.awt.Dimension;

import javax.swing.JFrame;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;

public class SearchCroossRefernceLookupDialog extends LookupDialog {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	public SearchCroossRefernceLookupDialog(JFrame mainFrame,
			AuthorAccess authorAccess, InsertReferenceElement element) {
		super(mainFrame);

		add(new SearchCrossReferncePanel(this, authorAccess, element));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Search");

		setModal(true);
		setPreferredSize(new Dimension(1100, 570));
		setLocation(100, 100);
	}

}
