package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class TreeContexMenuMouseAdapter extends MouseAdapter {

	private ITreeContextMenu treeContextMenu;

	public TreeContexMenuMouseAdapter(ITreeContextMenu treeContextMenu) {
		this.treeContextMenu = treeContextMenu;
	}

	private void myPopupEvent(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		final JTree tree = (JTree) e.getSource();
		TreePath path = tree.getPathForLocation(x, y);
		if (path == null)
			return;

		tree.setSelectionPath(path);

		if (treeContextMenu.showContextMenu(tree, path)) {
			JPopupMenu popup = treeContextMenu.createContextMenu(tree, path);
			popup.show(tree, x, y);
		}

	}

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			myPopupEvent(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			myPopupEvent(e);
	}

}
