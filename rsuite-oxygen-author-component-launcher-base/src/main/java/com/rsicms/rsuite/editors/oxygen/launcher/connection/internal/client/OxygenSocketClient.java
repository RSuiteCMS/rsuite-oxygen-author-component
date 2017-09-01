package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.OxygenSocketInfo;
import com.rsicms.rsuite.editors.oxygen.launcher.logging.MainLogger;

public class OxygenSocketClient {

	
	private PrintWriter socketOutput;

	private BufferedReader socketInput;

	private Socket oxygenApplicationSocket;

	private OxygenSocketInfo socketInfo;

	public OxygenSocketClient(OxygenSocketInfo socketInfo) {

		this.socketInfo = socketInfo;

	}

	public boolean tryOpenConnection()  {
		try {
			openConnection();
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public void openConnection() throws IOException {

			oxygenApplicationSocket = new Socket(socketInfo.getAddress(), socketInfo.getPortNumber());
			oxygenApplicationSocket.setSoTimeout(1000);
			socketOutput = new PrintWriter(oxygenApplicationSocket.getOutputStream(),
					true);
					
			socketInput = new BufferedReader(new InputStreamReader(
					oxygenApplicationSocket.getInputStream()));

	}

	public void openNewDocument(String documentIdToOpen) throws IOException {
		socketOutput.println("OPEN DOCUMENT: " + documentIdToOpen);	
	}

	public boolean pingOxygenApplication() throws IOException {
		socketOutput.println("PING");
		
		try{
			String readLine = socketInput.readLine();
			if ("oxygen".equals(readLine)){
				return true;
			}
		}catch (SocketTimeoutException e){
			return false;
		}
		
		return false;
	}

	public void closeConnection() {
		if (oxygenApplicationSocket != null && !oxygenApplicationSocket.isClosed()){
			try {
				oxygenApplicationSocket.close();
			} catch (IOException e) {
				Logger logger = MainLogger.getLauncherLogger();
				logger.log(Level.SEVERE, "Unable to close the socket connection", e);
			}
		}
	}
}
