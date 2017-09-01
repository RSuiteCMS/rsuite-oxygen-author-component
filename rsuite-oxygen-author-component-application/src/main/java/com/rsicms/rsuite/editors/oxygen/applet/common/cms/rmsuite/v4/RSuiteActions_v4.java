package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.v4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IModifiableCmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.http.HttpConnector;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.http.HttpResponse;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.http.MultipartRequest;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveProgressListener;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class RSuiteActions_v4 implements ICmsActions {

	private IModifiableCmsURI cmsURI;
	
	private static XMLInputFactory xmlInputFactory = XMLInputFactory
			.newFactory();

	private HttpConnector httpConnector;

	public RSuiteActions_v4(IModifiableCmsURI cmsURI) {
		this.cmsURI = cmsURI;
		httpConnector = new HttpConnector();
	}
	

	public RSuiteActions_v4(IModifiableCmsURI cmsURI, HttpConnector httpConnector) {
		this.cmsURI = cmsURI;
		this.httpConnector = httpConnector;
	}



	@Override
	public void saveDocument(IDocumentURI documentUri,
			SaveProgressListener progressListener, byte[] bytes)
			throws OxygenIntegrationException {

		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);){
			String moId = documentUri.getEditedDocumentId();
			
			MultipartRequest request = new MultipartRequest(documentUri.getSaveUri(), "utf-8");			
			request.addFormField("moId", moId);
			
			String fileName = moId + ".xml";			
			request.addFilePart(fileName, fileName, inputStream);
			String responseText = request.completeRequest();
			parseResponseSaveResponse(responseText);

		} catch (Exception e) {
			throw new OxygenIntegrationException("Unable to save document: "
					+ e.getMessage(), e);
		}

	}

	private void parseResponseSaveResponse(String response) throws Exception {

		XMLEventReader reader = null;

		try {
			reader = xmlInputFactory.createXMLEventReader(new StringReader(
					response));

			String rootElement = null;
			String currentElement = null;

			String message = "";

			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();

				if (event.isStartElement()) {
					StartElement element = event.asStartElement();
					currentElement = element.getName().getLocalPart();

					if (rootElement == null
							&& !"error".equalsIgnoreCase(currentElement)) {
						return;
					}

					if (rootElement == null) {
						rootElement = currentElement;
					}
				}

				if (event.isCharacters()
						&& "message".equalsIgnoreCase(currentElement)) {
					message += event.asCharacters().getData();
				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					String endElementName = endElement.getName().getLocalPart();
					if ("message".equalsIgnoreCase(endElementName)) {
						break;
					}
				}
			}

			throw new Exception(message);

		} catch (XMLStreamException e) {
			throw new Exception("Unable to parse message", e);
		} finally {
			closeQuietly(reader);
		}
	}


	private void closeQuietly(XMLEventReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (XMLStreamException e) {
				// Intentionally empty
			}
		}
	}

	@Override
	public void checkInDocument(IDocumentURI documentUri, String versionType,
			String versionNote) throws OxygenIntegrationException {
		versionType = versionType.toUpperCase();

		try {

			versionNote = URLEncoder.encode(versionNote, "utf-8");

			String urlParameters = "&versionType=" + versionType
					+ "&versionNote=" + versionNote;

			String documentPath = documentUri.getDocumentURI();
			String checkinUri = documentPath.replace("/content/binary/",
					"/content/checkout/");

			URL url = new URL(checkinUri);

			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");

			OutputStreamWriter out = new OutputStreamWriter(
					httpCon.getOutputStream());
			out.write(urlParameters);
			out.close();

			int responseCode = httpCon.getResponseCode();

			if (!(responseCode >= 200 && responseCode < 300)) {
				Scanner s = new Scanner(httpCon.getErrorStream());
				s.useDelimiter("\\Z");
				String response = s.next();
				s.close();

				throw new OxygenIntegrationException(
						"Unable to save the document. Received an error from the server. Response code: "
								+ responseCode + " " + response);

			}

		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		}
	}

	@Override
	public String loadDocument(IDocumentCustomization customization,
			final IDocumentURI documentUri) throws OxygenIntegrationException {
		return OxygenIOUtils.loadDocumentFromCMS(customization, documentUri);
	}

	@Override
	public boolean logInToCms(String userName, String password) throws OxygenIntegrationException {

		try {
			
			Map<String, String> formData = new HashMap<String, String>();
			formData.put("user", userName);
			formData.put("pass", password);

			HttpResponse response = httpConnector.sendPostRequest(cmsURI.getHostURI()
					+ "/rsuite/rest/v2/user/session", formData);
			
			String sessionKey = RSuiteHttpResponseParser.parseResponseSaveResponse(response.getResponseText(), "key");
			
			cmsURI.updateSessionKey(sessionKey);

		} catch (Exception e) {
			
			if (e.getMessage() != null && e.getMessage().contains("Unknown user ID/password combination")){
				return false;
			}
			
			throw new OxygenIntegrationException("Unable to log in to CMS: "
					+ e.getMessage(), e);
		}

		return true;
	}


	@Override
	public boolean isSessionValid() throws OxygenIntegrationException {
		HttpResponse response = httpConnector.sendGetRequest(cmsURI.getHostURI() + "/rsuite/rest/v1/api/rsuite.oxygen.check.session");
		
		if (response.getCode() >= 200 && response.getCode() < 300){
			return true;
		}
		
		return false;
	}
	
	@Override
	public OxygenOpenDocumentParmaters getOpenDocumentParmaters(String documentId) throws OxygenIntegrationException{		
		try{
			String requestPath = cmsURI.getHostURI() + "/rsuite/rest/v1/api/rsuite.oxygen.open.document.parameters" + "?rsuiteId=" + documentId + "&" + cmsURI.getSessionKeyParam();
			System.out.println(requestPath);
			HttpResponse httpResponse = httpConnector.sendGetRequest(requestPath);
			OxygenOpenDocumentParmaters openDocumentParameter = RSuiteHttpResponseParser.parseOpenDocumentResponse(httpResponse.getResponseText());		
			return openDocumentParameter;
		}catch (Exception e){
			throw new OxygenIntegrationException("Unable to obtain open document parameters: "
					+ e.getMessage(), e);
		}
		
	}
}
