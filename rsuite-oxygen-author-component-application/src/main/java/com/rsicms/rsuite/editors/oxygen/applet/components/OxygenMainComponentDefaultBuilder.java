package com.rsicms.rsuite.editors.oxygen.applet.components;

import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ATTRIBUTES;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ELEMENTS;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.OUTLINE;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.REVIEW;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.VALIDATION_PROBLEMS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;

import com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenAdditionalViewContainer;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenDocumentViews;

public class OxygenMainComponentDefaultBuilder implements
		IOxygenComponentBuilder {

	/** The author component factory */
	protected final AuthorComponentFactory factory;

	protected final OxygenMainComponent mainComponent;

	private JSplitPane editorAndOutlineSplit;

	/** Split pane for the attributes and elements lists. */
	private JSplitPane attributesAndElementsSplit;

	private JPanel samplePanel;

	/**
	 * Split pane for the editing area and review.
	 */
	private JSplitPane editorAndReviewSplit;
	
	private JSplitPane outlineAndDitaMapSplit;

	private JPanel toolbarContainer;

	private Map<AdditionalView, OxygenAdditionalViewContainer> viewContainersMap = new HashMap<AdditionalView, OxygenAdditionalViewContainer>();

	private static final int DIVIDER_SIZE = 7;

	private JPanel initializeContainer() {
		return new JPanel(new BorderLayout());
	}

	public OxygenMainComponentDefaultBuilder(

	AuthorComponentFactory factory, OxygenMainComponent mainComponent) {

		this.factory = factory;
		this.mainComponent = mainComponent;
	}

	/**
	 * The split pane between the (outline + editor) and (attributes + elements)
	 */
	private JSplitPane centerPanel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentBuilder
	 * #createAndConfigureMainPanel()
	 */
	@Override
	public void createAndConfigureMainPanel() {
		mainComponent.setLayout(new BorderLayout());
		mainComponent.add(createMainPanel());
	}

	/**
	 * Creates a sample editor panel.
	 * 
	 * @param addStatus
	 *            Add the status bar
	 * @param addToolbar
	 *            True to add a toolbar
	 * @return The sample panel
	 */
	protected JPanel createMainPanel() {
		samplePanel = initializeContainer();
		
		if (addHelperViews()) {

			// Attributes and elements.
			Component attributesAndElementsSplit = createAttributesAndElementComponent();
			Component outlineAndEditorSplit = createEditorAndOutlineComponent();

			// Center panel containing components and additional views
			centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			centerPanel.setBorder(BorderFactory.createEmptyBorder());
			centerPanel.setDividerLocation(0.8);
			centerPanel.setDividerSize(DIVIDER_SIZE);
			centerPanel.setResizeWeight(1);
			centerPanel.setLeftComponent(outlineAndEditorSplit);
			centerPanel.setRightComponent(attributesAndElementsSplit);

			samplePanel.add(centerPanel, BorderLayout.CENTER);
			toolbarContainer = initializeContainer();
			samplePanel.add(toolbarContainer, BorderLayout.NORTH);

			OxygenAdditionalViewContainer validationContainer = initializeViewContainer(VALIDATION_PROBLEMS);

			samplePanel.add(validationContainer, BorderLayout.SOUTH);

		} else {
			JTabbedPane tabbedPane = setUpEditorTabPane();
			samplePanel.add(tabbedPane, BorderLayout.CENTER);
		}

		return samplePanel;
	}

	public void cleanUpDocumentViews() {
		
		toolbarContainer.removeAll();
		JToolBar tollbar = new JToolBar();
		
		OxygenSystemToolbarButtons systemButtons = mainComponent.getSystemButtons();
		for (Component component :  systemButtons.getSystemButtons()){
			tollbar.add(component);
			if (component instanceof OxygenButton && ((OxygenButton)component).isDocumentButton()){
				component.setEnabled(false);
			}
		}
		
		for (Component component :  systemButtons.getEndSystemButtonsMap()){
			tollbar.add(component);
		}

		toolbarContainer.add(tollbar);
		
		
		for (OxygenAdditionalViewContainer container : viewContainersMap.values()){
			container.removeAll();
		} 		
		
		repaintMainComponent();
	}

	public void setUpDocumentViews() {
		OxygenDocument oxygenDocument = mainComponent
				.getActiveDocumentComponent();

		if (oxygenDocument == null) {
			return;
		}
		OxygenDocumentViews documentViews = oxygenDocument
				.getOxygenDocumentViews();

		for (AdditionalView view : AdditionalView.values()) {
			JPanel container = viewContainersMap.get(view);
			JPanel viewPanel = documentViews.getAdditionalView(view);

			if (container != null && viewPanel != null) {
				container.removeAll();
				container.add(viewPanel);
			}
		}

		toolbarContainer.removeAll();

		IOxygenComponentToolbar componentToolbar = documentViews
				.getComponentToolbar();
		if (addToolbar()) {
			documentViews.reconfigureActionsToolbar();
			toolbarContainer.add(componentToolbar.getActionsToolbar());
		}
		

		JPanel container = viewContainersMap.get(AdditionalView.DITA_MAP_MANAGER);
		if (container != null){
			viewContainersMap.get(AdditionalView.DITA_MAP_MANAGER).repaint();
			viewContainersMap.get(AdditionalView.DITA_MAP_MANAGER).revalidate();
		}
		
		repaintMainComponent();
	}

	private void repaintMainComponent() {
		mainComponent.invalidate();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainComponent.invalidate();
				mainComponent.revalidate();
				mainComponent.repaint();
			}
		});
	}

	public static WSAuthorComponentEditorPage getAuthorComponentEditorPage(
			EditorComponentProvider editorComponentProvider) {
		return ((WSAuthorComponentEditorPage) editorComponentProvider
				.getWSEditorAccess().getCurrentPage());
	}



	protected Component createAttributesAndElementComponent() {
		// Attributes and elements.
		attributesAndElementsSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				true);
		attributesAndElementsSplit.setBorder(BorderFactory.createEmptyBorder());
		attributesAndElementsSplit.setDividerLocation(0.5);
		attributesAndElementsSplit.setDividerSize(DIVIDER_SIZE);
		attributesAndElementsSplit.setResizeWeight(0.5);
		attributesAndElementsSplit.setPreferredSize(new Dimension(200,
				attributesAndElementsSplit.getPreferredSize().height));

		OxygenAdditionalViewContainer elementContainer = initializeViewContainer(ELEMENTS);
		OxygenAdditionalViewContainer attributeContainer = initializeViewContainer(ATTRIBUTES);

		attributesAndElementsSplit.setTopComponent(attributeContainer);
		attributesAndElementsSplit.setBottomComponent(elementContainer);
		
		if ( !(attributeContainer.isVisible() || elementContainer.isVisible())){
			attributesAndElementsSplit.setVisible(false);
		}

		return attributesAndElementsSplit;
	}

	private OxygenAdditionalViewContainer initializeViewContainer(
			AdditionalView view) {
		OxygenAdditionalViewContainer viewContainer = new OxygenAdditionalViewContainer(
				view);
		if (!getDefaultViews().contains(view)){
			viewContainer.setVisible(false);			
		}
		
		if (availableViews().contains(view)){
			viewContainersMap.put(view, viewContainer);
		}
		
		
		return viewContainer;
	}

	protected Component createEditorAndReviewComponent() {

		editorAndReviewSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		editorAndReviewSplit.setBorder(BorderFactory.createEmptyBorder());
		editorAndReviewSplit.setDividerSize(DIVIDER_SIZE);
		editorAndReviewSplit.setDividerLocation(0.8);
		editorAndReviewSplit.setResizeWeight(1);

		JTabbedPane tabbedPane = setUpEditorTabPane();

		editorAndReviewSplit.setLeftComponent(tabbedPane);

		OxygenAdditionalViewContainer reviewContainer = initializeViewContainer(REVIEW);
		editorAndReviewSplit.setRightComponent(reviewContainer);

		return editorAndReviewSplit;
	}

	private JTabbedPane setUpEditorTabPane() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setName("tabbed_pane_oxygen");
		
		mainComponent.setTabbedPane(tabbedPane);
		return tabbedPane;
	}

	protected Component createEditorAndOutlineComponent() {

		editorAndOutlineSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true);
		editorAndOutlineSplit.setBorder(BorderFactory.createEmptyBorder());
		editorAndOutlineSplit.setDividerSize(DIVIDER_SIZE);
		editorAndOutlineSplit.setDividerLocation(250);
		editorAndOutlineSplit.setResizeWeight(0);
		
		

		outlineAndDitaMapSplit =  new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					true);
		outlineAndDitaMapSplit.setBorder(BorderFactory.createEmptyBorder());
		outlineAndDitaMapSplit.setDividerSize(DIVIDER_SIZE);
		outlineAndDitaMapSplit.setDividerLocation(250);
		outlineAndDitaMapSplit.setResizeWeight(0);
		outlineAndDitaMapSplit.setVisible(true);
		
		

		OxygenAdditionalViewContainer outlineContainer = initializeViewContainer(
				OUTLINE);
		viewContainersMap.put(OUTLINE, outlineContainer);
		

		OxygenAdditionalViewContainer ditaMapManagerContainer = initializeViewContainer(
				AdditionalView.DITA_MAP_MANAGER);	
		
		outlineAndDitaMapSplit.setRightComponent(outlineContainer);
		outlineAndDitaMapSplit.setLeftComponent(ditaMapManagerContainer);
		
		if ( ! (outlineContainer.isVisible() || ditaMapManagerContainer.isVisible())){
			outlineAndDitaMapSplit.setVisible(false);
		}
		
		editorAndOutlineSplit.setLeftComponent(outlineAndDitaMapSplit);

		editorAndOutlineSplit
				.setRightComponent(createEditorAndReviewComponent());

		return editorAndOutlineSplit;
	}

	public JSplitPane getEditorAndOutlineSplit() {
		return editorAndOutlineSplit;
	}

	public JSplitPane getAttributesAndElementsSplit() {
		return attributesAndElementsSplit;
	}

	public JSplitPane getCenterPanel() {
		return centerPanel;
	}

	protected boolean addStatus() {
		return false;
	}

	protected boolean addToolbar() {
		return true;
	}

	protected boolean addHelperViews() {
		return true;
	}
	
	protected Set<AdditionalView> availableViews(){
		Set<AdditionalView> availableViews = new HashSet<AdditionalView>();
		availableViews.add(OUTLINE);
		availableViews.add(ATTRIBUTES);
		availableViews.add(ELEMENTS);
		availableViews.add(REVIEW);
		//availableViews.add(AdditionalView.DITA_MAP_MANAGER);
		
		return availableViews;
	}
	
	protected Set<AdditionalView> getDefaultViews(){
		Set<AdditionalView> availableViews = new HashSet<AdditionalView>();
		
		List<String> values = mainComponent.getOxygenConfiguration().getOxygenConfigurationValueList("defaultViewVisibility");
		if (values != null){
			availableViews.clear();
			for (String value : values){
				AdditionalView view = AdditionalView.fromString(value);
				if (view != null){
					availableViews.add(view);
				}				
			}
		}		
		
		return availableViews;
	}

	public OxygenAdditionalViewContainer getAdditionalViewContainer(
			AdditionalView view) {
		return viewContainersMap.get(view);
	}
	
	public Set<AdditionalView> getViewContainerKeySet() {
		return viewContainersMap.keySet();
	}

	public JSplitPane getEditorAndReviewSplit() {
		return editorAndReviewSplit;
	}

	public JSplitPane getOutlineAndDitaMapSplit() {
		return outlineAndDitaMapSplit;
	}

}
