package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenOptionalFeatures;

public class OxygenIntegrationLibraryManager {

	private ExecutionContext context;
	
	private Set<OxygenOptionalFeatures> optionalFeatures = new HashSet<>();

	private OxygenCustomJarManager customJarManager;
	
	private static OxygenPluginLibraries oxygenIntegrationPluginLibraries;

	public static void reloadOxygenPluginLibraries(Plugin oxygenIntegrationPlugin) throws IOException{
		File pluginJarFile = oxygenIntegrationPlugin.getLocation();
		JarFile pluginJar = new JarFile(pluginJarFile);
		oxygenIntegrationPluginLibraries = new OxygenPluginLibraries(oxygenIntegrationPlugin.getId(), pluginJar);
	}
	
	public OxygenIntegrationLibraryManager(ExecutionContext context, List<OxygenOptionalFeatures> optionalFeatures, OxygenCustomJarManager customJarManager) {
		this(context);
		
		if (optionalFeatures != null){
			for (OxygenOptionalFeatures feature : optionalFeatures){
				this.optionalFeatures.add(feature);	
			}
		}
		this.customJarManager = customJarManager;		
	}
	
	private OxygenIntegrationLibraryManager(ExecutionContext context) {
		this.context = context;
	}

	public List<OxygenIngegrationLibrary> getIntegrationLibraries() throws RSuiteException {

		List<OxygenIngegrationLibrary> libraries = new ArrayList<>();
		
		libraries.addAll(collectJarsFromOxygenIntegrationPlugin());
		libraries.addAll(OxygenFrameworkJarManager.collectJarsFromFrameworksPlugins(context));
		libraries.addAll(customJarManager.getJarsFromCustomRSuitePlugin());
		
		return libraries;
	}
	
	private List<OxygenIngegrationLibrary> collectJarsFromOxygenIntegrationPlugin(){
		List<OxygenIngegrationLibrary> libraries = new ArrayList<>();
		
		libraries.addAll(oxygenIntegrationPluginLibraries.getBaseJars());
		libraries.addAll(oxygenIntegrationPluginLibraries.getAdditionalJars());
		
		
		for (OxygenOptionalFeatures feature : optionalFeatures){
			libraries.addAll(oxygenIntegrationPluginLibraries.getOptionalJars(feature.getFeatureId()));
		}
		
		return libraries;
	}	
}
