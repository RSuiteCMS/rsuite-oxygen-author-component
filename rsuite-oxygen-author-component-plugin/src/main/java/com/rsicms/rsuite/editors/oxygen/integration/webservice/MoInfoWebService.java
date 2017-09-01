package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.SchemaInfo;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.utils.WebServiceUtils;

/**
 * Returns basic information about MO required to open O2
 * 
 */
public class MoInfoWebService extends DefaultRemoteApiHandler implements
		OxygenConstants {

	public static Log log = LogFactory.getLog(MoInfoWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session session = context.getSession();
		User user = session.getUser();
		String rsuiteId = args.getFirstValue("rsuiteId");
		
		String alias = args.getFirstValue("alias");

		ManagedObjectService moService = context.getManagedObjectService();
		ManagedObject mo = null; 
				
		if (rsuiteId != null){
			mo = moService.getManagedObject(user, rsuiteId);
		}else if (alias != null){
			mo = moService.getObjectByAlias(user, alias);
			rsuiteId = mo.getId();
		}
				

		if (mo == null) {
			throw new RSuiteException("Unable to find mo with id" + rsuiteId);
		}

		String serverUrl = WebServiceUtils.getHostFromWsArguments(args);

		String documentUri = serverUrl + RSUITE_REST_CONTENT + rsuiteId
				+ "?skey=" + session.getKey();

		XmlRemoteApiResult result = new XmlRemoteApiResult(writeToXML(mo, documentUri)); 
		result.setContentType("text/xml");
		
		return result;

	}

	private String writeToXML(ManagedObject mo, String documentUri) throws RSuiteException {

		 String xmlStr = null;
		try {
			SchemaInfo schemaInfo = mo.getSchemaInfo();
			String externalSchemaId = schemaInfo.getExternalId();

			StringWriter writerStr = new StringWriter();
			XMLOutputFactory xof = XMLOutputFactory.newInstance();
		
			XMLStreamWriter xmlsw = xof.createXMLStreamWriter(writerStr);
		
			xmlsw.writeStartDocument("UTF-8", "1.0");
			xmlsw.writeStartElement("response");
			
			xmlsw.writeAttribute("documentUri", documentUri);
			xmlsw.writeAttribute("schemaId", schemaInfo.getSchemaId());
			xmlsw.writeAttribute("schemaPublicId", schemaInfo.getPublicId());
			xmlsw.writeAttribute("schemaSystemId", schemaInfo.getSystemLocation());
			xmlsw.writeAttribute("externalSchemaId", externalSchemaId);
			xmlsw.writeAttribute("title", mo.getDisplayName());
			
			xmlsw.writeEndElement();
            // end XML document
            xmlsw.writeEndDocument();
            xmlsw.flush();
            xmlsw.close();
            
            xmlStr = writerStr.getBuffer().toString();
            writerStr.close();

		} catch (FactoryConfigurationError e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to create xml response", e);
		} catch (XMLStreamException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to create xml response", e);
		} catch (IOException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to create xml response", e);
		}
		return xmlStr;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
