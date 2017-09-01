package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;

public class OxygenPluginLibraries {

	private static final String OXYGEN_WEBCONTENT_PATH = "WebContent/";

	private static final String OXYGEN_LIB_PATH = "oxygen-jars/";

	private static final String OXYGEN_FULL_LIBRARY_PATH = OXYGEN_WEBCONTENT_PATH
			+ OXYGEN_LIB_PATH;

	private static final String OXYGEN_ADDITIONAL_LIBRARY_PATH = OXYGEN_LIB_PATH
			+ "oxygen-additional-jar/";

	private static final String OXYGEN_OPTIONAL_LIBRARY_PATH = OXYGEN_LIB_PATH
			+ "oxygen-optional-jar/";

	private String pluginId;
	
	private List<OxygenIngegrationLibrary> baseJars = new ArrayList<>();

	private List<OxygenIngegrationLibrary> additionalJars = new ArrayList<>();

	private Map<String, List<OxygenIngegrationLibrary>> optionalJars = new HashMap<String, List<OxygenIngegrationLibrary>>();

	public OxygenPluginLibraries(String pluginId, JarFile pluginJarFile) {
		this.pluginId = pluginId;
		processPluginJar(pluginJarFile);
	}

	private void processPluginJar(JarFile pluginJarFile) {
		try {
			Enumeration<JarEntry> entries = pluginJarFile.entries();
			while (entries.hasMoreElements()) {
				handleEntry(entries.nextElement());
			}
		} finally {
			if (pluginJarFile != null) {
				closeQuietly(pluginJarFile);
			}
		}
	}

	private void handleEntry(JarEntry entry) {
		String entryName = entry.getName();

		if (!entryName.startsWith(OXYGEN_FULL_LIBRARY_PATH) || entryName.endsWith("/") || isNotArchive(entryName)) {
			return;
		}

		
		entryName = entryName.replace(OXYGEN_WEBCONTENT_PATH, "");

		if (entryName.startsWith(OXYGEN_ADDITIONAL_LIBRARY_PATH)) {
			additionalJars.add(createOxygenLibraryIntegrationObject(entry, entryName));
		} else if (entryName.startsWith(OXYGEN_OPTIONAL_LIBRARY_PATH)) {
			handleOptionalJarEntry(entry, entryName);
		} else {
			baseJars.add(createOxygenLibraryIntegrationObject(entry, entryName));
		}

	}

	private OxygenIngegrationLibrary createOxygenLibraryIntegrationObject(
			JarEntry entry, String entryName) {
		return new OxygenIngegrationLibrary(pluginId, entryName, entry.getSize(), entry.getCrc());
	}

	private boolean isNotArchive(String entryName) {
		String extension = FilenameUtils.getExtension(entryName);
	
		if (extension != null && ("zip".equalsIgnoreCase(extension) || "jar".equalsIgnoreCase(extension))){
			return false;
		}
		
		return true;
	}

	private void handleOptionalJarEntry(JarEntry entry, String entryName) {
		String optionalEntry = entryName.replace(OXYGEN_OPTIONAL_LIBRARY_PATH,
				"");
		int firstSlashIndex = optionalEntry.indexOf("/");
		if (firstSlashIndex > -1) {
			String featureId = optionalEntry.substring(0, firstSlashIndex);
			addOptionalEntry(featureId, entry, entryName);
		}

	}

	private void addOptionalEntry(String featureId, JarEntry entry, String entryName) {
		List<OxygenIngegrationLibrary> optionalFeatureJars = optionalJars.get(featureId);
		if (optionalFeatureJars == null) {
			optionalFeatureJars = new ArrayList<>();
			optionalJars.put(featureId, optionalFeatureJars);
		}

		optionalFeatureJars.add(createOxygenLibraryIntegrationObject(entry, entryName));

	}

	private void closeQuietly(JarFile pluginJarFile) {
		try {
			pluginJarFile.close();
		} catch (IOException e) {

		}
	}

	public List<OxygenIngegrationLibrary> getBaseJars() {
		return createCopy(baseJars);
	}

	public List<OxygenIngegrationLibrary> getAdditionalJars() {
		return createCopy(additionalJars);
	}

	public List<String> getOptionalFeatures() {
		return new ArrayList<>(optionalJars.keySet());
	}

	public List<OxygenIngegrationLibrary> getOptionalJars(String featureId) {
		return createCopy(optionalJars.get(featureId));
	}

	private List<OxygenIngegrationLibrary> createCopy(Collection<OxygenIngegrationLibrary> originalList) {
		if (originalList == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(originalList);
	}

}
