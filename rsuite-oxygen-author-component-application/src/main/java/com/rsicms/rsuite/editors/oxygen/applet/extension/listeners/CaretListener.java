package com.rsicms.rsuite.editors.oxygen.applet.extension.listeners;

import ro.sync.ecss.extensions.api.AuthorCaretEvent;
import ro.sync.ecss.extensions.api.AuthorCaretListener;
import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;

public class CaretListener implements AuthorCaretListener {

	private AuthorNode previousNode;

	private AuthorNode currentNode;
	
	private AuthorElement previousCrossRefElement;

	@Override
	public void caretMoved(AuthorCaretEvent caretEvent) {
		
		AuthorNode newNode = caretEvent.getNode();
		
		if (currentNode != newNode){
			
			if (newNode.getType() == AuthorNode.NODE_TYPE_ELEMENT && "CrossReference".equals(newNode.getName())){
				previousCrossRefElement = (AuthorElement)newNode;
			}
			
			previousNode = currentNode;
			
		}
	
		
		currentNode = newNode;
	}

	public AuthorNode getPreviousNode() {
		return previousNode;
	}

	public AuthorNode getCurrentNode() {
		return currentNode;
	}

	public AuthorElement getPreviousCrossRefElement() {
		return previousCrossRefElement;
	}

	

}
