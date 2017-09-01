package com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;


public class LazyLoadingTreeNode extends DefaultMutableTreeNode {
	/** UID */
	private static final long serialVersionUID = 1L;
	private DefaultTreeModel model;

	private boolean isEmptyContainer;
	
	private String displayName;
	
	/**
	 * Default Constructor
	 * 
	 * @param userObject
	 *            an Object provided by the user that constitutes the node's
	 *            data
	 * @param tree
	 *            the JTree containing this Node
	 * @param cancelable
	 */
	public LazyLoadingTreeNode(Object userObject, DefaultTreeModel model) {
		super(userObject);
		this.model = model;
		IReposiotryResource repositoryNode = (IReposiotryResource) userObject;
		 
		displayName = repositoryNode.getDisplayText();
		
		
		if (displayName != null){
			displayName = displayName.replaceAll("\\s+", " ");
		}
		
		if (repositoryNode.isContainer()) {
		
			if (repositoryNode.hasChilds()){
				setAllowsChildren(true);
			}else{
				setChildren(new ArrayList<MutableTreeNode>());
				isEmptyContainer = true;			
			}
						
			
		} else {
			setAllowsChildren(false);
		}

	}

	/**
	 * Define nodes children
	 * 
	 * @param nodes
	 *            new nodes
	 */
	protected void setChildren(List<MutableTreeNode> nodes) {
		int childCount = getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				model.removeNodeFromParent((MutableTreeNode) getChildAt(0));
			}
		}
		for (int i = 0; nodes != null && i < nodes.size(); i++) {
			model.insertNodeInto(nodes.get(i), this, i);
		}
	}

	/**
	 * Need some improvement ... This method should restore the Node initial
	 * state if the worker if canceled
	 */
	protected void reset() {
		int childCount = getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				model.removeNodeFromParent((MutableTreeNode) getChildAt(0));
			}
		}
		setAllowsChildren(true);
	}

	/**
	 * 
	 * @return <code>true</code> if there are some children
	 */
	protected boolean areChildrenLoaded() {
		return getChildCount() > 0 && getAllowsChildren() || (isEmptyContainer && !isLeaf());
	}

	/**
	 * If the
	 * 
	 * @see #getAllowsChildren()
	 * @return false, this node can't be a leaf
	 */
	@Override
	public boolean isLeaf() {
		return !getAllowsChildren();
	}

	public String getDisplayName() {
		return displayName;
	}
	
}