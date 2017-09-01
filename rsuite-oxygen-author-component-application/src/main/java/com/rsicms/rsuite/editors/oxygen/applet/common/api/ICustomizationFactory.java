package com.rsicms.rsuite.editors.oxygen.applet.common.api;

import java.io.IOException;
import java.net.URLStreamHandlerFactory;
import java.util.List;

import org.xml.sax.EntityResolver;

import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public interface ICustomizationFactory {

	void initialize(OxygenAppletStartupParmaters parameters) throws OxygenIntegrationException;
	
	IDocumentURI createDocumentURI(OxygenOpenDocumentParmaters paramaters);
	
	ICmsURI getCmsURI();
	
	ICmsCustomization getCmsCustomization();

	URLStreamHandlerFactory getURLHandlerFactory();
	
	EntityResolver getEntityResolver();
	
	String getLicenseKey(ICmsURI cmsUri) throws IOException;

	IUsageNotificationHandler getUsageNotificationHandler();

	List<ISchemaAwareCustomizationFactory> getSchamaCustomizationFactories(OxygenOpenDocumentParmaters parameters);
	
	IOxygenComponentBuilder getComponentBuilder(AuthorComponentFactory factory, OxygenMainComponent mainComponent);
	
	IURLMapper getURLMapper();
	
}