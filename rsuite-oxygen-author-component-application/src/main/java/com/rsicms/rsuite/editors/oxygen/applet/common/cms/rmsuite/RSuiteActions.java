package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.CountingOutputStream;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveProgressListener;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
//TODO check inf necessary
@Deprecated
public class RSuiteActions implements ICmsActions {

	@Override
	public void saveDocument(IDocumentURI documentUri, SaveProgressListener progressListener, byte[] bytes)
			throws OxygenIntegrationException {

		try {
			URL url = new URL(documentUri.getSaveUri());

			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			httpCon.setRequestProperty("Content-Type",
					"text/xml; charset=UTF-8");

			CountingOutputStream cos = new CountingOutputStream(
					httpCon.getOutputStream(), progressListener);
			cos.write(bytes);
			cos.flush();
			cos.close();

			int responseCode = httpCon.getResponseCode();

			if (!(responseCode >= 200 && responseCode < 300)) {
				Scanner s = new Scanner(httpCon.getErrorStream());
				s.useDelimiter("\\Z");
				String response = s.next();

				throw new OxygenIntegrationException(
						"Unable to save the document. Received an error from the server. Response code: "
								+ responseCode + " " + response);
			}

		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		}

	}

	@Override
	public void checkInDocument(IDocumentURI documentUri, String versionType, String versionNote)
			throws OxygenIntegrationException {
		versionType = versionType.toUpperCase();

		try {

			versionNote = URLEncoder.encode(versionNote, "utf-8");

			String urlParameters = "&versionType=" + versionType
					+ "&versionNote=" + versionNote;

			String documentPath =documentUri.getDocumentURI();
			String checkinUri = documentPath.replace("/v1/", "/v2/");
			checkinUri = checkinUri.replace("/content/", "/content/checkout/id/");
			
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

				throw new OxygenIntegrationException(
						"Unable to save the document. Received an error from the server. Response code: "
								+ responseCode + " " + response);

			}

		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		}
	}

	@Override
	public String loadDocument(IDocumentCustomization customization, final IDocumentURI documentUri)
			throws OxygenIntegrationException {
		
		//TODO remove after switching to v2
		//IDocumentURI loadDocumentURI
		
		final ICmsURI cmsUri = documentUri.getCMSUri();

		IDocumentURI loadDocumentURI = new IDocumentURI() {
			
			@Override
			public String getSaveUri() {
				return null;
			}
			
			@Override
			public String getEditedDocumentId() {
				return null;
			}
			
			@Override
			public String getDocumentURI() {
				return cmsUri.getHostURI() + "/rsuite/rest/v2/content/binary?" + cmsUri.getSessionKeyParam() + "&id=" + documentUri.getEditedDocumentId() + "&includeSchema=true";
			}
			
			@Override
			public ICmsURI getCMSUri() {
				return null;
			}
		};
		
		
		return OxygenIOUtils.loadDocumentFromCMS(customization, loadDocumentURI);
	}

	@Override
	public boolean logInToCms(String text, String string) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSessionValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OxygenOpenDocumentParmaters getOpenDocumentParmaters(
			String documentId) throws OxygenIntegrationException {
		// TODO Auto-generated method stub
		return null;
	}

}
