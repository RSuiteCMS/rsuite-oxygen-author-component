package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.BaseOxygenLaunchParameters;

class BaseLauncherJnlpGenerator {

	private static final String xls = OxygenConstants.RSUITE_RESOURSE_PLUGIN_PREFIX
			+ "editor/jnlp-template-to-jnlp.xsl";

	static String generateJnlpFromTemplate(XmlApiManager xmlManager, BaseOxygenLaunchParameters jnlpParameters, String pathToJnlpTemplate) throws RSuiteException {

		try (StringWriter stringWriter = new StringWriter();
				InputStream jnlpTemplateStream = getTemplateStream(pathToJnlpTemplate);) {
			StreamSource inputSource = new StreamSource(jnlpTemplateStream);
			StreamResult streamResult = new StreamResult(stringWriter);
			Transformer transformer = xmlManager.getTransformer(new URI(xls));
			setUpTransfomrParmaters(transformer, jnlpParameters);
			transformer.transform(inputSource, streamResult);
			return stringWriter.toString();
		} catch (IOException | TransformerException | URISyntaxException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}


	private static void setUpTransfomrParmaters(Transformer transformer,
			BaseOxygenLaunchParameters jnlpParameters) throws RSuiteException {

		transformer.setParameter("rsuite.baseUri", jnlpParameters.getHostAddress());
		transformer.setParameter("rsuite.startArguments", jnlpParameters.createJnlpParameterList());

	}

	private static InputStream getTemplateStream(String pathToJnlpTemplate) {
		return BaseLauncherJnlpGenerator.class
				.getResourceAsStream(pathToJnlpTemplate);
	}

	
}
