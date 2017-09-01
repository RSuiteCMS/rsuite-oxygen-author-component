package com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.client;

import com.rsicms.rsuite.editors.oxygen.launcher.connection.internal.OxygenSocketInfo;

public class OxygenSocketClientFactory {

	public OxygenSocketClient createOxygenSocketClient(OxygenSocketInfo oxygenSocketInfo){
		return new OxygenSocketClient(oxygenSocketInfo);
	}
}
