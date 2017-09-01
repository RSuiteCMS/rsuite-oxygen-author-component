package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.contentcompletion.xml.CIAttribute;
import ro.sync.contentcompletion.xml.CIElement;
import ro.sync.contentcompletion.xml.WhatAttributesCanGoHereContext;
import ro.sync.contentcompletion.xml.WhatElementsCanGoHereContext;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorSchemaManager;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.ecss.extensions.api.node.AttrValue;
import ro.sync.ecss.extensions.api.node.AuthorDocumentFragment;
import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;

public class OxygenUtils {

	private static Logger logger = Logger.getLogger(OxygenUtils.class);


	public static String getApplicationId() {
		return "RSIcms Oxygen editor";
	}

	/**
	 * Invokes a runnable on AWT. Useful when calling Java methods from
	 * JavaScript.
	 * 
	 * @param runnable
	 *            The runnable
	 */
	public static void invokeOnAWT(Runnable runnable) {
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				OxygenUtils.handleException(logger, e);
			} catch (InvocationTargetException e) {
				OxygenUtils.handleException(logger, e);
			}
		}
	}

	public static Icon getIcon(String imgageName) {
		ImageIcon icon = null;
		try {
			InputStream is = getImageStream(imgageName);
			icon = new ImageIcon(IOUtils.toByteArray(is));
		} catch (Exception ex) {
			OxygenUtils.handleException(logger, ex);
		}
		
		return icon;
	}

	public static InputStream getImageStream(String imgageName) throws IOException {
			String imagePath = "images/" + imgageName;
			InputStream is = OxygenUtils.class.getClassLoader()
					.getResourceAsStream(imagePath);
			if (is == null) {
				throw new IOException("Unable to find image: " + imagePath);
			}

			return is;			

		
	}

	/**
	 * Checks if the element can be inserted in current context.
	 * 
	 * @return
	 */
	public static boolean checkIfCanInsertElementInCurrentContext(
			AuthorAccess authorAccess, InsertReferenceElement elementToInsert) {
		return checkIfCanInsertElementInCurrentContext(authorAccess,
				elementToInsert.getElementName());
	}

	/**
	 * Checks if the element can be inserted in current context.
	 * 
	 * @return
	 */
	public static boolean checkIfCanInsertElementInCurrentContext(
			AuthorAccess authorAccess, String elementToInsert) {
		boolean canInsert = false;
		try {
			int offset = authorAccess.getEditorAccess().getCaretOffset();

			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			AuthorSchemaManager manger = documentController
					.getAuthorSchemaManager();
			WhatElementsCanGoHereContext whereContext = manger
					.createWhatElementsCanGoHereContext(offset);

			if (whereContext != null) {
				List<CIElement> elements = manger
						.whatElementsCanGoHere(whereContext);

				if (elements != null) {
					for (CIElement element : elements) {
						if (elementToInsert.equalsIgnoreCase(element.getName())) {
							canInsert = true;
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
		return canInsert;
	}
	
	public static AuthorDocumentFragment createXMLFragmentInCurrentContext(
			AuthorAccess authorAccess, String elementToInsert) throws OxygenIntegrationException {
		
		try {
			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			
			AuthorSchemaManager manger = documentController
					.getAuthorSchemaManager();

				List<CIElement> elements = manger.getAllPossibleElements();

				if (elements != null) {
					for (CIElement element : elements) {
						if (elementToInsert.equalsIgnoreCase(element.getName())) {
							return manger.createAuthorDocumentFragment(element);
						}
					}
				}

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
		
		
		throw new OxygenIntegrationException("Unable to create element " + elementToInsert + ". Element is not defined in schema.");
	}

	/**
	 * Checks if the element can be inserted in current context.
	 * 
	 * @return
	 */
	public static boolean checkIfCanInsertTextInCurrentContext(
			AuthorAccess authorAccess) {
		boolean canInsert = false;
		try {
			int offset = authorAccess.getEditorAccess().getCaretOffset();

			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			AuthorSchemaManager manger = documentController
					.getAuthorSchemaManager();
			canInsert = manger.canInsertText(offset);
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
		return canInsert;
	}

	/**
	 * Checks if the attribute can be inserted in current context.
	 * 
	 * @return
	 */
	public static boolean checkIfCanInsertAttribute(AuthorAccess authorAccess,
			String attributeName) {
		boolean canInsert = false;
		try {
			int offset = authorAccess.getEditorAccess().getCaretOffset();

			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			AuthorNode currentNode = documentController.getNodeAtOffset(offset);
			AuthorElement element = null;

			if (currentNode.getType() != AuthorNode.NODE_TYPE_ELEMENT) {
				element = (AuthorElement) currentNode.getParent();
			} else {
				element = (AuthorElement) currentNode;
			}

			AttrValue value = element.getAttribute(attributeName);

			if (value != null) {
				return true;
			}

			canInsert = canInsertAttribute(documentController, element,
					attributeName);

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
		return canInsert;
	}

	public static boolean canInsertAttribute(
			AuthorDocumentController documentController, AuthorElement element,
			String attributeName

	) {
		AuthorSchemaManager manger = documentController
				.getAuthorSchemaManager();

		boolean canInsert = false;

		WhatAttributesCanGoHereContext context = manger
				.createWhatAttributesCanGoHereContext(element);
		List<CIAttribute> attributes = manger.whatAttributesCanGoHere(context);
		if (attributes != null) {
			for (CIAttribute attriubte : attributes) {
				if (attributeName.equals(attriubte.getName())) {
					canInsert = true;
					break;
				}
			}
		}
		return canInsert;
	}

	/**
	 * Checks if the attribute can be inserted in current context.
	 * 
	 * @return
	 */
	public static AuthorElement getCurrentElement(AuthorAccess authorAccess) {
		AuthorElement element = null;
		try {
			int offset = authorAccess.getEditorAccess().getCaretOffset();

			AuthorDocumentController documentController = authorAccess
					.getDocumentController();

			AuthorNode currentNode = documentController.getNodeAtOffset(offset);

			if (currentNode.getType() != AuthorNode.NODE_TYPE_ELEMENT) {
				element = (AuthorElement) currentNode.getParent();
			} else {
				element = (AuthorElement) currentNode;
			}

		} catch (BadLocationException e) {
			OxygenUtils.handleException(logger, e);
		}
		return element;
	}

	public static void handleExceptionUI(String message, Throwable e) {
		showErrorMessage(message);
		handleException(logger, e);
	}

	private static void showErrorMessage(String message) {
		if (OxygenMainComponent.getCurrentInstance() != null){
			OxygenMainComponent.getCurrentInstance().getDialogHelper().showErrorMessage(message);
		}
	}
	
	public static void handleExceptionUI(Logger logger, Throwable e) {
		showErrorMessage(e.getMessage());
		handleException(logger, e);
	}
	
	public static void handleException(Logger logger, Throwable e) {
		logger.error(e, e);
	}

	public static String getStringFromReader(BufferedReader reader) {
		StringBuilder builder = new StringBuilder();
		try {
			char[] cb = new char[1024];
			int r;
			r = reader.read(cb);
			while (r != -1) {
				builder.append(cb, 0, r);
				r = reader.read(cb);
			}
		} catch (Exception e) {
			// Does not happen.
			// logger.error(e, e);
		}
		return builder.toString();
	}

	public static boolean isBlank(String text) {
		if (text == null || text.trim().equals("")) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public static ICustomizationFactory loadCustomizationFactory(
			ClassLoader classLoader, String customizationClass)
			throws OxygenIntegrationException {
		try {
			if (!StringUtils.isBlank(customizationClass)
					&& !"undefined".equalsIgnoreCase(customizationClass)) {

				Class clazz = classLoader.loadClass(customizationClass);
				Object object = clazz.newInstance();
				
				if (!(object instanceof ICustomizationFactory)) {
					throw new OxygenIntegrationException("class "
							+ customizationClass + " must implement "
							+ ICustomizationFactory.class.getCanonicalName());
				}

				return (ICustomizationFactory) object;
			}

		} catch (ClassNotFoundException e) {
			throw new OxygenIntegrationException(e);
		} catch (InstantiationException e) {
			throw new OxygenIntegrationException(e);
		} catch (IllegalAccessException e) {
			throw new OxygenIntegrationException(e);
		}

		return null;
	}

	public static WSAuthorComponentEditorPage getAuthorComponentEditorPage(EditorComponentProvider editorComponent){
		return ((WSAuthorComponentEditorPage)editorComponent.getWSEditorAccess().getCurrentPage());
	}

}
