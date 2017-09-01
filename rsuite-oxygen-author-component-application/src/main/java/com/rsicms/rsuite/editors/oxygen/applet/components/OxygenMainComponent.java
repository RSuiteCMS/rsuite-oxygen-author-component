package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;
import ro.sync.ecss.extensions.api.component.listeners.OpenURLHandler;

import com.rsicms.rsuite.editors.oxygen.applet.HtmlAction;
import com.rsicms.rsuite.editors.oxygen.applet.common.DocumentCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RsuiteCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.DialogHelper;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenAdditionalView;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenAdditionalViewContainer;
import com.rsicms.rsuite.editors.oxygen.applet.domain.OxygenOpenURLHanlder;
import com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent.AuthorComponentFactoryManager;
import com.rsicms.rsuite.editors.oxygen.applet.domain.shortcut.ShortCutManager;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;
import com.rsicms.rsuite.editors.oxygen.applet.extension.helpers.OxygenInfo;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

/**
 * Integration wiht Rsuite CMS
 * 
 * @author lukasz
 */
@SuppressWarnings("serial")
public class OxygenMainComponent extends JComponent implements
		IOxygenMainComponent {

	/** Logger. */
	static Logger logger = Logger
			.getLogger(OxygenMainComponent.class.getName());
	
	/** The author component factory */
	private AuthorComponentFactory authorComponentFactory;

	private AuthorComponentFactoryManager authorComponentFactoryManager;
	
	private ICustomizationFactory customizationFactory;

	private JFrame parentFrame;

	private String userName;

	private String projectName;

	private DialogHelper dialogHelper;

	private ICmsCustomization cmsCustomization;

	private static HtmlAction htmlAction;

	private JTabbedPane tabbedPane;

	private OxygenSystemToolbarButtons systemButtons;

	private OxygenConfiguration oxygenConfiguration;

	private OxygenDitaMapsMainComponent ditaMapManagerComponent;

	protected static volatile List<OxygenDocument> editedComponents = new CopyOnWriteArrayList<OxygenDocument>();

	private IOxygenComponentBuilder componentBuilder;

	private static OxygenMainComponent currentInstance;

	private static boolean initilized = false;

	private OxygenInfo oxygenInfo;

	public OxygenDocument getDocumentComponent(int index) {
		if (index > -1 && index < editedComponents.size()) {
			return editedComponents.get(index);
		}

		return null;
	}

	public OxygenDocument getActiveDocumentComponent() {

		int index = tabbedPane.getSelectedIndex();

		if (index == -1 && tabbedPane.getTabCount() > 0) {
			index = tabbedPane.getTabCount() - 1;
		}

		if (index > -1 && index < editedComponents.size()) {
			return editedComponents.get(index);
		}

		return null;

	}

	private OxygenMainComponent(URL[] frameworkZips, URL optionsZipURL,
			JFrame frame, OxygenAppletStartupParmaters startupParameters,
			OxygenOpenDocumentParmaters documentParameters,
			final ICustomizationFactory customizationFactory)
			throws AuthorComponentException, MalformedURLException,
			OxygenIntegrationException {

		this.customizationFactory = customizationFactory;
		cmsCustomization = customizationFactory.getCmsCustomization();

		oxygenInfo = new OxygenInfo();

		URL codeBase = new URL(
				startupParameters
						.getParameterValue(OxygenAppletStartupParmatersNames.BASE_URI));

		final ICmsURI cmsUri = customizationFactory.getCmsURI();

		oxygenConfiguration = new OxygenConfiguration(cmsUri);
		
		OpenURLHandler openURLHandler = new OxygenOpenURLHanlder(this, customizationFactory);

		
		authorComponentFactoryManager = 
				new AuthorComponentFactoryManager(customizationFactory, oxygenConfiguration, openURLHandler);
		
		authorComponentFactoryManager.initializeAuthorComponentFactory(frameworkZips, optionsZipURL, codeBase);
		authorComponentFactory = authorComponentFactoryManager.getAuthorComponentFactory();
		
		parentFrame = frame;

		this.dialogHelper = new DialogHelper(frame);
		this.customizationFactory = customizationFactory;

		componentBuilder = customizationFactory.getComponentBuilder(
				authorComponentFactory, this);

		if (componentBuilder == null) {
			componentBuilder = new OxygenMainComponentDefaultBuilder(
					authorComponentFactory, this);
		}

		componentBuilder.createAndConfigureMainPanel();

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				OxygenDocument activeDoc = getActiveDocumentComponent();

				if (activeDoc != null) {
					componentBuilder.setUpDocumentViews();
					if (htmlAction != null){
						htmlAction.setActiveDocument(activeDoc.getDocumentUri());
					}					
				}

			}
		});


		ditaMapManagerComponent = new OxygenDitaMapsMainComponent(
				authorComponentFactory, this, customizationFactory);
		ditaMapManagerComponent.setHtmlAction(htmlAction);
		OxygenMainComponentDefaultBuilder builder = (OxygenMainComponentDefaultBuilder) componentBuilder;
		OxygenAdditionalViewContainer container = builder
				.getAdditionalViewContainer(AdditionalView.DITA_MAP_MANAGER);
		if (container != null) {
			container.add(new OxygenAdditionalView(
					AdditionalView.DITA_MAP_MANAGER.getMenuDescription(),
					ditaMapManagerComponent));
		}


		systemButtons = new OxygenSystemToolbarButtons(this);	

		ShortCutManager shorcutManager = new ShortCutManager();
		shorcutManager.setup(this);
	        
	        
		currentInstance = this;
	}

	
	

	public String getUserName() {
		return userName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void closeApplication() {
		if (htmlAction != null) {
			htmlAction.closeWindow();
		}

		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.exit(0);
	}

	public void closeDocument(OxygenDocument oxygenDocument) {
		int index = editedComponents.indexOf(oxygenDocument);
		closeDocumentFromTab(index);
	}

	public void closeDocumentFromTab(int tabIndex) {

		OxygenDocument oxygenDocument = editedComponents.get(tabIndex);

		tabbedPane.remove(tabIndex);
		editedComponents.remove(tabIndex);

		if (editedComponents.size() == 0) {
			componentBuilder.cleanUpDocumentViews();
		}

		authorComponentFactory.disposeEditorComponentProvider(oxygenDocument
				.getEditorComponentProvider());
	}

	public static OxygenMainComponent initializeComponent(
			OxygenAppletStartupParmaters parameters,
			OxygenOpenDocumentParmaters documentParameters, boolean standAlone)
			throws OxygenIntegrationException {
		
		OxygenMainComponent oxygenComponent = null;
		try {
			JFrame frame = new JFrame("RSuite Oxygen Component");

			frame.setExtendedState(frame.getExtendedState()
					| JFrame.MAXIMIZED_BOTH);

			
			
			ClassLoader classLoader = OxygenMainComponent.class
					.getClassLoader();
			
			String customizationClass = parameters.getParameterValue(OxygenAppletStartupParmatersNames.CUSTOMIZATION_CLASS);
			
			ICustomizationFactory oxygenCustomization = OxygenUtils
					.loadCustomizationFactory(classLoader, customizationClass);
			// default implementation
			if (oxygenCustomization == null) {
				oxygenCustomization = new RsuiteCustomizationFactory();
			}

			oxygenCustomization.initialize(parameters);

			List<ISchemaAwareCustomizationFactory> factories = oxygenCustomization
					.getSchamaCustomizationFactories(documentParameters);
			if (factories != null) {
				for (ISchemaAwareCustomizationFactory factory : factories) {
					DocumentCustomizationFactory
							.registerSchemaCustomization(factory);
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			List<URL> frameworksURLs = new ArrayList<URL>();
			
			Enumeration<URL> frameworksEnumeration = classLoader
					.getResources("frameworks.zip");

			while (frameworksEnumeration.hasMoreElements()) {
				URL url = (URL) frameworksEnumeration.nextElement();
				frameworksURLs.add(url);
			}

			// Get the ZIP pointing to fixed options if available.
			URL optionsZipURL = classLoader.getResource("options.zip");

			
			URLStreamHandlerFactory urlHandlerFactory = oxygenCustomization
					.getURLHandlerFactory();

			if (urlHandlerFactory != null && !initilized ) {
				URL.setURLStreamHandlerFactory(urlHandlerFactory);
				initilized = true;
			}
			
			
			
				 oxygenComponent = new OxygenMainComponent(
							frameworksURLs.toArray(new URL[0]), optionsZipURL, frame,
							parameters, documentParameters, oxygenCustomization);
				 

			// sentLicenseLoginNotification(oxygenComponent,
			// oxygenCustomization);
			
		} catch (Exception e) {
			throw new OxygenIntegrationException(e);
		}

		return oxygenComponent;
	}

	public static void configureMainFrame(JFrame frame,
			final OxygenMainComponent oxygenComponent,
			final ICustomizationFactory oxygenCustomization) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(dim.width, dim.height);

		frame.getContentPane().add(oxygenComponent);
		frame.setVisible(true);
		

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();
				
				
				boolean isModified = false;

				for (OxygenDocument oxygenDocument : editedComponents) {
					isModified = isModified && oxygenDocument.isModified();
				}
				
				
				if (isModified) {

					Object[] options = { "Close Without Saving", "Cancel", };
					int n = JOptionPane.showOptionDialog(frame,
							"There is some unsaved content", "Warning",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
					if (n == JOptionPane.YES_OPTION) {
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					} else if (n == JOptionPane.CANCEL_OPTION) {
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

					}
				}
			}						
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void saveActiveDocument() {
		SaveAction action = new SaveAction(this);
		action.actionPerformed(null);
	}

	public DialogHelper getDialogHelper() {
		return dialogHelper;
	}

	public ICmsCustomization getCmsCustomization() {
		return cmsCustomization;
	}

	public ICustomizationFactory getCustomizationFactory() {
		return customizationFactory;
	}

	public void setHtmlAction(HtmlAction localHtmlAction) {
		htmlAction = localHtmlAction;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public void initTabComponent(JTabbedPane pane, int i) {
		int index = i - 1;

		pane.setTabComponentAt(index, new ButtonTabComponent(this, pane));
	}

	public void openDocumentInNewTab(OxygenOpenDocumentParmaters paramaters)
			throws OxygenIntegrationException {
		openDocumentInNewTab(paramaters, true);
	}

	public void openDocumentInNewTab(OxygenOpenDocumentParmaters parameters,
			boolean editable) throws OxygenIntegrationException {

		long sa = System.currentTimeMillis();
		
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		DocumentInfo documentInfo = new DocumentInfo(this, parameters);

		String documentUri = parameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);

		// select active tab if document already opened
		if (selectTabIfAlreadyOpen(documentUri)
				|| ditaMapManagerComponent.selectTabIfAlreadyOpen(documentInfo)) {
			return;
		}

		// TODO enable for dita map manager
		// boolean openInDitaMapManager = selectEditorForMap(documentInfo);
		//
		//
		// if (openInDitaMapManager){
		// ditaMapManagerComponent.openMapInNewManagerTab(this, parameters);
		// return;
		// }

		String title = parameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.TITLE);

		
		OxygenDocument component = new OxygenDocument(this, tabbedPane,
				parameters, editable);

		if (!editable) {
			title += " [read-only]";
		}

		
		editedComponents.add(component);
		tabbedPane.addTab(title, component.getEditorComponentProvider()
				.getEditorComponent());
		
		
		
		initTabComponent(tabbedPane, tabbedPane.getTabCount());

		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

		IDocumentHandler documentHandler = component.getDocumentCustomization()
				.getDocumentHandler();
		if (documentHandler != null) {
			documentHandler.afterOpenDocument(component.getDocumentUri(),
					parameters, component.getEditorComponentProvider());
			
		}

//		getActiveDocumentComponent().getEditorComponentProvider()
//				.getEditorComponent().requestFocusInWindow();

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public static void checkDirty() {
		boolean modified = false;
		for (OxygenDocument doc : editedComponents) {
			modified = doc.isModified() || modified;
		}
		setDirty(modified);
	}

	public static void setDirty(boolean dirty) {
		if (htmlAction != null){
			htmlAction.setDirty(dirty);
		}		
	}

	private boolean selectEditorForMap(DocumentInfo documentInfo) {
		boolean openInDitaMapManager = false;

		if (isDitaMap(documentInfo)) {

			Object[] options = { "DITA Maps Manager", "Editor" };
			int n = JOptionPane.showOptionDialog(parentFrame,
					"Where do you want the DITA map file to be opened?",
					"Select editor", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (n == 0) {
				openInDitaMapManager = true;
			}

		}
		return openInDitaMapManager;
	}

	// TODO move to another class
	private boolean isDitaMap(DocumentInfo documentInfo) {
		QName rootElement = documentInfo.getRootElement();
		String localPart = rootElement.getLocalPart();

		if ("map".equalsIgnoreCase(localPart)
				|| "bookmap".equalsIgnoreCase(localPart)
				|| "subjectScheme".equalsIgnoreCase(localPart)) {
			return true;
		}

		List<String> publicIds = new ArrayList<String>();
		publicIds.add("-//OASIS//DTD DITA Map");
		publicIds.add("-//OASIS//DTD DITA BookMap");
		publicIds.add("-//OASIS//DTD DITA Subject Scheme Map");
		publicIds.add("-//OASIS//DTD DITA Learning Map");

		String schemaPublicId = documentInfo.getPublicSchemaId();

		if (schemaPublicId != null) {

			for (String publicIdMatch : publicIds) {
				if (schemaPublicId.contains(publicIdMatch)) {
					return true;
				}
			}
		}

		List<Attribute> attributes = documentInfo.getAttributes();

		for (Attribute attribute : attributes) {
			QName name = attribute.getName();
			String value = attribute.getValue();
			if ("class".equalsIgnoreCase(name.getLocalPart()) && value != null
					&& value.contains("map/map")) {
				return true;
			}
		}

		return false;
	}

	private boolean selectTabIfAlreadyOpen(String documentUri) {
		OxygenDocument oxygenDocument = new OxygenDocument(documentUri);

		int index = editedComponents.indexOf(oxygenDocument);

		if (index > -1) {
			tabbedPane.setSelectedIndex(index);
			return true;
		}

		return false;

	}

	public static int getTabIndex(OxygenDocument documentInfo) {
		return editedComponents.indexOf(documentInfo);

	}

	public IOxygenComponentBuilder getComponentBuilder() {
		return componentBuilder;
	}

	public JFrame getParentFrame() {
		return parentFrame;
	}

	public AuthorComponentFactoryManager getAuthorComponentFactoryManager() {
		return authorComponentFactoryManager;
	}

	public OxygenConfiguration getOxygenConfiguration() {
		return oxygenConfiguration;
	}

	public OxygenSystemToolbarButtons getSystemButtons() {
		return systemButtons;
	}

	public OxygenDitaMapsMainComponent getDitaMapManagerComponent() {
		return ditaMapManagerComponent;
	}

	public static OxygenMainComponent getCurrentInstance() {
		return currentInstance;
	}

	public OxygenInfo getOxygenInfo() {
		return oxygenInfo;
	}

	public static List<OxygenDocument> getEditedComponents() {
		return editedComponents;
	}	
}
