package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal;

public class OxygenSocketInfo {

	private String address;
	
	private int portNumber;

	public OxygenSocketInfo(String address, int portNumber) {
		this.address = address.trim();
		this.portNumber = portNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getPortNumber() {
		return portNumber;
	}
	
	
}
