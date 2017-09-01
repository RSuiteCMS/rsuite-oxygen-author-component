package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTableModel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.TableSelectionHandler;

public class ASearchPanel extends JPanel implements ISearchMainPanel{

	/** UID */
	private static final long serialVersionUID = 1408952258517964185L;

	protected TableSelectionHandler selectionHandler;
	
	protected OxygenDocument documentComponent;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private IDocumentURI documentUri;
	
	protected SearchResultTableModel myTableModel;

	protected JPanel searchPanel;
	
	public static boolean desing = true;
	
	private String searchType;
	
	protected AuthorAccess authorAccess;
	
	protected JTextField searchField;
	
	public ASearchPanel(AuthorAccess authorAccess, String searchType) {
		if (!desing){
			initialize(authorAccess);
		}
		this.searchType = searchType;
		this.authorAccess = authorAccess;
	}
	
	private void initialize(AuthorAccess authorAccess){
		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);

		documentComponent = context.getDocumentComponent();
		documentUri = documentComponent.getDocumentUri();
	}
	
	
	@Override
	public JPanel getSearchPanel() {
		return searchPanel;
	}


	@Override
	public SearchResultTableModel getResultTableModel() {
		return myTableModel;
	}

	@Override
	public String getSearchType() {
		return searchType;
	}

	@Override
	public IDocumentURI getDocumentUri() {
		return documentUri;
	}

	
	protected void setUpSearchPanel(JPanel panel, GroupLayout groupLayout, String label) {
		JLabel labelFilterSearch = new JLabel("Filter Search:");
		labelFilterSearch.setFont(new Font("Dialog", Font.BOLD, 13));

		searchField = new JTextField();

		searchField.setColumns(12);

		JLabel lblXmlContent = new JLabel(label);


		

		
		final JPanel panel_1 = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel_1);
		
		
		
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		searchPanel = panel_1;

		JSeparator separator = new JSeparator();

		JProgressBar progressBar = new JProgressBar();
		
		JButton searchButton = createSearchButton();
		SearchPanelConfigurator searchConfigurator = new SearchPanelConfigurator(this, searchField, progressBar, searchButton);
		searchConfigurator.configureSearchPanel();
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(300,100));

        scrollPane.setMaximumSize(new Dimension(300,750));
        
        
        
        
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		lblXmlContent)
																.addPreferredGap(
																		ComponentPlacement.RELATED)
																.addComponent(
																		separator,
																		GroupLayout.PREFERRED_SIZE,
																		1,
																		GroupLayout.PREFERRED_SIZE))
												.addComponent(
														scrollPane,
														GroupLayout.PREFERRED_SIZE,
														167,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(
														searchField,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_panel.createSequentialGroup()
															.addComponent(labelFilterSearch)
															.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
															.addComponent(searchButton))		
												.addComponent(
														progressBar,
														GroupLayout.PREFERRED_SIZE,
														167,
														GroupLayout.PREFERRED_SIZE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE,
								20, GroupLayout.PREFERRED_SIZE)
						.addGap(2)
											.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelFilterSearch)
						.addComponent(searchButton))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(searchField, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblXmlContent)
										.addComponent(separator,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 143,
								Short.MAX_VALUE).addGap(15)));
		

		panel.setLayout(gl_panel);
		setLayout(groupLayout);
	}

	private JButton createSearchButton() {
		Icon icon = OxygenUtils.getIcon("show-in-results.png");
        
		JButton searchButton = new JButton();
		searchButton.setMaximumSize(new Dimension(34, 16));
		searchButton.setPreferredSize(new Dimension(34, 16));
		searchButton.setIcon(icon);
		
		
		searchButton.setSize(30, 16);
		searchButton.setBorderPainted( false );
		searchButton.setFocusPainted(false);
		searchButton.setMargin(new Insets(0, 0, 0, 0));
		searchButton.setContentAreaFilled(false);
		searchButton.setBorderPainted(false);

		searchButton.setToolTipText("Show results");
		return searchButton;
	}
}
