package com.rsicms.rsuite.editors.oxygen.applet.components.tree;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetLookup;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ValidateAndAssignReferenceElementAction;

public class LinkTargetTabPanel extends JPanel implements ISelectableComponent,
		TreeDialogConstans {

	public static final String PANEL_NAME_LINK_TARGET = "Link Target Panel";

	/** UID. */
	private static final long serialVersionUID = 2419209070043813605L;

	private Logger logger = Logger.getLogger(this.getClass());

	private JComboBox dropDownList;

	private DefaultListModel linkTargetsListModel;

	private JList linkTargetsList;

	private JButton insertRefrenceButton;

	private IReferenceTargetLookup linkTargetLookup;

	private JEditorPane linkTargetInformationPane;

	private List<IReferenceTargetElement> targetNodesList;
	
	private IReposiotryResource currentNode;
	
	private InsertReferenceElement element;
	
	private AuthorAccess authorAccess;
	
	private IDocumentURI documentUri;
	
	private OxygenDocument documentComponent;
	
	private JSplitPane splitPane;

	public LinkTargetTabPanel(AuthorAccess authorAccess,
			TreeLookupDialog parentDialog, InsertReferenceElement element) {

		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);
		
		documentComponent = context.getDocumentComponent();
		documentUri = documentComponent.getDocumentUri();
		
		setLayout(new BorderLayout());
		setUpTopPanel();
		setUpCenterPanel();
		setupButtomPanel(authorAccess, parentDialog, element);
		this.element = element;
		this.authorAccess = authorAccess;
		

		setName(PANEL_NAME_LINK_TARGET);
	}

	/**
	 * Sets up central panel - the main list that presents target elements
	 */
	public void setUpCenterPanel() {

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		linkTargetLookup = documentComponent.getLookupRegister().getIReferenceTargetLookup();
		linkTargetsListModel = new DefaultListModel();

		// Create the list and put it in a scroll pane.
		linkTargetsList = new JList(linkTargetsListModel);
		linkTargetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		linkTargetsList.setSelectedIndex(0);
		linkTargetsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				IReferenceTargetElement node = (IReferenceTargetElement) linkTargetsList
						.getSelectedValue();
				if (node != null && node.getTitle() != null) {
					linkTargetInformationPane.setText(node.getTitle());
					insertRefrenceButton.setEnabled(true);
				}

			}
		});

		JScrollPane listScrollPane = new JScrollPane(linkTargetsList);
		splitPane.setLeftComponent(listScrollPane);

		linkTargetInformationPane = new JEditorPane();
		linkTargetInformationPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(linkTargetInformationPane);
		splitPane.setRightComponent(htmlView);

		add(splitPane, BorderLayout.CENTER);
	}

	/**
	 * Create top panel with the drop down list.
	 * 
	 * @return
	 */
	private void setUpTopPanel() {

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());

		// drop down list label
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.ipady = 1;

		JLabel label = new JLabel(TARGET_TYPE_LABEL);
		topPanel.add(label, c);

		// drop down list
		dropDownList = new JComboBox();
		dropDownList.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String selectedElement = (String) dropDownList
						.getSelectedItem();
				if (selectedElement != null) {
					loadList(selectedElement);
				}
				linkTargetInformationPane.setText("");
				insertRefrenceButton.setEnabled(false);
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 2.5;
		c.gridx = 1;
		c.ipady = 1;

		topPanel.add(dropDownList, c);

		add(topPanel, BorderLayout.NORTH);
	}

	/**
	 * Sets up the bottom panel - with action buttons.
	 * 
	 * @param authorAccess
	 * @param parentDialog
	 * @param element
	 */
	private void setupButtomPanel(AuthorAccess authorAccess,
			TreeLookupDialog parentDialog, InsertReferenceElement element) {

		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new GridLayout(2, 1));

		JPanel panelButtonSelectTag = new JPanel();

		insertRefrenceButton = new JButton(INSERT_CROSS_REFERENCE_BUTTON_LABEL);
		insertRefrenceButton.setEnabled(false);
		
		insertRefrenceButton
		.addActionListener(new ValidateAndAssignReferenceElementAction(
				authorAccess, parentDialog, element, this));
		
		

		JButton button = new JButton(TreeDialogConstans.CANCEL_BUTTON_LABEL);
		button.addActionListener(parentDialog);

		panelButtonSelectTag.add(insertRefrenceButton);
		panelButtonSelectTag.add(button);

		panelButtons.add(panelButtonSelectTag);
		add(panelButtons, BorderLayout.SOUTH);
	}

	public void refreshDataPanel(){
		linkTargetsList.repaint();
		splitPane.repaint();
	}
	
	public void loadDataToPanel(IReposiotryResource userNode) {
		try {

			resetPanel();			
			targetNodesList = linkTargetLookup.getReferenceTargetElementList(authorAccess, userNode, element);
			Set<String> elementsName = new TreeSet<String>();

			for (IReferenceTargetElement targetNode : targetNodesList) {
				linkTargetsListModel.addElement(targetNode);
				elementsName.add(targetNode.getElementName());
			}

			dropDownList.addItem(ALL_LABEL);

			for (String elementName : elementsName) {
				dropDownList.addItem(elementName);
			}
			
			currentNode = userNode;
			
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
	}

	/**
	 * Resets - clears all data - the panel
	 */
	public void resetPanel() {
		linkTargetsListModel.removeAllElements();
		dropDownList.removeAllItems();
		insertRefrenceButton.setEnabled(false);
		linkTargetInformationPane.setText("");
	}

	/**
	 * Loads target elements for a specific element
	 * 
	 * @param elementName
	 */
	private void loadList(String elementName) {
		linkTargetsListModel.removeAllElements();
		for (IReferenceTargetElement targetNode : targetNodesList) {
			if (elementName == null
					|| ALL_LABEL.equalsIgnoreCase(elementName)
					|| elementName
							.equalsIgnoreCase(targetNode.getElementName())) {
				linkTargetsListModel.addElement(targetNode);
			}

		}
	}

	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {
		
		boolean contextNode = StringUtils.equalsIgnoreCase(documentUri.getEditedDocumentId(), currentNode.getCMSid());
		
		return new SelectedNode(currentNode, (IReferenceTargetElement) linkTargetsList
				.getSelectedValue(), contextNode);
	}

}
