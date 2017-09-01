package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.encryption.AESUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.FileUtils;

public class RSuiteLocalSession {

	private static final int INVALIDATE_LOCAL_SESSION_HOURS_PERIOD = 12;

	private File authorComponentHomeFolder;

	public RSuiteLocalSession(File authorComponentHomeFolder) {
		this.authorComponentHomeFolder = authorComponentHomeFolder;
	}

	public String getLocalSession() throws OxygenApplicationException {
		File localSession = getLocalSessionFile();

		try {
			if (localSession.exists() && localSession.isFile()
					&& localSessionUpToDate(localSession)) {
				String encodedSession = FileUtils
						.readFileToString(localSession);
				return decryptSessionKey(encodedSession);
			}
		} catch (IOException e) {
			throw new OxygenApplicationException(e);
		}

		return null;
	}

	private boolean localSessionUpToDate(File localSession) {
		long lastModified = localSession.lastModified();
		Date now = new Date();
		long currentTimestamp = now.getTime();

		long delta = currentTimestamp - lastModified;
		long hours = TimeUnit.MILLISECONDS.toHours(delta);

		if (hours < INVALIDATE_LOCAL_SESSION_HOURS_PERIOD) {
			return true;
		}

		return false;
	}

	private File getLocalSessionFile() {
		return new File(authorComponentHomeFolder, "temp");
	}

	public void persistLocalSession(String sessionKey)
			throws OxygenApplicationException {

		try {
			File localSessionFile = getLocalSessionFile();
			localSessionFile.getParentFile().mkdirs();
			FileUtils.writeStringToFile(localSessionFile,
					AESUtils.encrypt(sessionKey));
		} catch (Exception e) {
			throw new OxygenApplicationException(e);
		}

	}

	private String decryptSessionKey(String encryptedValue)
			throws OxygenApplicationException {
		try {
			return AESUtils.decrypt(encryptedValue);
		} catch (Exception e) {
			throw new OxygenApplicationException(e);
		}

	}
}
