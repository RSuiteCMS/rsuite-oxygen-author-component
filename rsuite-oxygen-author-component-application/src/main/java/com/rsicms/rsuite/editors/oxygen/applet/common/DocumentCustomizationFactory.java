package com.rsicms.rsuite.editors.oxygen.applet.common;

import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IComponentInitializeHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class DocumentCustomizationFactory {

	private static List<ISchemaAwareCustomizationFactory> schemaCustomizations = new ArrayList<ISchemaAwareCustomizationFactory>();

	public static IDocumentCustomization createCustomization(
			ICustomizationFactory customizationFactory, IDocumentURI documentUri,
			OxygenOpenDocumentParmaters parameters) {

		String publicSchemaId = parameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID);
		
		String systemSchemaId = parameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID);

		DocumentCustomization customization = new DocumentCustomization();

		ISchemaAwareCustomizationFactory defaultFactory = customizationFactory
				.getCmsCustomization().getDefaultSchemaCustomizationFactory();

		IDocumentHandler documentHandler = null;

		IComponentInitializeHandler componentInitializer = null;

		for (ISchemaAwareCustomizationFactory schemaCustomization : schemaCustomizations) {
			if (schemaCustomization.matchSchema(publicSchemaId) || schemaCustomization.matchSchema(systemSchemaId)) {

				documentHandler = schemaCustomization.createDocumentHandler(documentUri,
						parameters);

				componentInitializer = schemaCustomization
						.createIComponentInitializeHandler(documentUri, parameters);
				break;
			}
		}

		if (documentHandler == null) {
			documentHandler = defaultFactory
					.createDocumentHandler(documentUri, parameters);
		}

		if (componentInitializer == null) {
			componentInitializer = defaultFactory
					.createIComponentInitializeHandler(documentUri, parameters);
		}

		customization.setDocumentHandler(documentHandler);

		customization.setLookupFactory(defaultFactory.createLookupFactory(
				documentUri, parameters));

		customization.setComponentInitializer(componentInitializer);

		return customization;
	}

	public static void registerSchemaCustomization(
			ISchemaAwareCustomizationFactory schemaCustomization) {
		schemaCustomizations.add(schemaCustomization);
	}
}
