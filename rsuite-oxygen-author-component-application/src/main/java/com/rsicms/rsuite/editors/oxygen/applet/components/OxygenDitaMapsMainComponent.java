package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.HtmlAction;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenMapContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAction;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

/**
 * Sample making use of a DITA Map tree component.
 */
@SuppressWarnings("serial")
public class OxygenDitaMapsMainComponent extends JPanel implements IOxygenMainComponent {
	
	private static int maxW = 0;
    private static int maxH = 0;

	private JTabbedPane mapsTabbedPane;

	private JPanel mainDitaMapPanel;

	private AuthorComponentFactory authorComponentFactory;

	private JPanel toolbarContainer;

	private ICustomizationFactory customizationFactory;
	
	private OxygenMapManagerSystemToolbarButtons systemButtons;
	
	private OxygenMainComponent mainComponent;
	
	/**
	 * Logger.
	 */
	private static Logger logger = Logger
			.getLogger(OxygenDitaMapsMainComponent.class.getName());

	protected static List<OxygenDitaMapDocument> editedComponents = new CopyOnWriteArrayList<OxygenDitaMapDocument>();
	private static HtmlAction htmlAction;

	public IOxygenDocument getDocumentComponent(int index) {
		if (index > -1 && index < editedComponents.size()) {
			return editedComponents.get(index);
		}

		return null;
	}

	public OxygenDitaMapDocument getActiveDocumentComponent() {

		int index = mapsTabbedPane.getSelectedIndex();

		if (index == -1 && mapsTabbedPane.getTabCount() > 0) {
			index = mapsTabbedPane.getTabCount() - 1;
		}

		if (index > -1 && index < editedComponents.size()) {
			return editedComponents.get(index);
		}

		return null;

	}

	public void initialize() {
		setLayout(new BorderLayout());
		mapsTabbedPane = new JTabbedPane();

		mainDitaMapPanel = new JPanel(new BorderLayout());

		mapsTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				OxygenDitaMapDocument activeDoc = getActiveDocumentComponent();
				if (activeDoc != null) {
					setUpDocumentViews();
				}
			}
		});

		toolbarContainer = new JPanel(new BorderLayout());

		mainDitaMapPanel.add(toolbarContainer, BorderLayout.NORTH);

		mainDitaMapPanel.add(mapsTabbedPane, BorderLayout.CENTER);
		
		add(mainDitaMapPanel);
		
		final Dimension originalTabsDim = mapsTabbedPane.getPreferredSize();
		
		mapsTabbedPane.addChangeListener(new ChangeListener() {

	            @Override
	            public void stateChanged(ChangeEvent e) {

	                Component p =   ((JTabbedPane) e.getSource()).getSelectedComponent();
	                Dimension panelDim = p.getPreferredSize();

	                Dimension nd = new Dimension(
	                        originalTabsDim.width - ( maxW - panelDim.width),
	                        originalTabsDim.height - ( maxH - panelDim.height) );

	                mapsTabbedPane.setPreferredSize(nd);
	            }

	        });

		systemButtons = new OxygenMapManagerSystemToolbarButtons(mainComponent);
	}
	public static void checkDirty() {
		boolean modified = false;
		for (OxygenDitaMapDocument doc : editedComponents) {
			modified = doc.isModified() || modified;
		}
		setDirty(modified);
	}

	public static void setDirty(boolean dirty) {
		htmlAction.setDirty(dirty);
	}
	

	private void setUpDocumentViews() {
		OxygenDitaMapDocument activeDoc = getActiveDocumentComponent();

		if (activeDoc == null) {
			return;
		}

		toolbarContainer.removeAll();

		//toolbarContainer.setLayout(new FlowLayout());
		
		activeDoc.reconfigureActionsToolbar();
		toolbarContainer.add(activeDoc.getActionsToolbar());

		toolbarContainer.repaint();
		toolbarContainer.revalidate();

		invalidate();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				invalidate();
				revalidate();
				repaint();
			}
		});
	}
	
	public boolean selectTabIfAlreadyOpen(DocumentInfo documentInfo){
		
		OxygenDitaMapDocument mapDocument = new OxygenDitaMapDocument(documentInfo.getDocumentURI().getDocumentURI());

		int index = editedComponents.indexOf(mapDocument);

		if (index > -1) {
			mapsTabbedPane.setSelectedIndex(index);
			return true;
		}
		
		return false;
	}
	
	public static int getTabIndex(OxygenDitaMapDocument documentInfo) {
		return editedComponents.indexOf(documentInfo);

	}
	

	public void openMapInNewManagerTab(OxygenMainComponent mainComponent, OxygenOpenDocumentParmaters paramaters)
			throws Exception {

		String documentUri = paramaters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);

		OxygenDitaMapDocument documentInfo = new OxygenDitaMapDocument(documentUri);

		int index = editedComponents.indexOf(documentInfo);

		if (index > -1) {
			mapsTabbedPane.setSelectedIndex(index);
			return;
		}

		OxygenDitaMapDocument component = new OxygenDitaMapDocument(this,
				authorComponentFactory, customizationFactory, paramaters, mapsTabbedPane);

		String title = paramaters
				.getParameterValue(OxygenOpenDocumentParmatersNames.TITLE);

		mapsTabbedPane.addTab(title, component.getDitaMapComponent()
				.getEditorComponent());
		
		initTabComponent(mapsTabbedPane, mapsTabbedPane.getTabCount());

		editedComponents.add(component);
	
		
		setUpDocumentViews();
		
		OxygenMapContext context = new OxygenMapContext(mainComponent, component);
		OxygenComponentRegister.registerOxygenEditorContext(component.getDitaMapComponent(), context);
	}

	public void initTabComponent(JTabbedPane pane, int i) {
		int index = i - 1;

		pane.setTabComponentAt(index, new ButtonTabComponent(this, pane));
	}

	/**
	 * Constructor. Builds the sample.
	 * 
	 * @throws AuthorComponentException
	 */
	public OxygenDitaMapsMainComponent(AuthorComponentFactory factory,
			OxygenMainComponent mainComponent,
			ICustomizationFactory customizationFactory)
			throws AuthorComponentException {

		authorComponentFactory = factory;
		this.customizationFactory = customizationFactory;
		this.mainComponent = mainComponent;
		
		initialize();

	}

	public JTabbedPane getMapsTabbedPane() {
		return mapsTabbedPane;
	}

	public OxygenMapManagerSystemToolbarButtons getSystemButtons() {
		return systemButtons;
	}

	@Override
	public JFrame getParentFrame() {
		return mainComponent.getParentFrame();
	}

	@Override
	public void saveActiveDocument() {
		SaveAction action = new SaveAction(mainComponent, true);
		action.actionPerformed(null);		
	}

	public void setHtmlAction(HtmlAction localHtmlAction) {
		htmlAction = localHtmlAction;
	}

	@Override
	public void closeDocumentFromTab(int tabIndex) {
		// TODO Auto-generated method stub
	}
	
}
