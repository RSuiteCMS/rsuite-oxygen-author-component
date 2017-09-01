package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;

public class FileHash {

	public static long computeCRC32ForFile(File file) throws IOException {

		CRC32 crc = new CRC32();

		InputStream fis = new FileInputStream(file);
		int n = 0;
		byte[] buffer = new byte[8192];
		while (n != -1) {
			n = fis.read(buffer);
			if (n > 0) {
				crc.update(buffer);
			}
		}

		fis.close();

		return crc.getValue();
	}

	public static long checksumBufferedInputStream(String filepath)
			throws IOException {

		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				filepath));

		CRC32 crc = new CRC32();

		int cnt;

		while ((cnt = inputStream.read()) != -1) {
			crc.update(cnt);
		}
		
		inputStream.close();

		return crc.getValue();
	}

	public static String createSha1(File file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		InputStream fis = new FileInputStream(file);
		int n = 0;
		byte[] buffer = new byte[8192];
		while (n != -1) {
			n = fis.read(buffer);
			if (n > 0) {
				digest.update(buffer, 0, n);
			}
		}

		fis.close();

		byte[] mdbytes = digest.digest();

		StringBuffer sha1 = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sha1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sha1.toString();
	}
}
