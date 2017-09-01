package com.rsicms.rsuite.editors.oxygen.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.MalformedURLException;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.domain.logging.LogConfigurator;
import com.rsicms.rsuite.editors.oxygen.applet.extension.launch.OpenDocumentManager;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

/**
 * Demonstrates the use of the author component as an applet.
 */
@SuppressWarnings("serial")
public class OxygenApplet extends JApplet {

	private HtmlAction htmlAction = null;
	
	/** Logger. */
	private Logger logger;
	
	/**
	 * The sample component wrapper
	 */
	private OxygenMainComponent oxygenComponent;
	
	private OpenDocumentManager openDocumentManager;

	public OxygenApplet() {
		 LogConfigurator logConfigurator = new LogConfigurator();
		 logConfigurator.configureConsoleLogging();
		 logger = Logger
					.getLogger(OxygenApplet.class.getName());
	}
	
	/**
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGui();
				}
			});
		} catch (Exception e) {
			logger.error(e,e);
		}
		
		//TODO
		try{
			throw new Exception("Test error");
		}catch(Exception e){
			logger.error(e,e);
		}
		
		super.init();
	}

	public void createGui() {
		try {
			// No restrictions to this signed applet
			System.setSecurityManager(null);

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			// A gray to match the web page.
			Color gray = new Color(240, 240, 240);
			UIManager.put("Panel.background", gray);
			UIManager.put("ToolBar.background", gray);
			UIManager.put("SplitPaneDivider.background", gray);
			UIManager.put("ScrollBar.background", gray);

			// The document base
			startAppletInit();
		} catch (Throwable t) {
			logger.error(t,t);
		}
	}

	/**
	 * Getter used from JavaScript
	 * 
	 * @return The author component sample. Can be <code>null</code> if the
	 *         AuthorComponent did not initialized propertly, for instance a
	 *         licens problem.
	 */
	public OxygenMainComponent getAuthorComponentSample() {
		return oxygenComponent;
	}

	/**
	 * Initialize the main applet components on a thread.
	 */
	private void startAppletInit() {
		final JPanel progressPanel = new JPanel(new GridLayout(2, 1));
		progressPanel.add(createInitializeLabel());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar);
		add(progressPanel, BorderLayout.NORTH);
		htmlAction = createHtmlAction();
		new Thread() {
			public void run() {
				try {
					initializeComponent();

					// Signal to Javascript loading has ended.
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								if (oxygenComponent != null) {
									add(oxygenComponent);
								}

								remove(progressPanel);
								// Reconfigure
								invalidate();
								validate();
								
								oxygenComponent.getActiveDocumentComponent().getEditorComponentProvider().getEditorComponent().requestFocus();							

							} catch (Throwable t) {
								logger.error(t, t);
							}
						}
					});
				} catch (Exception e) {
					JOptionPane.showMessageDialog(OxygenApplet.this,
							"AuthorComponent problem: " + e.getMessage(),
							"Cannot create applet", JOptionPane.ERROR_MESSAGE);
				} catch (Throwable e) {
					logger.error(e, e);
				}
			}

			public void initializeComponent() throws AuthorComponentException,
					OxygenIntegrationException, MalformedURLException {
				
				OxygenAppletStartupParmaters startupParameters = new OxygenAppletStartupParmaters();
				OxygenOpenDocumentParmaters documentParameters = new OxygenOpenDocumentParmaters();
				
				for (OxygenAppletStartupParmatersNames parameter : OxygenAppletStartupParmatersNames.values()){
					 String value = getParameter(parameter.getName());
					 
					 if (value == null){
						 throw new OxygenIntegrationException("Missing startup paramter: " + parameter.getName());
					 }
					 
					 startupParameters.addParameter(parameter, value);
				}
				
				for (OxygenOpenDocumentParmatersNames parameter : OxygenOpenDocumentParmatersNames.values()){
					 String value = getParameter(parameter.getName());
					 
					 if (value == null && parameter.isRequired()){
						 throw new OxygenIntegrationException("Missing startup paramter: " + parameter.getName());
					 }
					 
					 documentParameters.addParameter(parameter, value);
				}
				
				
				try {

					boolean standalone = false;

					System.setSecurityManager(null);
					OxygenMainComponent component = OxygenMainComponent
							.initializeComponent(startupParameters, documentParameters, standalone);

					
					oxygenComponent = component;
					oxygenComponent.setHtmlAction(htmlAction);
					oxygenComponent.openDocumentInNewTab(documentParameters);
					openDocumentManager = new OpenDocumentManager(component);
					

				} catch (Exception e) {
					logger.error(e, e);
				}

			}
		}.start();
		
	}

	private JLabel createInitializeLabel() {
		JLabel label = new JLabel("Initializing...");
		label.setName("initialize");
		return label;
	}

	protected HtmlAction createHtmlAction() {
		return new HtmlAction(getAppletContext());
	}

	public void clean() {
		if (oxygenComponent != null) {
			oxygenComponent.closeApplication();
		}
	}

	public void openDocumentInNewTab(final String moId){
		try {
			openDocumentManager.openDocumentInANewTab(moId);
		} catch (OxygenIntegrationException e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}
	}
	
	public void openDocumentInNewTab(final String documentUri, final  String title, final String systemId, final  String publicId, final  String schemaId, final String xpathStartLocation, final String moReferenceId){
					
					OxygenOpenDocumentParmaters documentParameters = new OxygenOpenDocumentParmaters();

					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.DOCUMENT_URI, documentUri);
					htmlAction.setActiveDocument(documentUri);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.TITLE, title);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID, publicId);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID, systemId);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.SCHEMA_ID, schemaId);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.XPATH_START_LOCATION, xpathStartLocation);
					documentParameters.addParameter(OxygenOpenDocumentParmatersNames.MO_REFERENCE_ID, moReferenceId);					
					
					if (oxygenComponent != null) {
						try {
							oxygenComponent.openDocumentInNewTab(documentParameters);
						} catch (Exception e) {
							OxygenUtils.handleException(logger, e);
						}
					}
	}
	
	
	/**
	 * @see java.applet.Applet#destroy()
	 */
	@Override
	public void destroy() {
		AuthorComponentFactory factory = AuthorComponentFactory.getInstance();
		factory.dispose();
		super.destroy();
		clean();
	}
	
	/**
	 * Calling this from Javascript causes the applet to throw a security warning the first time; we want to do this on init.
	 */
	public void getSecurityPermissions() {
		
	}

}
