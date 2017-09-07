package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.awt.event.KeyEvent;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.ecss.extensions.api.component.listeners.AuthorComponentListener;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import ro.sync.exml.view.graphics.Rectangle;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

import com.rsicms.rsuite.editors.oxygen.applet.common.DocumentCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IComponentInitializeHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.LookupFactoryRegister;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenDocumentViews;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.TagMode;
import com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent.AuthorComponentFactoryManager;
import com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent.OxygenBaseDocumentObjects;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ChangeTagModeAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;
import com.rsicms.rsuite.editors.oxygen.applet.extension.listeners.CaretListener;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class OxygenDocument implements IOxygenDocument {

	private Logger logger = Logger.getLogger(getClass());

	private volatile boolean isModified = false;

	private JTabbedPane tabbedPane;

	private EditorComponentProvider editorComponentProvider;

	private OxygenMainComponent mainComponent;

	private OxygenDocumentViews documentViews;

	private TagMode tagMode = TagMode.NO_TAGS;

	private ICustomizationFactory customizationFactory;

	private IDocumentURI documentURI;

	private IDocumentCustomization documentCustomization;

	private LookupFactoryRegister lookupRegister;

	private String documentId;

	private boolean editable = true;

	private AuthorAccess authorAccess;

	private ICmsActions cmsActions;

	private WSAuthorComponentEditorPage editorPage;

	private OxygenOpenDocumentParmaters openDocumentParameters;

	public OxygenDocument(String documentId) {
		this.documentId = documentId;
	}

	public OxygenDocument(OxygenMainComponent mainComponent,
			JTabbedPane tabbedPane, OxygenOpenDocumentParmaters parameters,
			boolean editable) throws OxygenIntegrationException {

		try {

			customizationFactory = mainComponent.getCustomizationFactory();
			IDocumentHandler systemDocumentHandler = customizationFactory
					.getCmsCustomization().getSystemDocumentHandler();

			String documentUri = parameters
					.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);

			documentURI = mainComponent.getCustomizationFactory()
					.createDocumentURI(parameters);

			if (systemDocumentHandler != null) {
				systemDocumentHandler.beforeDocumentInitialization(documentURI,
						parameters);
			}

			AuthorComponentFactoryManager factory = mainComponent
					.getAuthorComponentFactoryManager();

			OxygenBaseDocumentObjects documentObjects = factory
					.createOxygenBaseDocumentObjects();
			editorComponentProvider = documentObjects
					.getEditorComponentProvider();

			editorPage = documentObjects.getEditorPage();

			editorPage.setEditable(editable);

			authorAccess = documentObjects.getAuthorAccess();

			CaretListener caretListener = new CaretListener();
			authorAccess.getEditorAccess()
					.addAuthorCaretListener(caretListener);

			this.tabbedPane = tabbedPane;
			this.editable = editable;

			this.mainComponent = mainComponent;
			configure(editorComponentProvider);

			documentViews = new OxygenDocumentViews(mainComponent, this);

			documentId = documentUri;

			URL toLoad = new URL(documentUri);

			documentCustomization = DocumentCustomizationFactory
					.createCustomization(customizationFactory, documentURI,
							parameters);

			if (systemDocumentHandler != null) {
				systemDocumentHandler.beforeOpenDocument(documentURI,
						parameters, editorComponentProvider);
			}

			IDocumentHandler customDocumentHandler = documentCustomization
					.getDocumentHandler();
			if (customDocumentHandler != null) {
				customDocumentHandler.beforeOpenDocument(documentURI,
						parameters, editorComponentProvider);
			}

			OxygenEditorContext context = new OxygenEditorContext(
					mainComponent, this, caretListener);
			OxygenComponentRegister.registerOxygenEditorContext(authorAccess,
					context);

			IComponentInitializeHandler initalizer = documentCustomization
					.getIComponentInitializeHandler();

			if (initalizer != null) {
				initalizer.intializeAuthorComponent(editorComponentProvider,
						documentCustomization);

			}

			lookupRegister = new LookupFactoryRegister(
					documentCustomization.getLookupFactory());

			cmsActions = customizationFactory.getCmsCustomization()
					.getCmsActions();

			String documentContent = cmsActions.loadDocument(
					documentCustomization, documentURI);

			setDocumentContent(toLoad.toString(), documentContent);

			OxygenConfiguration oxygenConfiguration = mainComponent
					.getOxygenConfiguration();
			String tagMode = oxygenConfiguration
					.getOxygenConfigurationValue("defaultTagView");

			if (tagMode != null) {
				TagMode mode = TagMode.fromMenuName(tagMode);
				ChangeTagModeAction.setDisplayTagMode(mode, this);
			}

			moveCursorToStartLocation(parameters);
			openDocumentParameters = parameters;
			
			//mainComponent.get
			

		} catch (AuthorComponentException e) {
			throw new OxygenIntegrationException(e);
		} catch (MalformedURLException e) {
			throw new OxygenIntegrationException(e);
		}
	}

	public OxygenOpenDocumentParmaters getOpenDocumentParameters() {
		return openDocumentParameters;
	}

	private void moveCursorToStartLocation(
			OxygenOpenDocumentParmaters parameters) {

		try {
			String xpath = parameters
					.getParameterValue(OxygenOpenDocumentParmatersNames.XPATH_START_LOCATION);
			
			if (!StringUtils.isEmpty(xpath)) {

				AuthorDocumentController documentController = authorAccess
						.getDocumentController();
				AuthorNode[] result = documentController.findNodesByXPath(
						xpath, true, true, true);

				if (result != null && result.length > 0) {

					AuthorNode node = result[0];

					result = documentController
							.findNodesByXPath("descendant::text()", node,
									false, true, true, true);

					if (result.length > 0) {
						node = result[0];
					}

					Rectangle rec = editorPage.modelToViewRectangle(node
							.getStartOffset());
					authorAccess.getEditorAccess().scrollToRectangle(rec);
					editorPage.setCaretPosition(node.getStartOffset());

					editorComponentProvider.getEditorComponent().requestFocus();
				}

			}
		} catch (AuthorOperationException e) {
			OxygenUtils.handleException(logger, e);
		}
	}

	public boolean isModified() {
		return isModified;
	}

	public WSAuthorComponentEditorPage getEditorPage() {
		return editorPage;
	}

	/**
	 * Get the XML content from the component
	 * 
	 * @return the XML content from the component
	 */
	public Reader getEditedDocument() {
		return AccessController.doPrivileged(new PrivilegedAction<Reader>() {
			public Reader run() {
				return editorComponentProvider.getWSEditorAccess()
						.createContentReader();
			}
		});
	}

	private void configure(EditorComponentProvider editorComponent) {

		String dummyActionKey = "Save action";

		JPanel component = ((javax.swing.JPanel) OxygenUtils
				.getAuthorComponentEditorPage(editorComponent)
				.getAuthorAccess().getEditorAccess().getAuthorComponent());

		component.getInputMap(JComponent.WHEN_FOCUSED).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S,

				KeyEvent.CTRL_DOWN_MASK), dummyActionKey);
		component.getActionMap().put(dummyActionKey,
				new SaveAction(mainComponent));

		// Reconfigures the toolbar when the document type detection changes.
		editorComponent
				.addAuthorComponentListener(new AuthorComponentListener() {
					public void modifiedStateChanged(boolean modified) {

						if (modified && !isModified) {
							setModifiedTitle();
							tabbedPane.repaint();
						}

						isModified = modified;

					}

					/**
					 * Reconfigures the actions toolbar
					 * 
					 * @see ro.sync.ecss.extensions.api.component.listeners.AuthorComponentListener#documentTypeChanged()
					 */
					public void documentTypeChanged() {
						documentViews.reconfigureActionsToolbar();
					}

					/**
					 * Reconfigures the actions toolbar
					 * 
					 * @see ro.sync.ecss.extensions.api.component.listeners.AuthorComponentListener#loadedDocumentChanged()
					 */
					public void loadedDocumentChanged() {
						documentViews.reconfigureActionsToolbar();
					}
				});

	}

	public void setModifiedTitle() {
		final int index = OxygenMainComponent.getTabIndex(this);

		final String title = tabbedPane.getTitleAt(index);
		IOxygenComponentToolbar actionsToolbar = documentViews
				.getComponentToolbar();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainComponent.getSystemButtons().changeButtonStatus(
						OxygenSystemToolbarButtons.BUTTON_SAVE, true);
				tabbedPane.setTitleAt(index, title + " *");
			}
		});
		this.isModified = true;
		mainComponent.checkDirty();

		editorComponentProvider.getWSEditorAccess().setModified(false);

		actionsToolbar.revalidate();
	}

	/**
	 * Public method, used from the JavaScript. It uses the AWT thread to do the
	 * real execution, since the security manager will not allow operations on
	 * the browser thread.
	 * 
	 * @param url
	 *            The URL of the file to load, can be null if XML content is
	 *            specified. If no XML content is given, the URL will be used
	 *            both to obtain the content and to solve relative references
	 *            (eg: images). If the XML content is also given, the URL will
	 *            only be used to solve relative references from the file.
	 * 
	 * @param xmlContent
	 *            The xml content.
	 * @throws Exception
	 */
	public void setDocumentContent(final String url, final String xmlContent)
			throws OxygenIntegrationException {
		final Exception[] recorded = new Exception[1];
		OxygenUtils.invokeOnAWT(new Runnable() {
			public void run() {
				try {
					setDocument(url, xmlContent != null
							&& xmlContent.length() > 0 ? new StringReader(
							xmlContent) : null);
				} catch (Exception ex) {
					recorded[0] = ex;
				}
			}
		});
		if (recorded[0] != null) {
			throw new OxygenIntegrationException(recorded[0]);
		}
	}

	public void restoreFrameTitle() {
		int index = OxygenMainComponent.getTabIndex(this);

		String title = tabbedPane.getTitleAt(index);
		tabbedPane.setTitleAt(index, title.substring(0, title.length() - 2));
		editorComponentProvider.getWSEditorAccess().setModified(false);
		isModified = false;
		mainComponent.checkDirty();
		mainComponent.getSystemButtons().changeButtonStatus(
				OxygenSystemToolbarButtons.BUTTON_SAVE, false);

	}

	/**
	 * Set a new content
	 * 
	 * @param xmlSystemId
	 *            The system ID (URL) of the XML content, used to solve images,
	 *            etc
	 * @param xmlContent
	 *            The reader over the content.
	 * @throws AuthorComponentException
	 */
	private void setDocument(final String xmlSystemId, final Reader xmlContent)
			throws AuthorComponentException {		
		final Exception[] ex = new Exception[1];
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				// Installs a document modification listener.
				try {
					
					URL documentURL = getDocumentURL(xmlSystemId);
					editorComponentProvider
							.load(documentURL, xmlContent);
				} catch (Exception e) {
					ex[0] = e;
				}
				return null;
			}

			private URL getDocumentURL(final String xmlSystemId)
					throws MalformedURLException {
				URL documentURL = null;
				
				if (StringUtils.isNotEmpty(xmlSystemId)){
					
					String systemId = xmlSystemId;
					
					if (systemId.contains("?")){
						systemId = xmlSystemId.substring(0, systemId.indexOf("?"));
					}
					
					documentURL = new URL(systemId);
				}
				return documentURL;
			}
		});
		if (ex[0] != null) {
			throw new AuthorComponentException(ex[0]);
		}
	};

	/**
	 * Get the content serialized back to XML from the component
	 * 
	 * @return The content serialized back to XML from the component
	 */
	public String getSerializedDocument() {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return OxygenIOUtils.getSerializedDocument(
						editorComponentProvider, documentCustomization);
			}
		});
	}

	public void clearModifyFlag() {
		isModified = false;
		mainComponent.checkDirty();
	}

	public OxygenDocumentViews getOxygenDocumentViews() {
		return documentViews;
	}

	public EditorComponentProvider getEditorComponentProvider() {
		return editorComponentProvider;
	}

	public TagMode getTagMode() {
		return tagMode;
	}

	public void setTagMode(TagMode tagMode) {
		this.tagMode = tagMode;
	}

	public IDocumentURI getDocumentUri() {
		return documentURI;
	}

	public IDocumentCustomization getDocumentCustomization() {
		return documentCustomization;
	}

	public ICmsCustomization getCMSCustomization() {
		return customizationFactory.getCmsCustomization();
	}

	public LookupFactoryRegister getLookupRegister() {
		return lookupRegister;
	}

	public AuthorAccess getAuthorAccess() {
		return authorAccess;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof OxygenDocument) {
			String documentIdToCompare = ((OxygenDocument) obj)
					.getDocumentUri().getDocumentURI();
			if (documentIdToCompare == null && documentId == null) {
				return true;
			}

			if (documentId != null) {
				return documentId.equals(documentIdToCompare);
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash
				+ (this.documentId != null ? this.documentId.hashCode() : 0);
		return hash;
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public AuthorDocumentController getOxygenDocumentController() {
		return authorAccess.getDocumentController();
	}

}
