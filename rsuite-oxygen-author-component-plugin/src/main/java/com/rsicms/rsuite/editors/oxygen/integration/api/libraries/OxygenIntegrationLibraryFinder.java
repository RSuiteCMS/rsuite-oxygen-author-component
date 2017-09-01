package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.service.PluginManager;

public class OxygenIntegrationLibraryFinder {

	public static OxygenIngegrationLibrary getOxygenIngegrationLibrary(ExecutionContext context,
			String pluginId, String oxygenLibraryPath) throws RSuiteException {
		PluginManager pluginManager = context.getPluginManager();
		Plugin plugin = pluginManager.get(pluginId);
		return getOxygenIngegrationLibrary(plugin, oxygenLibraryPath);
	}
	
	public static OxygenIngegrationLibrary getOxygenIngegrationLibrary(
			Plugin plugin, String oxygenLibraryPath) throws RSuiteException {

		String staticWebServicePluginPath = getStaticWebServicePluginPath(plugin);
		ZipEntry oxygenLibraryEntry = getJarEntry(plugin,
				staticWebServicePluginPath, oxygenLibraryPath);

		return new OxygenIngegrationLibrary(plugin.getId(), oxygenLibraryPath,
				oxygenLibraryEntry.getSize(), oxygenLibraryEntry.getCrc());
	}

	private static ZipEntry getJarEntry(Plugin plugin,
			String staticWebservicePath, String oxygenLibraryPath)
			throws RSuiteException {
		File location = plugin.getLocation();
		JarFile jarFile = null;
		try {
			if (staticWebservicePath.startsWith("/")
					&& staticWebservicePath.length() > 0) {
				staticWebservicePath = staticWebservicePath.substring(1);
			}

			jarFile = new JarFile(location);
			String entryPath = staticWebservicePath + "/" + oxygenLibraryPath;
			ZipEntry oxygenLibraryEntry = jarFile.getEntry(entryPath);

			if (oxygenLibraryEntry == null) {
				throw new RSuiteException("Unable to find " + entryPath
						+ " in plugin " + plugin.getId());
			}

			return oxygenLibraryEntry;

		} catch (IOException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		} finally {
			closeQuitely(jarFile);
		}
	}

	private static void closeQuitely(JarFile jarFile) {
		if (jarFile != null) {
			try {
				jarFile.close();
			} catch (Exception e) {
			}
		}

	}

	private static String getStaticWebServicePluginPath(Plugin rsuitePlugin)
			throws RSuiteException {

		String staticWebservicePath = null;
		List<Object> webServiceProviderList = rsuitePlugin
				.getExtensionProviderList("rsuite.WebService");

		for (Object webServiceConfiguration : webServiceProviderList) {
			String className = webServiceConfiguration.getClass().getName();

			if ("com.reallysi.rsuite.system.plugin.extensions.StaticWebServiceConfig"
					.equals(className)) {
				staticWebservicePath = getPathFromStaticWebServiceConfig(webServiceConfiguration);
			}

		}

		if (staticWebservicePath == null) {
			throw new RSuiteException(
					"Unable to find static webservice path for plugin "
							+ rsuitePlugin.getId());
		}

		return staticWebservicePath;

	}

	private static String getPathFromStaticWebServiceConfig(
			Object webServiceConfiguration) throws RSuiteException {
		try {
			Field field = webServiceConfiguration.getClass().getDeclaredField(
					"path");
			field.setAccessible(true);
			String path = (String) field.get(webServiceConfiguration);
			return path;
		} catch (Exception e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}
	}


