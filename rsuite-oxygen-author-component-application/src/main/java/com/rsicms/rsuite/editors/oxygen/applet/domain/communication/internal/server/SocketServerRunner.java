package com.rsicms.rsuite.editors.oxygen.applet.domain.communication.internal.server;

import java.io.File;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.launch.OpenDocumentManager;
import com.rsicms.rsuite.editors.oxygen.applet.extension.temp.OxygenTempFolderManager;

public class SocketServerRunner extends Thread {

	private String baseUri;

	private Logger logger = Logger.getLogger(getClass());
	
	private OpenDocumentManager openDocumentManager;

	public SocketServerRunner(String baseUri, OpenDocumentManager openDocumentManager) {
		super();
		this.baseUri = baseUri;
		this.openDocumentManager = openDocumentManager;
	}

	@Override
	public void run() {

		try {
			OxygenTempFolderManager tempFolder = new OxygenTempFolderManager(
					baseUri);
			File applicationFolder = tempFolder.getTempFolder();

			OxygenSocketServer socketServer = new OxygenSocketServer(
					applicationFolder);
			socketServer.createSocketAndListen(openDocumentManager);
		} catch (Exception e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}

	}

}
