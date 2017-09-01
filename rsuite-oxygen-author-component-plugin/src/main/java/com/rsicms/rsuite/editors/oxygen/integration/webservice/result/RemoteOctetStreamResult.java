package com.rsicms.rsuite.editors.oxygen.integration.webservice.result;

import java.io.UnsupportedEncodingException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;

/**
 * Web service result supporting streaming data back to client.
 */
public class RemoteOctetStreamResult extends ByteSequenceResult {

	public RemoteOctetStreamResult(String fileName, String content) throws RSuiteException {
		setSuggestedFileName(fileName);
		setContent(convertStringToBytes(content));
		setContentType("application/octet-stream");
	}
	
	public RemoteOctetStreamResult(String fileName, byte[] content) throws RSuiteException {
		setSuggestedFileName(fileName);
		setContent(content);
		setContentType("application/octet-stream");
	}
	
	private byte[] convertStringToBytes(String content) throws RSuiteException{
		try {
			return content.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}
	
}
