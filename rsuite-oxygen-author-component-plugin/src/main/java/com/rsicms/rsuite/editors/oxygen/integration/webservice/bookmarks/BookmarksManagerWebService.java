package com.rsicms.rsuite.editors.oxygen.integration.webservice.bookmarks;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.bookmarks.BookmarkManager;

public class BookmarksManagerWebService implements RemoteApiHandler {

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String moId = args.getFirstValue("moId");
		String operation = args.getFirstValue("operation");

		User user = context.getSession().getUser();
		BookmarkManager bookmarkManager = new BookmarkManager(context);

		if ("add".equals(operation)) {
			bookmarkManager.addBookmark(user, moId);
		} else if ("remove".equals(operation)) {
			bookmarkManager.removeBookmark(user, moId);
		} else {
			throw new RSuiteException("Unsupported operation " + operation);
		}

		return createResultObject();
	}

	private XmlRemoteApiResult createResultObject() {
		XmlRemoteApiResult xmlRemote = new XmlRemoteApiResult("<ok/>");
		xmlRemote.setContentType("text/xml");

		return xmlRemote;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
