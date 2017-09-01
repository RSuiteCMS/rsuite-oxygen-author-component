package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.menuitems;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

public class RemoveBookmarkMenuItem extends JMenuItem {

	private static final long serialVersionUID = 7444911773918644358L;

	public RemoveBookmarkMenuItem(JTree tree, IBookmarkManager bookmarkManager) {
		super(createAction("Remove Bookmark", tree, bookmarkManager));
	}

	public static AbstractAction createAction(final String name, final JTree tree, final IBookmarkManager bookmarkManager){
		return new AbstractAction(name) {

			private static final long serialVersionUID = -8891559498795260969L;

			@Override
			public void actionPerformed(ActionEvent ae) {

				TreePath currentSelection = tree.getSelectionPath();
				if (currentSelection != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
							.getLastPathComponent();
					DefaultTreeModel model = ((DefaultTreeModel) tree
							.getModel());

					IReposiotryResource repositoryNode = AddBookmarkMenuItem
							.getRepositoryNode(node);
					if (repositoryNode != null
							&& bookmarkManager.removeBookmark(repositoryNode
									.getCMSid())) {
						model.removeNodeFromParent(node);
					}
				}
			}
		};
	}

}
