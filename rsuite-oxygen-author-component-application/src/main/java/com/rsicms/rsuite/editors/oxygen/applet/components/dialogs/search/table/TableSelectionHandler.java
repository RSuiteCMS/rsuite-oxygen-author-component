package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.EmptyResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;

public class TableSelectionHandler implements ListSelectionListener{

	private SearchResultTable table;
	
	private SearchResultTableModel myTableModel;
	
	private ResultItem selectedItem;
	
	
	public void initialize(SearchResultTable table,
			SearchResultTableModel myTableModel) {
		this.table = table;
		this.myTableModel = myTableModel;
	}

	public void valueChanged(ListSelectionEvent event) {

		if (!event.getValueIsAdjusting()) {
			int viewRow = table.getSelectedRow();
			if (viewRow >= 0) {
				int modelRow = table
						.convertRowIndexToModel(viewRow);
				selectedItem = myTableModel
						.getResultItem(modelRow);
				
				customSelectAction(selectedItem);

			}
		}
	}
	
	protected void customSelectAction( ResultItem selectedItem){
		
	}

	public ResultItem getSelectedItem() {
		
		if (selectedItem instanceof EmptyResultItem){
			return null;
		}
		return selectedItem;
	}
	
	
	
}
