package com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;



public class LazyLoadingExpandWorker extends Thread {

	private LazyLoadingTreeNode parent;

	private DefaultTreeModel model;

	private ITreeOxygenLookUp treeLookup;
	
	
	private Logger logger = Logger.getLogger(this.getClass());

	public LazyLoadingExpandWorker(LazyLoadingTreeNode parent, DefaultTreeModel model,
			ITreeOxygenLookUp treeLookup) {
		super();
		this.parent = parent;
		this.model = model;
		this.treeLookup = treeLookup;
	}

	@Override
	public void run() {
		parent.setChildren(loadChildren());
		model.nodeStructureChanged(parent);
		
	}

	private List<MutableTreeNode> loadChildren() {
		List<MutableTreeNode> list = new ArrayList<MutableTreeNode>();
		IReposiotryResource parentRepositoryNode = (IReposiotryResource) parent
				.getUserObject();

		try {
			List<IReposiotryResource> children = treeLookup
					.getChildren(parentRepositoryNode.getId());
			for (IReposiotryResource child : children) {
				LazyLoadingTreeNode newNode = new LazyLoadingTreeNode(child, model);
				list.add(newNode);
			}
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

		return list;
	}
}
