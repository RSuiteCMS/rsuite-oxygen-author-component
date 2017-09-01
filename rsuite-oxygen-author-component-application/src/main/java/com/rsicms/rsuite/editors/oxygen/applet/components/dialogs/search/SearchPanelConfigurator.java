package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ISearchFacetUtil;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.OxygenSearchFacetUtil;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacet;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacetUtil;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTableModel;

public class SearchPanelConfigurator {
	
	private JPanel panel_1;
	
	private JProgressBar progressBar;
	
	private JButton searchButton;
	
	private ISearchFacetUtil searchFacet;
	
	private SearchResultTableModel myTableModel;
	
	private JTextField searchField;
	
	private String searchType;
	
	private IDocumentURI documentUri;
	
	private Logger logger = Logger.getLogger(this.getClass());


	public SearchPanelConfigurator(ISearchMainPanel searchDialog, JTextField searchField, JProgressBar progressBar, JButton searchButton){

		this.panel_1 = searchDialog.getSearchPanel();
		this.myTableModel = searchDialog.getResultTableModel();
		this.searchField = searchField;
		this.searchType = searchDialog.getSearchType();
		this.documentUri = searchDialog.getDocumentUri();
		this.progressBar = progressBar;
		this.searchButton = searchButton;
		this.progressBar.setVisible(false);
		if ("rsuite:docMedia".equalsIgnoreCase(searchType)){
			searchFacet = new SearchFacetUtil(documentUri.getCMSUri());
		}else{
			searchFacet = new OxygenSearchFacetUtil(documentUri.getCMSUri());
		}
		
	}

	public void configureSearchPanel(){
		
		try {
			Map<String, String> docTypes = getSearchElements();
			int i = 0;
			for (Entry<String, String> entry : docTypes.entrySet()) {
				JCheckBox chinButton = new JCheckBox(entry.getValue());
				chinButton.setName(entry.getKey());

				if ("rsuite:docMedia".equalsIgnoreCase(searchType) && "image".equalsIgnoreCase(entry.getKey())){
					chinButton.setSelected(true);
				}
				
				GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
				gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 0);
				gbc_chckbxNewCheckBox.gridx = 0;
				gbc_chckbxNewCheckBox.gridy = i;
				panel_1.add(chinButton, gbc_chckbxNewCheckBox);
				i++;

			}
			
			
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

		SearchActionListener searchListener = new SearchActionListener();
		
		searchButton.addActionListener(searchListener);
		searchField.addActionListener(searchListener);
	}

	private Map<String, String> getSearchElements() throws Exception{
		
		if ("rsuite:docMedia".equalsIgnoreCase(searchType)){
			return searchFacet.getMediaTypes();
		}
		
		return searchFacet.getXmlDocTypes();
		
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	private class SearchActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			String searchText = searchField.getText();

			List<String> elements = new ArrayList<String>();

			for (Component component : panel_1.getComponents()) {
				if (component instanceof JCheckBox) {
					JCheckBox checkBox = (JCheckBox) component;
					if (checkBox.isSelected()) {
						elements.add(checkBox.getName());
					}
				}
			}

			if (!(StringUtils.isEmpty(searchText) && elements.size() == 0)) {
				progressBar.setIndeterminate(true);
				progressBar.setStringPainted(true);
				progressBar.setString("Searching...");
				progressBar.setVisible(true);

				SearchFacet search = new SearchFacet(searchText, elements, searchType);
				
				myTableModel.setResults(new ArrayList<ResultItem>());
				myTableModel.fireTableDataChanged();
				SearchWorker worker = new SearchWorker(progressBar,
						myTableModel, search, searchFacet);
				worker.execute();
			} 
		}
	}
}
