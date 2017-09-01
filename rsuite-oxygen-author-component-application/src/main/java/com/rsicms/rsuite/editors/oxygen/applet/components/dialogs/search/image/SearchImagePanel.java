package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.image;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.commons.lang.StringUtils;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.ASearchPanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.SearchResultPanelConfigurator;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTable;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.CloseDialogAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ValidateAndAssignReferenceElementAction;

public class SearchImagePanel extends ASearchPanel implements ISelectableComponent {

	/** UID */
	private static final long serialVersionUID = -1764224054792750944L;

	private JTextField filterText;

	private SearchResultTable table;

	private JLabel picLabel;	

	/**
	 * Create the panel.
	 */
	public SearchImagePanel(final LookupDialog parentDialog,
			AuthorAccess authorAccess, InsertReferenceElement element) {

		super(authorAccess, "rsuite:docMedia");

		JPanel panel = new JPanel();

		JPanel resultPanel = new JPanel();

		JPanel buttonPanel = new JPanel();

		GroupLayout groupLayout = new GroupLayout(this);
		
		configureMainGroupLayout(panel, resultPanel, buttonPanel, groupLayout);


		setUpButtonPanel(parentDialog, authorAccess, element, buttonPanel);

		setUpResultPanel(resultPanel);

		setUpSearchPanel(panel, groupLayout, "Files and media:");

	}

	private void setUpButtonPanel(final LookupDialog parentDialog,
			AuthorAccess authorAccess, InsertReferenceElement element,
			JPanel buttonPanel) {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new CloseDialogAction(parentDialog));

		JButton btnInsertCross = new JButton("Insert Image");

		
		btnInsertCross.addActionListener(new ValidateAndAssignReferenceElementAction(authorAccess, parentDialog, element, this));
		

		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addContainerGap(286, Short.MAX_VALUE)
					.addComponent(btnInsertCross)
					.addGap(35)
					.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addGap(23))
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnInsertCross))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
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
																656,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_resultPanel
				.setVerticalGroup(gl_resultPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								Alignment.TRAILING,
								gl_resultPanel
										.createSequentialGroup()
										.addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 206,
												Short.MAX_VALUE)
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
										.addContainerGap()));

		selectionHandler = new ImageTableSelectionHandler(getDocumentUri(), picLabel);
		SearchResultPanelConfigurator configurator = new SearchResultPanelConfigurator(filterText);
		configurator.configureResultPanel(selectionHandler);
		table = configurator.getResultTable();
		myTableModel = configurator.getResultTableModel();
		
		scrollPane.setViewportView(table);
		resultPanel.setLayout(gl_resultPanel);
	}



	private void configureMainGroupLayout(JPanel searchPanel, JPanel panel_2,
			JPanel panel_4, GroupLayout groupLayout) {
		JLabel label = new JLabel("");
		JLabel lblNewLabel_1 = new JLabel("Image preview");
		lblNewLabel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		lblNewLabel_1.setMaximumSize(new Dimension(250, 250));
		lblNewLabel_1.setPreferredSize(new Dimension(250, 250));
		lblNewLabel_1.setSize(new Dimension(250, 250));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		picLabel =lblNewLabel_1;
		
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(222)
									.addComponent(label))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
									.addGap(204))))
						.addComponent(panel_4, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 640, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(searchPanel, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
							.addGap(42))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
								.addComponent(label))))
					.addGap(36)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(30))
		);
	}

	

	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {
		ResultItem currentNode = selectionHandler.getSelectedItem();
		boolean contextNode = StringUtils.equalsIgnoreCase(
				getDocumentUri().getEditedDocumentId(), currentNode.getCMSid());

		IReferenceTargetElement targetElement = null;

		
		return new SelectedNode(currentNode, targetElement, contextNode);

	}
}
