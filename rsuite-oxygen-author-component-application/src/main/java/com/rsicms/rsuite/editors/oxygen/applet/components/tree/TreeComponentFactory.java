package com.rsicms.rsuite.editors.oxygen.applet.components.tree;

import org.apache.log4j.Logger;

@Deprecated
public class TreeComponentFactory {

	private static Logger logger = Logger.getLogger(TreeComponentFactory.class);

//	public static JTree setUpTreeComponent(InsertReferenceElement element) {
//		ITreeOxygenLookUp treeLookup = LookupFactoryRegister
//				.getITreeOxygenLookUp(element);
//
//		JTree tree = new JTree();
//
//		setUpCustomTreeIcons(tree);
//
//		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
//				treeLookup.getRootDisplayText(), true);
//
//		rootNode.setUserObject(new RootRepositoryResource(treeLookup
//				.getRootDisplayText(), treeLookup.getRootIcon()));
//
//		DefaultTreeModel model = new DefaultTreeModel(rootNode);
//
//		try {
//			List<IReposiotryResource> rootChildren = treeLookup
//					.getRootChildren();
//
//			for (IReposiotryResource node : rootChildren) {
//
//				LazyLoadingTreeNode lazyNode = new LazyLoadingTreeNode(node,
//						model);
//				rootNode.add(lazyNode);
//			}
//		} catch (Exception e) {
//			OxygenUtils.handleException(logger, e);
//		}
//
//		final LazyLoadingTreeController controller = new LazyLoadingTreeController(
//				model, treeLookup);
//
//		tree.setModel(model);
//		tree.addTreeWillExpandListener(controller);
//
//		return tree;
//	}
//
//	private static void setUpCustomTreeIcons(JTree tree) {
//		ImageIcon imageIcon = TreeCellRenderer.createImageIcon("images/im.gif");
//		ImageIcon xmlIcon = TreeCellRenderer
//				.createImageIcon("images/xml-resource.gif");
//
//		ImageIcon folderIcon = TreeCellRenderer
//				.createImageIcon("images/folder.png");
//
//		if (imageIcon != null && xmlIcon != null) {
//			tree.setCellRenderer(new TreeCellRenderer(folderIcon, xmlIcon,
//					imageIcon));
//		}
//	}
	

}
