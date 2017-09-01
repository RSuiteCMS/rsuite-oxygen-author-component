package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels;

import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.PANEL_NAME_REPOSITORY;

import javax.swing.JDialog;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.ITreeContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.RepositoryContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentAction;

public class RepositoryTreeLookupPanel extends TreeLookupPanel{

	public RepositoryTreeLookupPanel(JDialog parentDialog,
			ITreeOxygenLookUp treeLookup, IBookmarkManager bookmarkManager,
			OpenNewDocumentAction openDocumentAction, ICmsURI cmsURI) {
		
		super(parentDialog, treeLookup, createContextMenu(bookmarkManager), openDocumentAction, cmsURI);
	}

	/** uid **/
	private static final long serialVersionUID = 2534699320204103436L;

	protected String getPanelName() {
		return PANEL_NAME_REPOSITORY;
	}

	protected String getTreeComponentName() {
		return "repositoryTree";
	}

	private static ITreeContextMenu createContextMenu(IBookmarkManager bookmarkManager) {
		return new RepositoryContextMenu(bookmarkManager);
	}

}
