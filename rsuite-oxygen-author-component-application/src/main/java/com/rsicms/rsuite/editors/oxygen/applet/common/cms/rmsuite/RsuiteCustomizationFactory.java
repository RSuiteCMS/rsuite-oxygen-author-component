package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;		
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.xml.sax.EntityResolver;

import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IModifiableCmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ISchemaAwareCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IURLMapper;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IUsageNotificationHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers.SchemaCache;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.temp.OxygenTempFolderManager;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;


public class RsuiteCustomizationFactory implements ICustomizationFactory {

	private IModifiableCmsURI cmsURI;
	
	private RSuiteVersion version = RSuiteVersion.RSUITE_4;

	private EntityResolver rsuiteEntityResolver;
	
	private IURLMapper urlMapper = new RsuiteDefaultURLMapper();
	
	private SchemaCache schemaCache;
	
	private ICmsCustomization cmsCustomization;

	public RsuiteCustomizationFactory() throws OxygenIntegrationException {
	}
	
	private IDocumentURI createCmsUri(OxygenOpenDocumentParmaters parameters) {
		
		String schemaPublicId = parameters.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID);
		String schemaSystemId = parameters.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID);
		String schemaId = parameters.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_ID);
		
		if (rsuiteEntityResolver instanceof RSuiteCachedEntityResolver){
			
			((RSuiteCachedEntityResolver)rsuiteEntityResolver).addPublicSchemaIdMapping(schemaPublicId, schemaId);
			((RSuiteCachedEntityResolver)rsuiteEntityResolver).addSystemSchemaIdMapping(schemaSystemId, schemaId);
		}
		
		return  RSuiteObjectFactory.getRSuiteURI(version, cmsURI, parameters);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.cms.rsuite.CmsCustomizationFactory
	 * #getURLHandlerFactory()
	 */
	@Override
	public URLStreamHandlerFactory getURLHandlerFactory() {
		return new URLStreamHandlerFactory() {
			public URLStreamHandler createURLStreamHandler(String protocol) {

				if ("rsuiteimg".equals(protocol)) {
					return new RsuiteStreamImageURLHandler((RSuiteURI) cmsURI);
				}else if ("rsuite".equals(protocol)) {
					//return new RsuiteStreamURLHandler(cmsURI);
				}

				return null;
			}
		};

	}

	@Override
	public IUsageNotificationHandler getUsageNotificationHandler() {
		return null;
	}

	@Override
	public IOxygenComponentBuilder getComponentBuilder(AuthorComponentFactory factory, OxygenMainComponent mainComponent) {
		return null;
	}

	@Override
	public IDocumentURI createDocumentURI(OxygenOpenDocumentParmaters parameters) {
		return createCmsUri(parameters);
	}



	@Override
	public void initialize(OxygenAppletStartupParmaters parameters) throws OxygenIntegrationException {
		
		String baseUri = parameters.getParameterValue(OxygenAppletStartupParmatersNames.BASE_URI);
		String sessionKey = parameters.getParameterValue(OxygenAppletStartupParmatersNames.SESSION_KEY);
		this.cmsURI = new RSuiteURI(baseUri, sessionKey);
		
		OxygenTempFolderManager tempFolderManager =  new OxygenTempFolderManager(baseUri);
		schemaCache = new SchemaCache(cmsURI, tempFolderManager);
		rsuiteEntityResolver = new RSuiteCachedEntityResolver(cmsURI, schemaCache);
		cmsCustomization = new RSuiteCmsCustomization(this, cmsURI);
		
	}

	
	
	@Override
	public EntityResolver getEntityResolver() {
		
		return rsuiteEntityResolver;
	}

	@Override
	public ICmsURI getCmsURI() {
		return cmsURI;
	}

	@Override
	public String getLicenseKey(ICmsURI cmsUri) throws IOException {
		
		try{
			InputStream input = OxygenIOUtils.loadContentFromURL(cmsUri.getHostURI() + "/rsuite/rest/v1/api/rsuite.oxygen.license?" + cmsUri.getSessionKeyParam());
			return IOUtils.toString(input);
		}catch (FileNotFoundException e){
			//ignore when there is no license
		}
		
		return "";
	}

	@Override
	public IURLMapper getURLMapper() {
		return urlMapper;
	}

	public SchemaCache getSchemaCache() {
		return schemaCache;
	}

	@Override
	final public ICmsCustomization getCmsCustomization() {
		return cmsCustomization;
	}

	public RSuiteVersion getVersion() {
		return version;
	}

	@Override
	public List<ISchemaAwareCustomizationFactory> getSchamaCustomizationFactories(OxygenOpenDocumentParmaters parameters) { 
		return null;
	}

}
