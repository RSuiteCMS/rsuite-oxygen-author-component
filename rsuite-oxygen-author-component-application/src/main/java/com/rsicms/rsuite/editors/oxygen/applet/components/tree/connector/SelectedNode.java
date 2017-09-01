package com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;

public class SelectedNode implements ISelectedReferenceNode {

	private IReposiotryResource repositoryNode;
	
	private IReferenceTargetElement referenceTargetNode;
	
	private boolean contextNode;
	
	private String linkValue;

	public SelectedNode(IReposiotryResource repositoryNode,
			IReferenceTargetElement referenceTargetNode, boolean contextNode) {
		this(repositoryNode);
		this.referenceTargetNode = referenceTargetNode;
		this.contextNode = contextNode;
	}

	public SelectedNode(IReposiotryResource repositoryNode) {
		this.repositoryNode = repositoryNode;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.ISelectedNode#getRepositoryNode()
	 */
	@Override
	public IReposiotryResource getRepositoryResource() {
		return repositoryNode;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.ISelectedNode#getReferenceTargetNode()
	 */
	@Override
	public IReferenceTargetElement getReferenceTargetElement() {
		return referenceTargetNode;
	}

	@Override
	public boolean isContextNode() {
		return contextNode;
	}

	@Override
	public String getLinkValue(InsertReferenceHandler referenceHandler) {
		
		if (linkValue == null){
			initializeLinkValue(referenceHandler);	
		}
		
		return linkValue;
	}

	private void initializeLinkValue(InsertReferenceHandler referenceHandler) {
		linkValue = repositoryNode.getCMSlink();
		
		if (referenceTargetNode != null) {

			if (isContextNode()){
				linkValue = "";
			}
			
			linkValue += "#" + referenceTargetNode.getTargetId();
		}
		

		if (referenceHandler != null){
			linkValue = referenceHandler.getLinkValue(this,
					linkValue);
		}
		
	}
	
}
