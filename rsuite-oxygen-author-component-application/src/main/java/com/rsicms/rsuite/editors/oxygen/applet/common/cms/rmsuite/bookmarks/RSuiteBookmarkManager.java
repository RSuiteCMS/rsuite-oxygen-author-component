package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.bookmarks;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IBookmarkManager;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.HttpUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;

public class RSuiteBookmarkManager implements IBookmarkManager {

	private Logger logger = Logger.getLogger(getClass());

	private String bookmarkRequestBasePath;

	public RSuiteBookmarkManager(ICmsURI cmsURI) {

		bookmarkRequestBasePath = cmsURI.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.bookmarks.manager?"
				+ cmsURI.getSessionKeyParam();
	}

	@Override
	public boolean addBookmark(String cmsId) {

		String operation = "add";

		return sendBookmarkRequest(cmsId, operation);
	}

	@Override
	public boolean removeBookmark(String cmsId) {
		String operation = "remove";

		return sendBookmarkRequest(cmsId, operation);
	}

	private boolean sendBookmarkRequest(String cmsId, String operation) {
		String requestPath = bookmarkRequestBasePath + "&moId=" + cmsId
				+ "&operation=" + operation;

		try {
			String response = HttpUtils.sendGetRequest(requestPath);

			if (!"<ok/>".equalsIgnoreCase(response)) {
				OxygenMainComponent
						.getCurrentInstance()
						.getDialogHelper()
						.showErrorMessage(
								"Unable to " + operation + " bookmark " + cmsId);
				logger.error(response);
			}

			return true;

		} catch (OxygenIntegrationException e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}

		return false;
	}

}
