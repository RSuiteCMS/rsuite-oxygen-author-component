package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.ecss.extensions.api.component.ditamap.DITAMapTreeComponentProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;

public class OxygenIOUtils {

	private static Logger logger = Logger.getLogger(OxygenIOUtils.class);

	/**
	 * Loads document from CMS and run custom handler
	 * 
	 * @param documentURI
	 *            document URI
	 * @return String reader with fixed change tracking PI
	 * @throws Exception
	 */
	public static String loadDocumentFromCMS(IDocumentCustomization customization, IDocumentURI documentURI) throws OxygenIntegrationException {

		String sr = null;
		try {

			String documentUri = documentURI.getDocumentURI();
			InputStream is = loadContentFromURL(documentUri);

			IDocumentHandler ioHandler = customization.getDocumentHandler();
			
			sr = IOUtils.toString(is, "UTF-8");
			
			if (ioHandler != null){
				sr =  ioHandler.modifyDocumentBeforeLoad(sr);
			}

			is.close();
		} catch (IOException e) {
			throw new OxygenIntegrationException("Unable to load and transform document", e);
		}

		return sr;
	}

	public static InputStream loadContentFromURL(String address)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(address);			
		HttpURLConnection httpCon = (HttpURLConnection) url
				.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");
		httpCon.setRequestProperty("Content-Type",
				"text/xml; charset=UTF-8");
		httpCon.setUseCaches(false);
		
		InputStream is = httpCon.getInputStream();
		if (httpCon.getResponseCode() != 200){
			throw new IOException(" Failed request " + httpCon.getResponseMessage());
		}
		return is;
	}

	public static String getSerializedDocument(EditorComponentProvider editorComponentProvider, IDocumentCustomization customization){
		IDocumentHandler ioHandler = customization.getDocumentHandler();
		
		Reader reader = editorComponentProvider.getWSEditorAccess().createContentReader();
		
		String sr = null;
		try {
			
			sr = readerToString(reader);
			
			if (ioHandler != null){
				return ioHandler.modifyDocumentBeforeSave(sr);
			}

		} catch (IOException e) {
			OxygenUtils.handleException(logger, e);
		}
		
		return sr;
	}
	
	public static String getSerializedDocument(DITAMapTreeComponentProvider ditaMapComponentProvider, IDocumentCustomization customization){
		IDocumentHandler ioHandler = customization.getDocumentHandler();
		
		Reader reader = ditaMapComponentProvider.getWSEditorAccess().createContentReader();
		String sr = null;
		
		try {
			sr = readerToString(reader);

			if (ioHandler != null){
				return ioHandler.modifyDocumentBeforeSave(sr);
			}
		} catch (IOException e) {
			OxygenUtils.handleException(logger, e);
		}
		
		return sr;
	}
	
	public static String readerToString(Reader in) throws IOException {
		StringBuilder out = new StringBuilder();
		char[] b = new char[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static String isToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	
}
