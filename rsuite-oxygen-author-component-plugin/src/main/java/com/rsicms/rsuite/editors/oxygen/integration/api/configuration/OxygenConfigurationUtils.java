package com.rsicms.rsuite.editors.oxygen.integration.api.configuration;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_CONFIGURATION_FILE;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_CONFIGURATION_PRIORITY;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertiesUtils;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginPropertyResult;
import com.rsicms.rsuite.editors.oxygen.integration.utils.PluginUtils;

public class OxygenConfigurationUtils {

	public static InputStream readConfiguration(ExecutionContext context)
			throws RSuiteException {
		PluginPropertyResult resultProperty = PluginPropertiesUtils
				.getPluginProperty(context,
						PROPERTY_OXYGEN_CONFIGURATION_FILE.getProperty(),
						PROPERTY_OXYGEN_CONFIGURATION_PRIORITY.getProperty());
		InputStream is = null;
	
		if (resultProperty != null) {
			String pluginId = resultProperty.getPluginId();
			String resourcePath = resultProperty.getPropertyValue();
			is = PluginUtils.loadResourceFromPlugin(context, pluginId,
					resourcePath);
		}
	
		return is;
	}

	
	public static OpenSubMoPolicy readOpenSubMoConfiguration(ExecutionContext context) throws RSuiteException{
		
		OpenSubMoPolicy openSubMoPolicy = new OpenSubMoPolicy();
		
		try{
		InputStream is = readConfiguration(context);
		if (is != null){
			 DocumentBuilderFactory dbf = context.getXmlApiManager().getDocumentBuilderFactory();
		     DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		     Document document = documentBuilder.parse(is);
			
			XPathFactory xpathFactory = context.getXmlApiManager().getXPathFactory();
			XPath xPath = xpathFactory.newXPath();
			
			Node subMoPolicy = (Node)xPath.evaluate("/*/subMoPolicy",
					 document.getDocumentElement(), XPathConstants.NODE);
			
			openSubMoPolicy = OpenSubMoPolicy.parseSubMoPolicyElement((Element)subMoPolicy);
		}
		}catch (IOException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to read in submo policy", e);
		} catch (XPathExpressionException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to read in submo policy", e);
		} catch (ParserConfigurationException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to read in submo policy", e);
		} catch (SAXException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to read in submo policy", e);
		}
		
		
		return openSubMoPolicy;
	}
}
