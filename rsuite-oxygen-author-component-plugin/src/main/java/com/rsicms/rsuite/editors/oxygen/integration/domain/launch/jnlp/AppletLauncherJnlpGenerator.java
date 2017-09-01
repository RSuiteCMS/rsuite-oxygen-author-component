package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp;

import static com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp.BaseLauncherJnlpGenerator.generateJnlpFromTemplate;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.BaseOxygenLaunchParameters;

public class AppletLauncherJnlpGenerator {

	public static String generateJnlp(XmlApiManager xmlManager, BaseOxygenLaunchParameters jnlpParameters) throws RSuiteException {

		return generateJnlpFromTemplate(xmlManager, jnlpParameters, "/WebContent/launcher/jnlp/applet/JNLP-INF/APPLICATION_TEMPLATE.JNLP");
	}
	
}
