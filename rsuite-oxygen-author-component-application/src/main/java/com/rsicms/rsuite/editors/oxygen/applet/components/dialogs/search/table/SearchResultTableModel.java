package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.EmptyResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;

public class SearchResultTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6196052450757030453L;

	private String[] columnNames = { "Name", "Kind", "Date Created",
			"Last Modified", };

	private List<ResultItem> results = new ArrayList<ResultItem>();

	public SearchResultTableModel() {
	}

	public void setResults(List<ResultItem> results) {
		this.results = results;
	}
	
	public void addResults(List<ResultItem> additionalResults) {
		this.results.addAll(additionalResults);
	}

	public ResultItem getResultItem(int rowIndex) {
		return results.get(rowIndex);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return results.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {

		if (results.size() == 0) {
			return "";
		}

		ResultItem item = results.get(row);
		String value = "";

		if (col == 0) {
			
			if (item instanceof EmptyResultItem){
				value = "";
			}else{
				value = item.getDisplayName() + " <" + item.getLocalName() + "> "
						+ "[" + item.getRsuiteId() + "]";	
			}
			
			value = value.replaceAll("\\s+", " ");
		}

		if (col == 1) {
			value = item.getKind();
		}

		if (col == 2) {
			value = item.getFormattedDateCreated();
		}

		if (col == 3) {
			value = item.getFormattedDateModified();
		}

		return value;
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

}
