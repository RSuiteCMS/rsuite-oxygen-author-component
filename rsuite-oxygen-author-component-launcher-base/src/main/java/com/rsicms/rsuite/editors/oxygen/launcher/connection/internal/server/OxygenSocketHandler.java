package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.IOUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.logging.MainLogger;

public class OxygenSocketHandler extends Thread {


	private Socket clientSocket;

	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;

	public OxygenSocketHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
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
			Logger logger = MainLogger.getLauncherLogger();
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
		}
	}

	private void closeInputOutputStreams() {
		IOUtils.closeQuietly(socketInput);
		IOUtils.closeQuietly(socketOutput);
	}
}
