package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class SchemaCacheSerializer extends Thread {

	private Logger logger = Logger.getLogger(getClass());
	
	private File schemaTempFolder;

	private volatile LinkedBlockingQueue<SchemaEntry> queue = new LinkedBlockingQueue<SchemaEntry>();

	public SchemaCacheSerializer(File schemaTempFolder) {
		super();
		this.schemaTempFolder = schemaTempFolder;
	}

	@Override
	public void run() {
		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			@Override
			public Void run() {
		
				try {
					while (true) {
						SchemaEntry entry = queue.take();

						if (entry.getSchemaId() == null) {
							break;
						}
						File file = new File(schemaTempFolder, entry.getSchemaId());
						FileUtils.writeByteArrayToFile(file, entry.getContent());
					}
				} catch (IOException e) {
					OxygenUtils.handleException(logger, e);
				} catch (InterruptedException e) {
					OxygenUtils.handleException(logger, e);
				}
		
				return null;
			}
			
		});
		
	}

	public LinkedBlockingQueue<SchemaEntry> getQueue() {
		return queue;
	}

}
