package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.crossRefernce;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetLookup;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.ASearchPanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.SearchResultPanelConfigurator;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTable;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.EditedRepositoryResource;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.CloseDialogAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ValidateAndAssignReferenceElementAction;

public class SearchCrossReferncePanel extends ASearchPanel implements ISelectableComponent {

	public static final String ALL_LABEL = "All";
	/** UID */
	private static final long serialVersionUID = -1764224054792750944L;

	private JTextField filterText;

	private SearchResultTable table;

	private InsertReferenceElement element;

	private IReferenceTargetLookup linkTargetLookup;

	private JComboBox dropDownList;

	private JEditorPane linkTargetInformationPane;

	private List<IReferenceTargetElement> targetNodesList;

	private DefaultListModel linkTargetsListModel;

	private JList linkTargetsList;

	private String editedResourceId;

	private IReposiotryResource currentNode;

	private IDocumentURI documentUri;

	/**
	 * Create the panel.
	 */
	public SearchCrossReferncePanel(final LookupDialog parentDialog,
			AuthorAccess authorAccess, InsertReferenceElement element) {

		super(authorAccess, "rsuite:xmlDoc");
		
		customInitilize(element);

		JPanel searchPanel = new JPanel();

		JPanel resultPanel = new JPanel();

		JPanel buttonPanel = new JPanel();

		JPanel targetPanel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(this);		
		
		configureMainGroupLayout(searchPanel, resultPanel, targetPanel, buttonPanel, groupLayout);

		setUpButtonPanel(parentDialog, authorAccess, element, buttonPanel);

		setUpResultPanel(resultPanel);

		setUpSearchPanel(searchPanel, groupLayout, "XML Content:");
		
		setUpTargetPanel(targetPanel);		

	}


	private void setUpButtonPanel(final LookupDialog parentDialog,
			AuthorAccess authorAccess, InsertReferenceElement element,
			JPanel buttonPanel) {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new CloseDialogAction(parentDialog));

		String buttonName = "Insert Cross Reference";
		if (element.isConfRef()){
			buttonName = "   Insert Conref   ";
		}
		
		JButton btnInsertCross = new JButton(buttonName);
		btnInsertCross.addActionListener(new ValidateAndAssignReferenceElementAction(authorAccess, parentDialog, element, this));
		

		JButton btnShowLocalTargets = new JButton("Show local targets");

