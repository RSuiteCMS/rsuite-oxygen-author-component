package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.FileUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.logging.MainLogger;

public class OxygenSocketServer {


	private File applicationHomeFolder;

	private volatile boolean stopListening = false;

	private volatile ServerSocket serverSocket;

	public OxygenSocketServer(File applicationHomeFolder) {
		this.applicationHomeFolder = applicationHomeFolder;
	}

	public void createSocketAndListen() throws IOException {

		serverSocket = new ServerSocket(0);
		storeSocketInformation(serverSocket);

		while (!stopListening) {
			try {
				Socket clientSocket = serverSocket.accept();
				new OxygenSocketHandler(clientSocket).start();
			} catch (IOException e) {
				Logger logger = MainLogger.getLauncherLogger();
				logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
