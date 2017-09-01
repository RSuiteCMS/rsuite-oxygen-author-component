package com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy;


import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;



public class LazyLoadingTreeController implements TreeWillExpandListener {
	
	
	
	/** Tree Model */
	private DefaultTreeModel model;
	
	/** Tree Model */
	private ITreeOxygenLookUp treeLookup;
	
	/**
	 * Default constructor
	 * @param model Tree model
	 */
	public LazyLoadingTreeController(DefaultTreeModel model, ITreeOxygenLookUp treeLookup) {
		this.model = model;
		this.treeLookup = treeLookup;
	}

	public void treeWillCollapse(TreeExpansionEvent event)
			throws ExpandVetoException {
		//Do nothing on collapse.
	}
	/**
     * Invoked whenever a node in the tree is about to be expanded.
     * If the Node is a LazyLoadingTreeNode load it's children in a SwingWorker
     */
	public void treeWillExpand(TreeExpansionEvent event)
			throws ExpandVetoException {
		TreePath path = event.getPath();
		Object lastPathComponent = path.getLastPathComponent();
		if (lastPathComponent instanceof LazyLoadingTreeNode) {
			LazyLoadingTreeNode lazyNode = (LazyLoadingTreeNode) lastPathComponent;
			expandNode(lazyNode, model);
		}
	}

	/**
	 * If the Node is not already loaded 
	 * @param node
	 * @param model
	 */
	public void expandNode(final LazyLoadingTreeNode node, 
			final DefaultTreeModel model) {
		
		
		if (node.areChildrenLoaded()) {
			return;
		}
		node.setChildren(createLoadingNode());
		
		LazyLoadingExpandWorker worker = new LazyLoadingExpandWorker(node, model, treeLookup);
		worker.start();
	}
	
	/**
	 * 
	 * @return a new Loading please wait node 
	 */
	protected List<MutableTreeNode> createLoadingNode() {
		List<MutableTreeNode> nodes = new ArrayList<MutableTreeNode>();
		nodes.add(new DefaultMutableTreeNode("Loading ...", false));		
		return nodes;
	}
	
	
}
