package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.OxygenLauncherParameters;

public class OxygenLauncherJarCreator {

	private static final byte[] BUFFER = new byte[1024 * 1024];

	public ByteArrayOutputStream createLauncherJar(
			OxygenLauncherParameters launcherParameters) throws RSuiteException {

		try {
			return createJarFileWithLauncherParmaters(getLauncherJarTemplate(),
					launcherParameters.getLauncherParameters());
		} catch (IOException e) {
			throw new RSuiteException(0,
					"Unable to create oxygen launcher jar", e);
		}
	}

	private InputStream getLauncherJarTemplate() {
		return this.getClass().getResourceAsStream(
				"/WebContent/launcher/lib/rsuite-oxygen-launcher-template.jar");
	}

	private ByteArrayOutputStream createJarFileWithLauncherParmaters(
			InputStream launcherJarTemplate, String launcherParmaters)
			throws IOException {
		ByteArrayOutputStream launcherJar = new ByteArrayOutputStream();

		try (InputStream templateJarInputStream = launcherJarTemplate;

				ZipInputStream stream = new ZipInputStream(
						templateJarInputStream);
				ZipOutputStream launcherJarZipStream = new ZipOutputStream(
						launcherJar)) {

			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null) {
				launcherJarZipStream.putNextEntry(entry);
				if (!entry.isDirectory()) {
					copy(stream, launcherJarZipStream);
				}
				launcherJarZipStream.closeEntry();
			}

			addLauncherParameters(launcherJarZipStream, launcherParmaters);

		} finally {
		}

		return launcherJar;
	}

	private static void addLauncherParameters(
			ZipOutputStream launcherJarZipStream, String launcherParamaters)
			throws IOException {
		ZipEntry e = new ZipEntry("launcherParameters.txt");
		launcherJarZipStream.putNextEntry(e);
		launcherJarZipStream.write(launcherParamaters.getBytes());
		launcherJarZipStream.closeEntry();
	}

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		int bytesRead;
		while ((bytesRead = input.read(BUFFER)) != -1) {
			output.write(BUFFER, 0, bytesRead);
		}
	}

}
