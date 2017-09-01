package com.rsicms.rsuite.editors.oxygen.applet.common.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class HttpConnector {

	private static final int TIMEOUT_MILISECONDS = 100000;

	private static String charset = "UTF-8";

	public HttpResponse sendPutRequest(String requestUrl, Map<String, String> formData)
			throws IOException {
		return sendRequest(requestUrl, formData, "PUT");
	}

	public HttpResponse sendPostRequest(String requestUrl,
			Map<String, String> formData) throws IOException {
		return sendRequest(requestUrl, formData, "POST");
	}

	private HttpResponse sendRequest(String requestUrl, Map<String, String> formData,
			String type) throws IOException {

		String requestData = "";

		for (Entry<String, String> entry : formData.entrySet()) {
			requestData += entry.getKey() + "="
					+ URLEncoder.encode(entry.getValue(), charset) + "&";
		}

		HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl)
				.openConnection();
		connection.setReadTimeout(TIMEOUT_MILISECONDS);
		connection.setDoOutput(true);
		connection.setRequestMethod(type);

		connection
				.setRequestProperty("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Accept-Charset", "utf-8;q=0.7,*;q=0.7");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");

		OutputStream output = null;
		try {
			output = connection.getOutputStream();
			output.write(requestData.getBytes(charset));
		} finally {
			tryToCloseStream(output);
		}

		InputStream response = connection.getInputStream();
		
		return new HttpResponse(connection.getResponseCode(), IOUtils.toString(response, charset));

	}

	private void tryToCloseStream(OutputStream output) {
		if (output != null)
			try {
				output.close();
			} catch (IOException logOrIgnore) {
			}
	}

	public HttpResponse sendGetRequest(String requestPath)
			throws OxygenIntegrationException {

		String response = null;
		int code = -1;

		try {
			URL url = new URL(requestPath);
			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setDoOutput(true);

			httpCon.setRequestMethod("GET");
			httpCon.connect();
			
			code = httpCon.getResponseCode();
			
			if (code >= 200 && code < 300){
				InputStream is = httpCon.getInputStream();
				response = IOUtils.toString(is);	
			}
			
			httpCon.disconnect();

		} catch (Exception e) {
			throw new OxygenIntegrationException("Failed get request "
					+ requestPath, e);
		}

		return new HttpResponse(code, response);
	}
}
