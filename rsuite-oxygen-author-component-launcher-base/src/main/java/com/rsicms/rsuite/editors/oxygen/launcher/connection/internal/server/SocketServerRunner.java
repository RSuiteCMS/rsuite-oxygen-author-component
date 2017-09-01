package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.server;

import java.io.File;
import java.io.IOException;


public class SocketServerRunner {

	public static void main(String[] args) {
		File applicationFolder = new File("/tmp/oxygenSocket");
		OxygenSocketServer socketServer = new OxygenSocketServer(applicationFolder);
		try {
			socketServer.createSocketAndListen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
