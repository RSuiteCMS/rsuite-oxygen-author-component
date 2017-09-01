package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.menuitems.RemoveBookmarkMenuItem;

public class BookmarkContextMenu implements ITreeContextMenu {

	private IBookmarkManager bookmarkManager;
	
	public BookmarkContextMenu(IBookmarkManager bookmarkManager) {
		super();
		this.bookmarkManager = bookmarkManager;
	}

	@Override
	public boolean showContextMenu(JTree tree, TreePath path) {
		return path.getPathCount() == 2;
	}

	@Override
	public JPopupMenu createContextMenu(final JTree tree, TreePath path) {

		JPopupMenu popup = new JPopupMenu();

		JMenuItem removeBookmarkMenuItem = new RemoveBookmarkMenuItem(tree, bookmarkManager);
		popup.add(removeBookmarkMenuItem);

		return popup;

	}

}
