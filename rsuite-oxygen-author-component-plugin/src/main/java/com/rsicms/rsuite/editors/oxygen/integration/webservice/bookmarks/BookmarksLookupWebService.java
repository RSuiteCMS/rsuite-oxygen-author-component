package com.rsicms.rsuite.editors.oxygen.integration.webservice.bookmarks;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.domain.bookmarks.BookmarkManager;
import com.rsicms.rsuite.editors.oxygen.integration.utils.MoLookupSerialiazer;
import com.rsicms.rsuite.editors.oxygen.integration.utils.MoUtils;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup.ALookupWebService;

public class BookmarksLookupWebService  extends ALookupWebService {

	@Override
	protected void processRequest(RemoteApiExecutionContext context,
			CallArgumentList args, XMLStreamWriter xmlWriter)
			throws RSuiteException, XMLStreamException {
		
		
		String id = args.getFirstValue("id");

		xmlWriter.writeStartElement("tree");
		xmlWriter.writeAttribute("id", id);

		User user = context.getSession().getUser();

		List<ManagedObject> childs;
		
		if ("0".equals(id)) {
			BookmarkManager bookmarkManager = new BookmarkManager(context);
			childs = bookmarkManager.getBookmarksForUser(user);
		}else{
			childs = getChildsMo(context, id, user);
		}

		

		MoLookupSerialiazer moSerializer = new MoLookupSerialiazer(
				getLookupHandler());

		for (ManagedObject child : childs) {
			moSerializer.writeItemElement(context, xmlWriter, child);
		}
		
	}


	private List<ManagedObject> getChildsMo(RemoteApiExecutionContext context,
			String id, User user) throws RSuiteException {
		ManagedObject mo = MoUtils.getRealMo(context, id);

		List<ManagedObject> childs = MoUtils.getManageObjectChildren(context, user,
				mo);
		return childs;
	}



	

}
