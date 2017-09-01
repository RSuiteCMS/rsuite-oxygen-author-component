package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import javax.swing.JPanel;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTableModel;

public interface ISearchMainPanel {

	JPanel getSearchPanel();

	SearchResultTableModel getResultTableModel();

	String getSearchType();
	
	IDocumentURI getDocumentUri();

}
