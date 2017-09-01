package com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public interface ITreeContextMenu {

	boolean showContextMenu(JTree tree, TreePath path);
	
	JPopupMenu createContextMenu(final JTree tree, TreePath path);
}