		btnShowLocalTargets.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadDataToPanel(new EditedRepositoryResource(editedResourceId));

			}
		});

		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(gl_buttonPanel.createParallelGroup(
				Alignment.TRAILING).addGroup(
				gl_buttonPanel
						.createSequentialGroup()
						.addGap(83)
						.addComponent(btnShowLocalTargets)
						.addPreferredGap(ComponentPlacement.RELATED, 36,
								Short.MAX_VALUE)
						.addComponent(btnInsertCross)
						.addGap(35)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE,
								100, GroupLayout.PREFERRED_SIZE).addGap(23)));
		gl_buttonPanel.setVerticalGroup(gl_buttonPanel.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						gl_buttonPanel
								.createSequentialGroup()
								.addGroup(
										gl_buttonPanel
												.createParallelGroup(
														Alignment.BASELINE)
												.addComponent(btnCancel)
												.addComponent(
														btnShowLocalTargets)
												.addComponent(btnInsertCross))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		buttonPanel.setLayout(gl_buttonPanel);
	}

	private void setUpResultPanel(JPanel resultPanel) {
		JLabel lblNewLabel = new JLabel("Result Filter:");

		filterText = new JTextField();
		filterText.setColumns(15);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_resultPanel = new GroupLayout(resultPanel);
		gl_resultPanel
				.setHorizontalGroup(gl_resultPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_resultPanel
										.createSequentialGroup()
										.addGroup(
												gl_resultPanel
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_resultPanel
																		.createSequentialGroup()
																		.addComponent(
																				lblNewLabel)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				filterText,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																scrollPane,
																GroupLayout.DEFAULT_SIZE,
																531,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_resultPanel
				.setVerticalGroup(gl_resultPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_resultPanel
										.createSequentialGroup()
										.addComponent(scrollPane,
												GroupLayout.PREFERRED_SIZE,
												111, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addGroup(
												gl_resultPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblNewLabel)
														.addComponent(
																filterText,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(43, Short.MAX_VALUE)));


		selectionHandler = new CrossReferenceTableSelectionHandler(this);
		SearchResultPanelConfigurator configurator = new SearchResultPanelConfigurator(filterText);
		configurator.configureResultPanel(selectionHandler);
		table = configurator.getResultTable();
		myTableModel = configurator.getResultTableModel();
		
		scrollPane.setViewportView(table);
		resultPanel.setLayout(gl_resultPanel);
	}

	
	private void configureMainGroupLayout(JPanel searchPanel, JPanel resultPanel,
			JPanel targetPanel, JPanel buttonPanel, GroupLayout groupLayout) {
		
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				searchPanel,
																				GroupLayout.PREFERRED_SIZE,
																				187,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								targetPanel,
																								GroupLayout.DEFAULT_SIZE,
																								644,
																								Short.MAX_VALUE)
																						.addComponent(
																								resultPanel,
																								GroupLayout.DEFAULT_SIZE,
																								644,
																								Short.MAX_VALUE)))
														.addComponent(
																buttonPanel,
																Alignment.TRAILING,
																GroupLayout.PREFERRED_SIZE,
																640,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(26)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				resultPanel,
																				GroupLayout.PREFERRED_SIZE,
																				173,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				targetPanel,
																				GroupLayout.DEFAULT_SIZE,
																				254,
																				Short.MAX_VALUE))
														.addComponent(
																searchPanel,
																GroupLayout.DEFAULT_SIZE,
																335,
																Short.MAX_VALUE))
										.addPreferredGap(
												ComponentPlacement.RELATED, 12,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(buttonPanel,
												GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addGap(49)));
	}
	
	private void setUpTargetPanel(JPanel targetPanel) {
		JLabel lblTargetType = new JLabel("Target type:");

		dropDownList = new JComboBox();
		dropDownList.setPreferredSize(new Dimension(32, 30));
		dropDownList.setMinimumSize(new Dimension(32, 30));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		GroupLayout gl_targetPanel = new GroupLayout(targetPanel);
		gl_targetPanel
				.setHorizontalGroup(gl_targetPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_targetPanel
										.createSequentialGroup()
										.addGroup(
												gl_targetPanel
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_targetPanel
																		.createSequentialGroup()
																		.addComponent(
																				lblTargetType)
																		.addPreferredGap(
																				ComponentPlacement.UNRELATED)
																		.addComponent(
																				dropDownList,
																				GroupLayout.PREFERRED_SIZE,
																				446,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																splitPane,
																GroupLayout.DEFAULT_SIZE,
																632,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_targetPanel.setVerticalGroup(gl_targetPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_targetPanel
						.createSequentialGroup()
						.addGap(2)
						.addGroup(
								gl_targetPanel
										.createParallelGroup(
												Alignment.TRAILING, false)
										.addComponent(dropDownList, 0, 25,
												Short.MAX_VALUE)
										.addComponent(lblTargetType,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 221,
								Short.MAX_VALUE).addContainerGap()));

		linkTargetInformationPane = new JEditorPane();
		linkTargetInformationPane.setEditable(false);
		splitPane.setRightComponent(linkTargetInformationPane);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setLeftComponent(scrollPane_1);

		scrollPane_1.setViewportView(linkTargetsList);
		targetPanel.setLayout(gl_targetPanel);
		
		dropDownList.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String selectedElement = (String) dropDownList
						.getSelectedItem();
				if (selectedElement != null) {
					loadList(selectedElement);
				}
				linkTargetInformationPane.setText("");
			}
		});
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



	public void loadDataToPanel(IReposiotryResource userNode) {
		try {

			resetPanel();
			targetNodesList = linkTargetLookup.getReferenceTargetElementList(
					authorAccess, userNode, element);
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
		linkTargetInformationPane.setText("");
	}
	
	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {
		
		boolean contextNode = StringUtils.equalsIgnoreCase(
				documentUri.getEditedDocumentId(), currentNode.getCMSid());

		return new SelectedNode(currentNode,
				(IReferenceTargetElement) linkTargetsList.getSelectedValue(),
				contextNode);
	}
	
	private void customInitilize(InsertReferenceElement element) {
		this.element = element;

		documentUri = documentComponent.getDocumentUri();
		editedResourceId = documentUri.getEditedDocumentId();

		linkTargetLookup = documentComponent.getLookupRegister()
				.getIReferenceTargetLookup();

	
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
				}

			}
		});
	}


}
