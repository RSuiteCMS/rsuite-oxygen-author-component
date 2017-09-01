package com.rsicms.rsuite.editors.oxygen.launcher.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.ExceptionUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.OxygenTempFolderManager;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.FileUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.OxygenSocketInfo;
import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client.OxygenSocketClient;
import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client.OxygenSocketClientFactory;

public class OxygenApplication {

	private File applicationHomeFolder;
	
	private OxygenSocketClientFactory oxygenSocketClientFactory = new OxygenSocketClientFactory();
	
	public OxygenApplication(File applicationHomeFolder) {
		this.applicationHomeFolder = applicationHomeFolder;
	}
	
	public OxygenApplication(String baseURI) throws OxygenApplicationException{
		OxygenTempFolderManager tempFolderManager = new OxygenTempFolderManager(baseURI);
		File tempFolder = tempFolderManager.getTempFolder();
		applicationHomeFolder = new File(tempFolder,
				"application");
		applicationHomeFolder.mkdirs();
	}

	
	@SuppressWarnings("unchecked")
	public OxygenSocketInfo getOxygenSocketInformation() throws OxygenApplicationException{
		
		File socketInfoFile = new File(applicationHomeFolder.getParentFile(), "socket.info");
		
		if (!socketInfoFile.exists()){
			throw new OxygenApplicationException("The socket information file does not exist");
		}
		
		try {
			List<String> socketInfoLines = FileUtils.readLines(socketInfoFile,
					"utf-8");
			int portNumber = Integer.parseInt(socketInfoLines.get(1).trim());
			return new OxygenSocketInfo(socketInfoLines.get(0), portNumber);
		} catch (IOException e) {
			throw new OxygenApplicationException("Unable to read in the socket information file");
		}		
	}
	
	public boolean isOxygenApplicationLaunched() throws OxygenApplicationException {
		
		OxygenSocketClient oxygenSocketClient = null;
		
		try{
			OxygenSocketInfo oxygenSocketInformation = getOxygenSocketInformation();
			oxygenSocketClient = oxygenSocketClientFactory.createOxygenSocketClient(oxygenSocketInformation);
			
			if (oxygenSocketClient.tryOpenConnection()){
				return oxygenSocketClient.pingOxygenApplication();				
			}
		} catch (IOException | OxygenApplicationException e) {
			ExceptionUtils.handleException(e);
			return false;
		}finally{
			closeOxygenClientQuietly(oxygenSocketClient);
		}

		return false;
	}
	
	private void closeOxygenClientQuietly(OxygenSocketClient oxygenSocketClient) {
		if (oxygenSocketClient != null){
			oxygenSocketClient.closeConnection();	
		}		
	}


	public File getApplicationHomeFolder() {
		return applicationHomeFolder;
	}

}
