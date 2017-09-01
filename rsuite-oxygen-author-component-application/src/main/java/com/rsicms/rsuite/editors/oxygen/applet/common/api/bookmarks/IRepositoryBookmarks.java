package com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;

public interface IRepositoryBookmarks {

	IBookmarkManager getBookmarkManager();
	
	ITreeOxygenLookUp getBookmarkTreeLookup();
}
