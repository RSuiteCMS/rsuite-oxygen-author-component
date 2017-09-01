package com.rsicms.rsuite.editors.oxygen.integration.webservice.result;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.ResponseStatus;

/**
 * Web service result supporting streaming data back to client.
 */
public class RemoteBinaryStreamResult extends DefaultRemoteApiResult {

	private InputStream inputStream;

	private String contentType;
	
	private String encoding = "UTF-8"; // Default encoding.
	
	private ResponseStatus responseStatus = ResponseStatus.SUCCESS;

	private String suggestedFileName;
	
	public RemoteBinaryStreamResult(ExecutionContext context, ManagedObject mo)
			throws RSuiteException {
		String mtype = null;
		if (mo.isNonXml()) {
			suggestedFileName = mo.getDisplayName();
		} else {
			suggestedFileName = mo.getId() + ".xml";
		}

		String path = mo.getExternalAssetPath();
		if (path == null)
			path = getSuggestedFileName();
		if (path != null) {
			int n = path.lastIndexOf('.');
			if (n > 0) {
				mtype = context.getConfigurationService()
						.getMimeMappingCatalog()
						.getMimeTypeByExtension(path.substring(n + 1));
			}						
		}
		
		if (mtype == null){
			mtype = mo.getContentType();
		}
		
		if (mtype == null && StringUtils.isBlank(mtype)) {
			mtype = "application/octet-stream";
		}
		contentType = mtype;
		inputStream = mo.getInputStream();
	}

	public RemoteBinaryStreamResult(InputStream inputStream) {
		this.contentType = "image/jpeg";
		this.inputStream = inputStream;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	@Override
	public String getSuggestedFileName() {
		return suggestedFileName;
	}

	@Override
	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus  = responseStatus;

	}
}
