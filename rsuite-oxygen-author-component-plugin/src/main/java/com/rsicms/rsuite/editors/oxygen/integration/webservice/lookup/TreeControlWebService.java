package com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.utils.MoLookupSerialiazer;
import com.rsicms.rsuite.editors.oxygen.integration.utils.MoUtils;

/**
 * Web service to find an image by name and return the binary object
 * <p>
 * This web service is used by the various content previews to display the
 * images
 * </p>
 */
public class TreeControlWebService extends ALookupWebService {

	@Override
	protected void processRequest(RemoteApiExecutionContext context,
			CallArgumentList args, XMLStreamWriter xmlWriter)
			throws RSuiteException, XMLStreamException {

		String id = args.getFirstValue("id");

		xmlWriter.writeStartElement("tree");
		xmlWriter.writeAttribute("id", id);

		User user = context.getSession().getUser();

		if ("0".equals(id)) {
			id = context.getContentAssemblyService().getRootFolder(user)
					.getId();
		}

		ManagedObject mo = MoUtils.getRealMo(context, id);

		List<ManagedObject> childs = MoUtils.getManageObjectChildren(context, user,
				mo);

		MoLookupSerialiazer moSerializer = new MoLookupSerialiazer(
				getLookupHandler());

		for (ManagedObject child : childs) {
			moSerializer.writeItemElement(context, xmlWriter, child);
		}
	}

}
