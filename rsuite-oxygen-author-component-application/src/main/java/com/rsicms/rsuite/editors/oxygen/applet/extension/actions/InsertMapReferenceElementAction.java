package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.component.ditamap.DITAMapTreeComponentProvider;
import ro.sync.ecss.extensions.api.node.AuthorElement;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDitaMapDocument;

public class InsertMapReferenceElementAction implements ActionListener {

	private Logger logger = Logger.getLogger(this.getClass());

	private DITAMapTreeComponentProvider ditaMapComponent;
	
	private Dialog lookupDialog;

	private InsertReferenceElement element;

	private ISelectableComponent lookupComponent;
	
	private Position position = Position.AFTER;

	public InsertMapReferenceElementAction(OxygenDitaMapDocument ditaMapDocument,
			Dialog lookupDialog, InsertReferenceElement element,
			ISelectableComponent lookupComponent, Position position) {
		this(ditaMapDocument, lookupDialog, element, lookupComponent);
		this.position = position;
	}
	

	public InsertMapReferenceElementAction(OxygenDitaMapDocument ditaMapDocument,
			Dialog lookupDialog, InsertReferenceElement element,
			ISelectableComponent lookupComponent) {
		
		this.lookupDialog = lookupDialog;
		this.element = element;
		this.lookupComponent = lookupComponent;
		this.ditaMapComponent = ditaMapDocument.getDitaMapComponent();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {

		ISelectedReferenceNode targetNode = lookupComponent.getSelectedReferenceNode();
		if (targetNode != null){
			insertElement(targetNode);
		}
	}

	public void insertElement(ISelectedReferenceNode selectedReferenceNode) {
		
			if (selectedReferenceNode != null) {

				AuthorDocumentController ditaMapDocumentController = ditaMapComponent.getDITAAccess().getDocumentController();
				
				JTree mapTree = (JTree) ditaMapComponent.getDITAAccess().getDITAMapTreeComponent();
					TreePath sel = mapTree.getSelectionPath();
				
					if(sel != null) {
						Object selNode = sel.getLastPathComponent();
				
						if(selNode instanceof AuthorElement) {
							AuthorElement selElem = (AuthorElement) selNode;
							try {
				
								InsertReferenceHandler referenceHandler = element
										.getReferenceHandler();

								IReposiotryResource repositoryResourceNode = selectedReferenceNode
										.getRepositoryResource();
								IReferenceTargetElement targetElement = selectedReferenceNode
										.getReferenceTargetElement();

								String linkValue = repositoryResourceNode.getCMSid();
								
								if (targetElement != null) {

									if (selectedReferenceNode.isContextNode()){
										linkValue = "";
									}
									
									linkValue += "#" + targetElement.getTargetId();
								}

								linkValue = referenceHandler.getLinkValue(selectedReferenceNode,
										linkValue);

								String xmlFragment = "<" + element.getElementName() + " "
										+ element.getAttributeName() + "=\"" + linkValue
										+ "\" />";
								xmlFragment = referenceHandler.getXmlFragment(selectedReferenceNode,
										xmlFragment);
								
//								if (position == Position.APPEND_CHILD){
//									ditaMapDocumentController.insertXMLFragment(xmlFragment, selElem, AuthorConstants.POSITION_INSIDE_LAST);
//								}else{
//									ditaMapDocumentController.insertXMLFragment(xmlFragment, getPositionOffset(selElem, position));
//								}
								
								ditaMapDocumentController.insertXMLFragment(xmlFragment, getPositionOffset(selElem, position));
								
								
							} catch (AuthorOperationException e) {
								OxygenUtils.handleException(logger, e);
							}
						}
					}

			}

				lookupDialog.dispose();
			
		
	}
	
	private int getPositionOffset(AuthorElement selElem, Position position){
		switch (position) {
		case AFTER:
			return selElem.getEndOffset() + 1;
		case BEFORE:
			return  selElem.getStartOffset();
		case APPEND_CHILD:
			return  selElem.getEndOffset();
		default:
			return selElem.getEndOffset() + 1;
		}
	}
	
	 public enum Position{
		 AFTER, BEFORE, END, APPEND_CHILD
	 }
}
