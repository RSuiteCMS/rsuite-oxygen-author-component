package com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.ILookupHandler;
import com.rsicms.rsuite.editors.oxygen.integration.utils.StaxUtils;

/**
 * Web service to find an image by name and return the binary object
 * <p>
 * This web service is used by the various content previews to display the
 * images
 * </p>
 */
public abstract class ALookupWebService implements RemoteApiHandler {

	private static ILookupHandler lookupHandler;

	private static Log log = LogFactory.getLog(ALookupWebService.class);

	/**
	 * Old misspelled initialization method for older versions of RSuite.
	 */
	public void initalize(RemoteApiDefinition def) {
		initialize(def);
	}

	/**
	 * Initialize handler.
	 */
	public void initialize(RemoteApiDefinition def) {
	}

	/**
	 * Process request.
	 * 
	 * @param context
	 *            Execution context.
	 * @param args
	 *            Service parameters provided by client.
	 */
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			Writer writer = new StringWriter();
			XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(writer);
			xmlWriter.writeStartDocument();

			processRequest(context, args, xmlWriter);

			xmlWriter.writeEndElement();
			xmlWriter.writeEndDocument();

			XmlRemoteApiResult xmlRemote = new XmlRemoteApiResult(
					writer.toString());
			xmlRemote.setContentType("text/xml");

			return xmlRemote;
		} catch (Throwable e) {
			log.error(e.getLocalizedMessage(), e);
			return new MessageDialogResult(MessageType.ERROR,
					"Web service Error", e.getLocalizedMessage() + ".");
		}
	}

	abstract protected void processRequest(RemoteApiExecutionContext context,
			CallArgumentList args, XMLStreamWriter xmlWriter)
			throws RSuiteException, XMLStreamException;

	public static void registerLookupHandler(ILookupHandler instance) {
		lookupHandler = instance;
	}

	public static ILookupHandler getLookupHandler() {
		return lookupHandler;
	}
}
