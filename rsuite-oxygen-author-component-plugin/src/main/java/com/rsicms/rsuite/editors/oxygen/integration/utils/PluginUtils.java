package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.service.PluginManager;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;

public class PluginUtils {

	@SuppressWarnings("rawtypes")
	public static Class loadClassFromPlugin(Plugin plugin,
			ClassLoader customPluginClassLoader, String className)
			throws RSuiteException {

		ClassLoader oxygenPluginCL = IOxygenIntegrationAdvisor.class.getClassLoader();
		
		ClassLoader classLoader = new StreamClassLoader(plugin,
				customPluginClassLoader, oxygenPluginCL);
		
		Class clazz = null;
		try {
			clazz = classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to load class " + className, e);
		}

		return clazz;
	}
	
	public static InputStream loadResourceFromPlugin(ExecutionContext context, String pluginId, String resourcePath)
			throws RSuiteException {

		PluginManager pluginManger = context.getPluginManager();
		Plugin plugin = pluginManger.get(pluginId);
		
		return plugin.getResourceAsStream(resourcePath);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class loadClassFromPlugin(ExecutionContext context, ClassLoader parentClassLoader, String pluginId, String className)
			throws RSuiteException, ClassNotFoundException {
		
		PluginManager pluginManger = context.getPluginManager();
		Plugin plugin = pluginManger.get(pluginId);

		ClassLoader oxygenPluginCL = PluginUtils.class.getClassLoader();
		
		ClassLoader classLoader = new StreamClassLoader(plugin,
				parentClassLoader, oxygenPluginCL);
		
		return classLoader.loadClass(className);
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public static String getPluginId(Class clazz)
			throws FactoryConfigurationError, XMLStreamException {
		InputStream in = clazz.getResourceAsStream("/rsuite-plugin.xml");

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader

		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		// Read the XML document

		String pluginId = null;

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();
				Attribute attirbute = startElement
						.getAttributeByName(new QName("id"));
				if (attirbute != null) {
					pluginId = attirbute.getValue();
				}
				break;
			}
		}
		return pluginId;
	}

	public static String constructPluginResourceUri(String pluginName, String path) throws URISyntaxException {
		StringBuilder sb = new StringBuilder("rsuite:/res/plugin/");
		sb.append(pluginName);
		sb.append("/").append(path);
	
		return (new URI(sb.toString())).normalize().toString();
	}

}
