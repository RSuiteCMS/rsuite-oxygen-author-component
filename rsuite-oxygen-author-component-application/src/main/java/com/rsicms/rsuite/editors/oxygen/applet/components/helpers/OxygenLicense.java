package com.rsicms.rsuite.editors.oxygen.applet.components.helpers;

import static com.rsicms.rsuite.editors.oxygen.applet.components.helpers.OxygenLicenseUtils.getLicenseKey;

import org.apache.commons.lang.StringUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.LicenseServerConfiguration;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;

public class OxygenLicense {

	private boolean floatingLicense = true;

	private String fixedLicenseKey;

	private LicenseServerConfiguration licenseServer;
	
	private ICmsURI cmsUri;

	public OxygenLicense(final ICustomizationFactory customizationFactory,
			final ICmsURI cmsUri, final OxygenConfiguration oxygenConfiguration) {

		this.cmsUri = cmsUri;
		fixedLicenseKey = getLicenseKey(
				customizationFactory, cmsUri);

		if (StringUtils.isBlank(fixedLicenseKey)) {
			licenseServer = oxygenConfiguration.getLicenseServerConfiguration();
		} else {
			floatingLicense = false;
		}
	}

	public boolean isFloatingLicense() {
		return floatingLicense;
	}

	public String getFixedLicenseKey() {
		return fixedLicenseKey;
	}

	public String getLicenseServerUser() {
		return licenseServer.getUser();
	}

	public String getLicenseServerPassword() {
		return licenseServer.getPassword();
	}

	public String getLicenseServerUrl() {
		String licenseURL = licenseServer.getUrl();

		if (licenseURL.startsWith("/")) {
			licenseURL = cmsUri.getHostURI() + licenseURL;
		}

		return licenseURL;
	}


}
