package com.rsicms.rsuite.editors.oxygen.applet.domain.communication.internal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.launch.OpenDocumentManager;


public class OxygenSocketHandler extends Thread {

	private Logger logger = Logger.getLogger(getClass());
	
	private Socket clientSocket;

	private PrintWriter socketOutput = null;
	
	private BufferedReader socketInput = null;

	private OpenDocumentManager openDocumentManager;
	
	public OxygenSocketHandler(Socket clientSocket, OpenDocumentManager openDocumentManager) {
		this.clientSocket = clientSocket;
		this.openDocumentManager = openDocumentManager;
	}

	@Override
	public void run() {

		try {
			createInputOutputStreams();

			String request;

			while ((request = socketInput.readLine()) != null) {
				handleRequest(socketOutput, request);
			}

		} catch (IOException e) {
			logger.error(e, e);
		} finally {
			closeInputOutputStreams();
		}
	}

	private void createInputOutputStreams() throws IOException {
		socketOutput = new PrintWriter(clientSocket.getOutputStream(), true);
		socketInput = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
	}

	private void handleRequest(PrintWriter socketOutput, String request) {
		
		if ("PING".equals(request)) {
			socketOutput.println("oxygen");
		}else if (request.startsWith("OPEN DOCUMENT")){			
			String[] split = request.split(":");
			String objectId = split[1].trim();
			try {
				openDocumentManager.bringApplicationToFront();				
				openDocumentManager.openDocumentInANewTab(objectId);				
				socketOutput.println("OK");
			} catch (Exception e) {
				socketOutput.println("FAIL");
				OxygenUtils.handleException(logger, e);
			}
		}
	}

	
	private void closeInputOutputStreams() {
		IOUtils.closeQuietly(socketInput);
		IOUtils.closeQuietly(socketOutput);
	}
}
