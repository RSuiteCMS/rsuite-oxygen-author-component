package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp;

import static com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp.BaseLauncherJnlpGenerator.generateJnlpFromTemplate;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.BaseOxygenLaunchParameters;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.OxygenLauncherParameters;

public class ApplicationLauncherJnlpGenerator {

	public static String generateJnlp(XmlApiManager xmlManager,
			OxygenLauncherParameters launcherParameters) throws RSuiteException {

		BaseOxygenLaunchParameters jnlpParameters = new BaseOxygenLaunchParameters(
				launcherParameters.getHostAddress(),
				launcherParameters.getHostAddress());

		return generateJnlpFromTemplate(xmlManager, jnlpParameters,
				"/WebContent/launcher/jnlp/application/JNLP-INF/APPLICATION_TEMPLATE.JNLP");
	}
}
