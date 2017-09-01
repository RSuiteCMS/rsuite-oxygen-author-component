package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.temp.OxygenTempFolderManager;

public class SchemaCache {

	private File schemaTempFolder;

	private ICmsURI cmsUri;

	private SizeLimitLinkedHashMap<String, SchemaEntry> memoryCache = new SizeLimitLinkedHashMap<String, SchemaEntry>();

	private static ExecutorService executor = Executors.newFixedThreadPool(2);

	private Map<String, Future<Void>> schemaDownloadRequest = new HashMap<>();

	public SchemaCache(ICmsURI cmsUri, OxygenTempFolderManager tempFolderManager)
			throws OxygenIntegrationException {
		schemaTempFolder = new File(tempFolderManager.getTempFolder(),
				"schemas");

		try {
			if (schemaTempFolder.exists()) {
				FileUtils.deleteDirectory(schemaTempFolder);
			}
		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		}

		if (!schemaTempFolder.mkdirs()) {
			throw new OxygenIntegrationException(
					"Unable to create temp folder "
							+ schemaTempFolder.getAbsolutePath());
		}

		this.cmsUri = cmsUri;
	}

	public void requestSchemaDownload(final String documentId,
			final String schemaId) {
		Future<Void> futureDownloadTask = executor.submit(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				downloadSchemas(documentId, schemaId);
				return null;
			}
		});

		schemaDownloadRequest.put(schemaId, futureDownloadTask);
	}

	public void waitForSchemaDownload(String documentId, String schemaId)
			throws OxygenIntegrationException {
		Future<Void> futureDownloadTask = schemaDownloadRequest.get(schemaId);

		if (futureDownloadTask != null) {
			try {
				futureDownloadTask.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new OxygenIntegrationException(e);
			}
		}
	}

	public void downloadSchemas(String documentId, String schemaId)
			throws Exception {

		File cachedSchemaFile = new File(schemaTempFolder, schemaId);
		if (cachedSchemaFile.exists()) {
			return;
		}

		String schemaWsUrl = cmsUri.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.schema.downloader?"
				+ cmsUri.getSessionKeyParam() + "&rsuiteId=" + documentId;
		InputStream is = OxygenIOUtils.loadContentFromURL(schemaWsUrl);

		unzipSchemaPackageAndLoadToCache(is, schemaTempFolder);
	}

	public void cacheSchema(String schemaId, byte[] schemaContent)
			throws IOException {
		SchemaEntry schemaEntry = new SchemaEntry(schemaId, schemaContent);
		cacheSchemaEntry(schemaEntry);
	}

	public void cacheSchemaEntry(SchemaEntry schemaEntry) throws IOException {
		memoryCache.put(schemaEntry.getSchemaId(), schemaEntry);
		File cachedEntryFile = new File(schemaTempFolder,
				schemaEntry.getSchemaId());
		FileUtils.writeByteArrayToFile(cachedEntryFile,
				schemaEntry.getContent());
	}

	public byte[] getCachedSchema(String schemaId) throws IOException {

		SchemaEntry entry = memoryCache.get(schemaId);

		if (entry == null) {
			File cachedSchemaFile = new File(schemaTempFolder, schemaId);
			if (cachedSchemaFile.exists()) {
				entry = new SchemaEntry(schemaId,
						IOUtils.toByteArray(new FileInputStream(
								cachedSchemaFile)));
			}
		}

		if (entry != null) {
			return entry.getContent();
		}

		return null;
	}

	private void unzipSchemaPackageAndLoadToCache(InputStream in, File outputDir)
			throws IOException, InterruptedException {

		SchemaCacheSerializer serializer = new SchemaCacheSerializer(outputDir);
		BlockingQueue<SchemaEntry> queue = serializer.getQueue();

		serializer.start();

		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze = null;

		while ((ze = zin.getNextEntry()) != null) {

			ByteArrayOutputStream fout = new ByteArrayOutputStream();

			for (int c = zin.read(); c != -1; c = zin.read()) {
				fout.write(c);
			}

			zin.closeEntry();
			fout.flush();
			fout.close();

			String schemaId = ze.getName();
			SchemaEntry entry = new SchemaEntry(schemaId, fout.toByteArray());
			cacheSchemaEntry(entry);
			queue.put(entry);
		}
		zin.close();

		SchemaEntry poisonToken = new SchemaEntry(null, null);
		queue.put(poisonToken);
	}

}
