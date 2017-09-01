package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.EmptyResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ISearchFacetUtil;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacet;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.SearchResultTableModel;

public class SearchWorker extends SwingWorker<Void, Void> {

	private JProgressBar progressBar;

	private SearchResultTableModel tableModel;

	private ISearchFacetUtil searchFacetUtil;

	private SearchFacet searchFacet;

	private Logger logger = Logger.getLogger(getClass());

	public SearchWorker(JProgressBar progressBar,
			SearchResultTableModel tableModel, SearchFacet searchFacet,
			ISearchFacetUtil searchFacetUtil) {
		super();
		this.progressBar = progressBar;
		this.tableModel = tableModel;
		this.searchFacetUtil = searchFacetUtil;
		this.searchFacet = searchFacet;
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {

			List<ResultItem> results = searchFacetUtil
					.performFacetSearch(searchFacet);
			
			if (results.size()== 0){
				results.add(EmptyResultItem.getInstance());
			}
			
			tableModel.setResults(results);
			tableModel.fireTableDataChanged();
			
			

		} catch (Throwable e) {
			OxygenUtils.handleExceptionUI("An error occured during the search", e);
		}

		return null;
	}

	@Override
	protected void done() {
		progressBar.setIndeterminate(false);
		progressBar.setString("");
		progressBar.setVisible(false);

	}
}
