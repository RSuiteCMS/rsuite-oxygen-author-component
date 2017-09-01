package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.crossRefernce;

import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.TableSelectionHandler;

public class CrossReferenceTableSelectionHandler extends TableSelectionHandler {

	private SearchCrossReferncePanel searchCrossPanel;

	
	
	public CrossReferenceTableSelectionHandler(SearchCrossReferncePanel searchCrossPanel) {
		super();
		this.searchCrossPanel = searchCrossPanel;
	}


	@Override
	protected void customSelectAction(ResultItem selectedItem) {
		 searchCrossPanel.loadDataToPanel(selectedItem);
	}
	
}
