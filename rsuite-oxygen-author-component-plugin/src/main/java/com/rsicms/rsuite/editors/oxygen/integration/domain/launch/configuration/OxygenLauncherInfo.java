package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.reallysi.rsuite.api.RSuiteException;

public class OxygenLauncherInfo {

	public String getLauncherVersion() throws RSuiteException{
		
		try(InputStream inputStream = getLauncherPropertiesStream()) {

			if (inputStream != null){
				Properties properties = new Properties();
				properties.load(inputStream);
				String version = properties.getProperty("version");
				
				if (version != null){
					return version;
				}
			}			
		}catch(IOException e){
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
		
		throw new RSuiteException("Launcher version is not present");
	}

	private InputStream getLauncherPropertiesStream() {
		return this.getClass().getResourceAsStream("/WebContent/launcher/launcher.properties");
	}
}
