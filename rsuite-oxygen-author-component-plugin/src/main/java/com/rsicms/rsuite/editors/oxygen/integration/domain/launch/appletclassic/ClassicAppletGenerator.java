package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.appletclassic;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenIntegrationAdvisorLibraryListCreator;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.domain.PluginWebContentManager;

public class ClassicAppletGenerator {

	private PluginWebContentManager pluginWebContentManager = new PluginWebContentManager();

	private final String APPLET_TAG_XSLT_PATH;

	private ExecutionContext context;

	public ClassicAppletGenerator(ExecutionContext context) {
		this.context = context;
		APPLET_TAG_XSLT_PATH = pluginWebContentManager
				.generatePluginPath("editor/applet.classic.html.tag.xsl");
	}

	public String generateAppletTag(IOxygenWebEditingContext webContext,
			IOxygenIntegrationAdvisor advisor, MoToLaunch moToLunch)
			throws RSuiteException {
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);

		Map<String, String> transformParams = createTransformParameters(
				webContext, advisor, moToLunch);
		transform(context, APPLET_TAG_XSLT_PATH, transformParams, result);

		return writer.toString();
	}

	private Map<String, String> createTransformParameters(
			IOxygenWebEditingContext webContext,
			IOxygenIntegrationAdvisor advisor, MoToLaunch moToLunch)
			throws RSuiteException {

		OxygenIntegrationAdvisorLibraryListCreator libraryList = new OxygenIntegrationAdvisorLibraryListCreator(
				context);

		return ClassicAppletTransformParamGenerator.createTransformParameters(
				libraryList, pluginWebContentManager, webContext, advisor,
				moToLunch);
	}

	private void transform(ExecutionContext context, String xsltUri,
			Map<String, String> transformParams, Result result)
			throws RSuiteException {

		Transformer transformer = getTransformer(context, xsltUri);

		Reader reader = new StringReader("<xml />");

		StreamSource input = new StreamSource(reader);

		for (String paramName : transformParams.keySet()) {
			String value = transformParams.get(paramName);
			if (value != null) {
				transformer.setParameter(paramName, value);
			}
		}

		try {
			transformer.transform(input, result);
		} catch (TransformerException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"unable to transform", e);
		}

	}

	private Transformer getTransformer(ExecutionContext context, String xsltUri)
			throws RSuiteException {
		Transformer transformer = null;

		try {
			transformer = context.getXmlApiManager().getTransformer(
					new URI(xsltUri));
		} catch (URISyntaxException e) {
			throw new RSuiteException("unable to get " + xsltUri + ": "
					+ e.getMessage());

		}

		return transformer;
	}

}
