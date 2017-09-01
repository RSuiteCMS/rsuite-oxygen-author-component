package com.rsicms.rsuite.editors.oxygen.launcher.common.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;


import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.IOUtils;


public class HttpUtils {

	private static final int TIMEOUT_MILISECONDS = 100000;

	private static String charset = "UTF-8";

	public static String sendPutRequest(String requestUrl, Map<String, String> formData) throws IOException {
		return sendRequest(requestUrl,  formData, "PUT");
	}
	
	public static String sendPostRequest(String requestUrl, Map<String, String> formData) throws IOException {
		return sendRequest(requestUrl,  formData, "POST");
	}
	
	private static String sendRequest(String requestUrl, Map<String, String> formData, String type) throws IOException {

		
		String requestData = "";
		
		for (Entry<String, String> entry : formData.entrySet()){
			requestData += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), charset) + "&";
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
		return IOUtils.toString(response, charset);

	}

	private static void tryToCloseStream(OutputStream output) {
		if (output != null)
			try {
				output.close();
			} catch (IOException logOrIgnore) {
			}
	}

	public static String sendGetRequest(String requestPath)
			throws OxygenApplicationException {
	
		String response = null;
	
		try {
			URL url = new URL(requestPath);
			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setDoOutput(true);
	
			httpCon.setRequestMethod("GET");
			httpCon.connect();
			InputStream is = httpCon.getInputStream();
			response = IOUtils.toString(is);
			httpCon.disconnect();
	
		} catch (Exception e) {
			throw new OxygenApplicationException("Failed get request "
					+ requestPath, e);
		}
	
		return response;
	}

    public static InputStream downloadFile(String fileURL) throws IOException {
        URL url = new URL(fileURL);

        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
 
        if (responseCode == HttpURLConnection.HTTP_OK) {
           return httpConn.getInputStream();
 
        } else {
            throw new IOException(
                    "No file to download. Server replied HTTP code: "
                            + responseCode);
        }
    }
 
}
