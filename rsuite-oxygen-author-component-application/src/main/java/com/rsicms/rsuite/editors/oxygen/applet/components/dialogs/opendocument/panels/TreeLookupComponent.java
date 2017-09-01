package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.RootRepositoryResource;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.ITreeContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.TreeContexMenuMouseAdapter;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeController;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.TreeCellRenderer;

public class TreeLookupComponent extends JTree {

	private static Logger logger = Logger
			.getLogger(TreeLookupComponent.class);
	
	/** uid **/
	private static final long serialVersionUID = 4580453589459043671L;
	
	private ITreeOxygenLookUp treeLookup;
	
	private DefaultTreeModel treeModel;
	
	public TreeLookupComponent(String name, ITreeOxygenLookUp treeLookup, ITreeContextMenu contextMenu) {
		
		this.treeLookup = treeLookup;
		
		setName(name);

		setUpCustomTreeIcons(this);

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
				treeLookup.getRootDisplayText(), true);

		rootNode.setUserObject(new RootRepositoryResource(treeLookup
				.getRootDisplayText(), treeLookup.getRootIcon()));

		treeModel = new DefaultTreeModel(rootNode);
		initializeTreeModel(treeLookup, rootNode, treeModel);

		final LazyLoadingTreeController controller = new LazyLoadingTreeController(
				treeModel, treeLookup);

		setModel(treeModel);
		addTreeWillExpandListener(controller);
		
		addContextMenu(contextMenu);
		
	}
	
	private void addContextMenu(ITreeContextMenu contextMenu) {
		
		TreeContexMenuMouseAdapter treeContexMenuMouseAdapter = new TreeContexMenuMouseAdapter(
				contextMenu);
		addMouseListener(treeContexMenuMouseAdapter);
	}

	private void initializeTreeModel(ITreeOxygenLookUp treeLookup,
			DefaultMutableTreeNode rootNode, DefaultTreeModel model) {
		try {
			List<IReposiotryResource> rootChildren = treeLookup
					.getRootChildren();

			for (IReposiotryResource node : rootChildren) {

				LazyLoadingTreeNode lazyNode = new LazyLoadingTreeNode(node,
						model);
				rootNode.add(lazyNode);
			}
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
	}

	private void setUpCustomTreeIcons(JTree tree) {
		ImageIcon imageIcon = TreeCellRenderer.createImageIcon("images/im.gif");
		ImageIcon xmlIcon = TreeCellRenderer
				.createImageIcon("images/xml-resource.gif");

		ImageIcon folderIcon = TreeCellRenderer
				.createImageIcon("images/folder.png");

		if (imageIcon != null && xmlIcon != null) {
			tree.setCellRenderer(new TreeCellRenderer(folderIcon, xmlIcon,
					imageIcon));
		}
	}
	
	public void loadTree(){
		try {

			Object rootObject = getModel().getRoot();

			if (rootObject instanceof DefaultMutableTreeNode) {
				DefaultTreeModel model = (DefaultTreeModel) getModel();
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) rootObject;
				rootNode.removeAllChildren();
				model.reload();
				
				List<IReposiotryResource> rootChildren = treeLookup
						.getRootChildren();

				for (IReposiotryResource node : rootChildren) {

					LazyLoadingTreeNode lazyNode = new LazyLoadingTreeNode(
							node, model);
					rootNode.add(lazyNode);
				}

				expandRow(0);
			}

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
	}
	
	public void reloadTee(){
		loadTree();
	}
}
