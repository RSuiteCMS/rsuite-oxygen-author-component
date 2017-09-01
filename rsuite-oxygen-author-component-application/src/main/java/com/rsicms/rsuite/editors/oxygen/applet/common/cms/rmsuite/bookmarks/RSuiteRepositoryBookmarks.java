package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.bookmarks;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.RsuiteBookmarksOxygenLookUp;

public class RSuiteRepositoryBookmarks implements IRepositoryBookmarks {

	private ICmsURI cmsURI;

	public RSuiteRepositoryBookmarks(ICmsURI cmsURI) {
		super();
		this.cmsURI = cmsURI;
	}

	@Override
	public IBookmarkManager getBookmarkManager() {
		return new RSuiteBookmarkManager(cmsURI);
	}

	@Override
	public ITreeOxygenLookUp getBookmarkTreeLookup() {
		return 	new RsuiteBookmarksOxygenLookUp(
				cmsURI, "0");
	}



}
