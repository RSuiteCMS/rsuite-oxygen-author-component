package com.rsicms.rsuite.editors.oxygen.launcher.jnlp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class OxygenParamererListParser {

	public static Map<String, String> parseParameters(String parameterList)
			throws OxygenApplicationException {

		Map<String, String> parsedParameters = new HashMap<String, String>();

		String[] splitedParameters = parameterList.split("&");
		for (String parameterEntry : splitedParameters) {
			String[] parameter = parameterEntry.split("=");
			parsedParameters.put(parameter[0], decodeValue(parameter[1]));
		}
		return parsedParameters;
	}

	private static String decodeValue(String value) throws OxygenApplicationException {
		try {
			return URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new OxygenApplicationException(e);
		}
	}
	
}
