package com.rsicms.rsuite.editors.oxygen.applet.domain.authorcomponent;

import static com.rsicms.rsuite.editors.oxygen.applet.components.helpers.OxygenLicenseUtils.setMathFlowLicenses;

import java.net.URL;
import java.util.Map.Entry;

import org.xml.sax.EntityResolver;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;
import ro.sync.ecss.extensions.api.component.listeners.OpenURLHandler;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.component.customization.RSuiteInputURLChooserCustomizer;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.component.customization.RSuiteRelativeReferenceResolver;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.helpers.OxygenLicense;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;

public class AuthorComponentFactoryManager {

	private AuthorComponentFactory authorComponentFactory;

	private static boolean initilized = false;

	private ICustomizationFactory customizationFactory;

	private OxygenConfiguration oxygenConfiguration;

	private OpenURLHandler openURLHandler;

	private ICmsURI cmsUri;
	
	private OxygenBaseDocumentObjectFactory baseOxygenDocumentComponentFactory;

	public AuthorComponentFactoryManager(
			ICustomizationFactory customizationFactory,
			OxygenConfiguration oxygenConfiguration,
			OpenURLHandler openURLHandler) {

		this.customizationFactory = customizationFactory;
		this.oxygenConfiguration = oxygenConfiguration;
		this.openURLHandler = openURLHandler;
		this.cmsUri = customizationFactory.getCmsURI();
	}

	public void initializeAuthorComponentFactory(URL[] frameworkZips,
			URL optionsZipURL, URL codeBase) throws AuthorComponentException {
		authorComponentFactory = AuthorComponentFactory.getInstance();

		authorComponentFactory
				.addInputURLChooserCustomizer(new RSuiteInputURLChooserCustomizer());

		authorComponentFactory.addRelativeReferencesResolver("http",
				new RSuiteRelativeReferenceResolver());
		authorComponentFactory.addRelativeReferencesResolver("https",
				new RSuiteRelativeReferenceResolver());

		OxygenLicense oxygenLicense = new OxygenLicense(customizationFactory,
				cmsUri, oxygenConfiguration);

		if (!initilized) {
			if (oxygenLicense.isFloatingLicense()) {
				authorComponentFactory.init(frameworkZips, optionsZipURL, null,
						OxygenUtils.getApplicationId(),
						oxygenLicense.getLicenseServerUrl(),
						oxygenLicense.getLicenseServerUser(),
						oxygenLicense.getLicenseServerPassword());
			} else {
				authorComponentFactory.init(frameworkZips, optionsZipURL, null,
						OxygenUtils.getApplicationId(),
						oxygenLicense.getFixedLicenseKey());
			}
			initilized = true;
		}
		

		authorComponentFactory.setOpenURLHandler(openURLHandler);

		for (Entry<String, Object> entry : oxygenConfiguration
				.getOxygenConfigurationPropertyMap().entrySet()) {
			authorComponentFactory.setObjectProperty(entry.getKey(),
					entry.getValue());
		}

		EntityResolver resolver = customizationFactory.getEntityResolver();

		if (resolver != null) {
			authorComponentFactory.getXMLUtilAccess()
					.addPriorityEntityResolver(resolver);
		}
		
		baseOxygenDocumentComponentFactory = new OxygenBaseDocumentObjectFactory(authorComponentFactory);
		baseOxygenDocumentComponentFactory.prepareEditorComponentProvider();
		
		setMathFlowLicenses(authorComponentFactory, oxygenConfiguration);
	}

	public AuthorComponentFactory getAuthorComponentFactory() {
		return authorComponentFactory;
	}

	public OxygenBaseDocumentObjects createOxygenBaseDocumentObjects() throws OxygenIntegrationException{
		return baseOxygenDocumentComponentFactory.obtianBaseDocumentObjects();
	}
}
