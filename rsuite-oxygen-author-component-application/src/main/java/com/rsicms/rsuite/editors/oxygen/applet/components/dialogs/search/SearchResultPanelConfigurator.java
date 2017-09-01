package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTable;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTableModel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.TableSelectionHandler;

public class SearchResultPanelConfigurator {

	private SearchResultTableModel resultTableModel;

	private SearchResultTable resultTable;

	private TableRowSorter<SearchResultTableModel> sorter;
	
	private JTextField filterText;
	
	
	public SearchResultPanelConfigurator(
			JTextField filterText) {
		this.resultTableModel =  new SearchResultTableModel();
		this.filterText = filterText;
	}

	public void configureResultPanel(TableSelectionHandler selectionHandler){
		configureTable(selectionHandler);
		configureFilterText();		
	}

	public void configureTable(TableSelectionHandler selectionHandler) {

		sorter = new TableRowSorter<SearchResultTableModel>(resultTableModel);
		resultTable = new SearchResultTable(resultTableModel);
		resultTable.setRowSorter(sorter);

		resultTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		resultTable.getColumnModel().getColumn(1).setMaxWidth(100);

		resultTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		resultTable.getColumnModel().getColumn(2).setMaxWidth(150);

		resultTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		resultTable.getColumnModel().getColumn(3).setMaxWidth(150);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.CENTER);
		resultTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		if (selectionHandler == null) {
			selectionHandler = new TableSelectionHandler();
		}
		
		selectionHandler.initialize(resultTable, resultTableModel);

		resultTable.getSelectionModel().addListSelectionListener(selectionHandler);
		
	}


	private void configureFilterText() {
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}

			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}

			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
	}

	
	/**
	 * Update the row filter regular expression from the expression in the text
	 * box.
	 */
	private void newFilter() {
		RowFilter<SearchResultTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter(filterText.getText(), 0);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}

	public SearchResultTableModel getResultTableModel() {
		return resultTableModel;
	}

	public SearchResultTable getResultTable() {
		return resultTable;
	}
	
	
}
