package com.rsicms.rsuite.editors.oxygen.launcher.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplication;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.system.CommandExecutionResult;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.system.SystemUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client.OxygenSocketClient;
import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client.OxygenSocketClientFactory;

public class OxygenApplicationRunner {

	private OxygenApplication oxygenApplication;

	private OxygenSocketClientFactory oxygenSocketClientFactory;

	public OxygenApplicationRunner(OxygenApplication oxygenApplication) {
		this.oxygenApplication = oxygenApplication;
		this.oxygenSocketClientFactory = new OxygenSocketClientFactory();
	}

	public OxygenApplicationRunner(OxygenApplication oxygenApplication,
			OxygenSocketClientFactory oxygenSocketClientFactory) {
		this.oxygenApplication = oxygenApplication;
		this.oxygenSocketClientFactory = oxygenSocketClientFactory;
	}

	void startNewOxygenApplication(String startUpArguments)
			throws OxygenApplicationException {
		File applicationHomeFolder = oxygenApplication
				.getApplicationHomeFolder();
		List<String> command = new ArrayList<String>();
		command.add("java");
		command.add("-Xms256m");
		command.add("-cp");
		command.add("" + applicationHomeFolder + "/*");
		command.add("com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneRunner");
		command.add(startUpArguments);

		CommandExecutionResult executionResult = SystemUtils.executeOScommand(
				command, applicationHomeFolder);

		StringBuilder error = executionResult.getError();
		if (error.length() > 0 || executionResult.getResultCode() != 0) {
			throw new OxygenApplicationException(executionResult.getOutput()
					+ "\n" + error.toString());
		}

	}

	public void openDocumentInNewTab(String documentIdToOpen)
			throws OxygenApplicationException {

		OxygenSocketClient oxygenSocketClient = oxygenSocketClientFactory
				.createOxygenSocketClient(oxygenApplication
						.getOxygenSocketInformation());

		try {
			oxygenSocketClient.openConnection();
			oxygenSocketClient.openNewDocument(documentIdToOpen);
		} catch (IOException e) {
			throw new OxygenApplicationException(e.getMessage(), e);
		} finally {
			closeOxygenClientQuietly(oxygenSocketClient);
		}

	}

	private void closeOxygenClientQuietly(OxygenSocketClient oxygenSocketClient) {
		if (oxygenSocketClient != null) {
			oxygenSocketClient.closeConnection();
		}
	}

}
