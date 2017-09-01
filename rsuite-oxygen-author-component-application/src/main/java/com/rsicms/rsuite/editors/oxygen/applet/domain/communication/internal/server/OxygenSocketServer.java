package com.rsicms.rsuite.editors.oxygen.applet.domain.communication.internal.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.extension.launch.OpenDocumentManager;


public class OxygenSocketServer {

	private Logger logger = Logger.getLogger(getClass());

	private File applicationHomeFolder;

	private volatile boolean stopListening = false;

	private volatile ServerSocket serverSocket;

	
	
	public OxygenSocketServer(File applicationHomeFolder) {
		this.applicationHomeFolder = applicationHomeFolder;
	}

	public void createSocketAndListen(OpenDocumentManager openDocumentManager) throws IOException {

		serverSocket = new ServerSocket(0);
		storeSocketInformation(serverSocket);

		while (!stopListening) {
			try {
				Socket clientSocket = serverSocket.accept();
				new OxygenSocketHandler(clientSocket, openDocumentManager).start();
			} catch (IOException e) {
				logger.error(e,e);
			}
		}
	}

	private void storeSocketInformation(ServerSocket serverSocket)
			throws IOException {
		StringBuilder socketInfo = createSocketInformation(serverSocket);

		File socketInfoFile = new File(applicationHomeFolder, "socket.info");
		FileUtils.writeStringToFile(socketInfoFile, socketInfo.toString());
	}

	private StringBuilder createSocketInformation(ServerSocket serverSocket) {
		StringBuilder socketInfo = new StringBuilder();

		socketInfo.append(serverSocket.getInetAddress().getHostAddress())
				.append("\n");
		socketInfo.append(serverSocket.getLocalPort()).append("\n");
		return socketInfo;
	}

	public void stopListening() {
		try {
			stopListening = true;
			serverSocket.close();
		} catch (IOException e) {
		}
	}
}
