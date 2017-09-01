package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.menuitems.AddBookmarkMenuItem;

public class RepositoryContextMenu implements ITreeContextMenu {

	private IBookmarkManager bookmarkManager;
	
	public RepositoryContextMenu(IBookmarkManager bookmarkManager) {
		super();
		this.bookmarkManager = bookmarkManager;
	}

	@Override
	public boolean showContextMenu(JTree tree, TreePath path) {

		if (path.getParentPath() != null) {
			return true;
		}

		return false;
	}

	@Override
	public JPopupMenu createContextMenu(final JTree tree, TreePath path) {

		JPopupMenu popup = new JPopupMenu();

		JMenuItem addBookmarkMenuItem =  new AddBookmarkMenuItem(tree, bookmarkManager);
		popup.add(addBookmarkMenuItem);

		return popup;
	}

}
