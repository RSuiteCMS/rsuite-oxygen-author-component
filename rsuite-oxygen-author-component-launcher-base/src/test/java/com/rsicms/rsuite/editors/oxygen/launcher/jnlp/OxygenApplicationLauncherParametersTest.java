package com.rsicms.rsuite.editors.oxygen.launcher.jnlp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class OxygenApplicationLauncherParametersTest {

	@Test
	public void should_parse_jnlp_parameter_list()
			throws OxygenApplicationException {
		String parameterList = "sessionKey=-234392139&baseUri=http%3A%2F%2Flocalhost%3A8080%2Frsuite%2Frest%2Fv2%2Fstatic%2Frsuite%2Foxygen%2Fapplet%2Fintegration&projectName=Rsuite&userName=admin&customizationClass=com.rsicms.rsuite.oxygen.iet.applet.extension.IetOxygenCustomizationFactory&hostAddress=http%3A%2F%2Flocalhost%3A8080";
		OxygenApplicationParameters parameters = new OxygenApplicationParameters(
				parameterList);

		assertEquals(
				"com.rsicms.rsuite.oxygen.iet.applet.extension.IetOxygenCustomizationFactory",
				parameters.getCustomizationClass());
		assertEquals(
				"http://localhost:8080",
				parameters.getHostAddress());
		
		assertNotNull(parameters.getBaseURI());
	}

}
