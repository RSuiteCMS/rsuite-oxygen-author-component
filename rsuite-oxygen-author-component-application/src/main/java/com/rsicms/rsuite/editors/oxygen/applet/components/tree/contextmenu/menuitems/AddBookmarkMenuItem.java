package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.menuitems;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

public class AddBookmarkMenuItem extends JMenuItem {

	private static final long serialVersionUID = -3931266160773097117L;


	public AddBookmarkMenuItem(JTree tree, IBookmarkManager bookmarkManager) {
		super(createAction("Add bookmark", tree, bookmarkManager));
	}

	
	public static AbstractAction createAction(final String name, final JTree tree, final IBookmarkManager bookmarkManager){
		return new AbstractAction(name) {
			
			private static final long serialVersionUID = -8891559498795260969L;

			@Override
			public void actionPerformed(ActionEvent e) {

				TreePath currentSelection = tree.getSelectionPath();
				if (currentSelection != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
							.getLastPathComponent();

					IReposiotryResource repositoryNode = getRepositoryNode(node);
					if (repositoryNode != null) {
						bookmarkManager.addBookmark(repositoryNode.getCMSid());
					}

				}
				
			}
		};
	}


	public static IReposiotryResource getRepositoryNode(DefaultMutableTreeNode node){
		Object userObject = node.getUserObject();

		if (userObject instanceof IReposiotryResource) {
			IReposiotryResource repositoryNode = (IReposiotryResource) userObject;
			if (repositoryNode != null) {
				return repositoryNode;
			}
		}	
		
		return null;
	}
}
