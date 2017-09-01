package com.rsicms.rsuite.editors.oxygen.launcher.common.utils.encryption;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.Base64Coder;

public class AESUtils {

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'O', 'x', 'y', 'R',
			'S', 'u', 'A', '7', '#', '6', '!', 'N', 't', 'S', '4', '4' };

	public static String encrypt(String valueToEnc) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = Base64Coder.encodeLines(encValue);
		return encryptedValue;
	}

	public static String decrypt(String encryptedValue) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);

		byte[] decordedValue = Base64Coder.decodeLines(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}
}
