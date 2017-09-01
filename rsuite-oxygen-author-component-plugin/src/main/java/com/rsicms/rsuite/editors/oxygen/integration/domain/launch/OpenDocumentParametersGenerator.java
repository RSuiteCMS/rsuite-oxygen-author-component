package com.rsicms.rsuite.editors.oxygen.integration.domain.launch;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants.RSUITE_REST_CONTENT;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.advisor.OxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.utils.OxygenLaunchHelper;
import com.rsicms.rsuite.editors.oxygen.integration.utils.StaxUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.WebServiceUtils;

public class OpenDocumentParametersGenerator {

	public static String getOpenDocumentParametersInXmlFormat(
			RemoteApiExecutionContext context, CallArgumentList args)
			throws RSuiteException {

		OxygenLaunchHelper helper = new OxygenLaunchHelper(context, args);
		MoToLaunch moToLaunch = helper.getMoToLaunch();

		String serverUrl = WebServiceUtils.getHostFromWsArguments(args);

		OxygenWebEditingContext webEditingContext = helper
				.getOxygenWebEditingContext();

		String documentUri = serverUrl + RSUITE_REST_CONTENT
				+ moToLaunch.getId() + "?skey="
				+ webEditingContext.getSession().getKey()
				+ "&includeSchema=true";

		return writeToXML(moToLaunch, documentUri);
	}

	private static String writeToXML(MoToLaunch moToLaunch, String documentUri)
			throws RSuiteException {
		String xmlStr = null;
		try {

			StringWriter writerStr = new StringWriter();
			XMLOutputFactory xof = XMLOutputFactory.newInstance();

			XMLStreamWriter xmlsw = xof.createXMLStreamWriter(writerStr);

			xmlsw.writeStartDocument("UTF-8", "1.0");
			xmlsw.writeStartElement("openDocument");

			StaxUtils.writeElementWithValue(xmlsw, "documentUri", documentUri);
			StaxUtils.writeElementWithValue(xmlsw, "title",
					moToLaunch.getTitle());
			StaxUtils.writeElementWithValue(xmlsw, "schemaSystemId",
					moToLaunch.getSchemaSystemId());
			StaxUtils.writeElementWithValue(xmlsw, "schemaPublicId",
					moToLaunch.getSchemaPublicId());
			StaxUtils.writeElementWithValue(xmlsw, "schemaId",
					moToLaunch.getSchemaId());
			StaxUtils.writeElementWithValue(xmlsw, "moReferenceId",
					moToLaunch.getMoReferenceId());
			
			StaxUtils.writeElementWithValue(xmlsw, "xpathStartLocation",
					moToLaunch.getLaunchXpath());

			xmlsw.writeEndElement();

			// end XML document
			xmlsw.writeEndDocument();
			xmlsw.flush();
			xmlsw.close();

			xmlStr = writerStr.getBuffer().toString();
			writerStr.close();

		} catch (FactoryConfigurationError e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to create xml response", e);
		} catch (XMLStreamException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to create xml response", e);
		} catch (IOException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to create xml response", e);
		}
		return xmlStr;
	}
}
