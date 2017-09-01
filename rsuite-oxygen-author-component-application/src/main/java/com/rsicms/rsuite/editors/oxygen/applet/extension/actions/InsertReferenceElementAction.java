package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.util.List;

import javax.swing.text.BadLocationException;

import org.apache.commons.lang.StringUtils;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AttrValue;
import ro.sync.ecss.extensions.api.node.AuthorDocumentFragment;
import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class InsertReferenceElementAction {

	private static final String REGULAR_EXPRESSION_REFERENCE_TAG = "(<([^\\s]+).+)(\\/>)";

	private AuthorAccess authorAccess;

	private InsertReferenceElement element;

	public InsertReferenceElementAction(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		this.authorAccess = authorAccess;

		this.element = element;
	}

	public void insertReferenceElement(
			ISelectedReferenceNode selectedReferenceNode)
			throws OxygenIntegrationException {

		try {

			InsertReferenceHandler referenceHandler = element
					.getReferenceHandler();

			String linkValue = selectedReferenceNode
					.getLinkValue(referenceHandler);
			IReferenceTargetElement targetElement = selectedReferenceNode
					.getReferenceTargetElement();

			String referenceElementName = element.getElementName();

			if (element.isConfRef()) {
				referenceElementName = targetElement.getElementName();
			}

			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			AuthorDocumentFragment newDocumentFragment = OxygenUtils
					.createXMLFragmentInCurrentContext(authorAccess,
							referenceElementName);
								
			
			List<AuthorNode> nodes = newDocumentFragment.getContentNodes();
			
			if (!nodes.isEmpty()) {
				AuthorElement referenceElement = (AuthorElement) nodes.get(0);
				referenceElement.setAttribute(element.getAttributeName(), new AttrValue(
						linkValue));
			}

			String xmlFragment = documentController
					.serializeFragmentToXML(newDocumentFragment);

			xmlFragment = addSelectedTextToTheRefenceElement(documentController, xmlFragment);
			
			xmlFragment = referenceHandler.getXmlFragment(
					selectedReferenceNode, xmlFragment);

			OxygenEditorContext context = OxygenComponentRegister
					.getRegisterOxygenEditorContext(authorAccess);

			InsertReferenceHandler systemRefernceHandler = context
					.getMainComponent().getCmsCustomization()
					.getSystemReferenceHandler();

			systemRefernceHandler.beforeInsertFragment(authorAccess, element,
					selectedReferenceNode);
			referenceHandler.beforeInsertFragment(authorAccess, element,
					selectedReferenceNode);
			documentController.insertXMLFragmentSchemaAware(xmlFragment,
					authorAccess.getEditorAccess().getCaretOffset() , true);
			
			referenceHandler.afterInsertFragment(authorAccess, element,
					selectedReferenceNode);
			systemRefernceHandler.afterInsertFragment(authorAccess, element,
					selectedReferenceNode);

		} catch (BadLocationException e) {
			throw new OxygenIntegrationException("Unable to insert refrence ",
					e);
		} catch (AuthorOperationException e) {
			throw new OxygenIntegrationException("Unable to insert refrence ",
					e);
		}
	}

	private String addSelectedTextToTheRefenceElement(AuthorDocumentController documentController, String xmlFragment) throws AuthorOperationException, BadLocationException {
		String selectedText = authorAccess.getEditorAccess().getSelectedText();
		
		if (StringUtils.isNotBlank(selectedText) && xmlFragment.matches(REGULAR_EXPRESSION_REFERENCE_TAG)){
			
			AuthorDocumentFragment textFragment = documentController.createNewDocumentTextFragment(selectedText);
			selectedText = documentController.serializeFragmentToXML(textFragment);
			
			xmlFragment = xmlFragment.replaceFirst(REGULAR_EXPRESSION_REFERENCE_TAG, "$1>" + selectedText + "</$2>");
		}
		return xmlFragment;
	}
}
