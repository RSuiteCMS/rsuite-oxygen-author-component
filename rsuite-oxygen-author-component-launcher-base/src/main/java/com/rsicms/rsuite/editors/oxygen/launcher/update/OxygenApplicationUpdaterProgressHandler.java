package com.rsicms.rsuite.editors.oxygen.launcher.update;

public interface OxygenApplicationUpdaterProgressHandler {

	void updateProgress(long downloadedBytes, long bytesToDownload);
}
