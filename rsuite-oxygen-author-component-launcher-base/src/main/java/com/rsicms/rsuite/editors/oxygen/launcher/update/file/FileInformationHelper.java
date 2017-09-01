package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.Base64Coder;

public class FileInformationHelper {

	
	public static String serializeAndBase64EncodeLocalFiles(List<FileInformation> localFiles) {
		StringBuilder requestMessage = new StringBuilder();

		for (FileInformation localFile : localFiles) {
			requestMessage.append(localFile.toString()).append("\n");
		}
		
		String encodedRequest = Base64Coder.encodeString(requestMessage
				.toString());
		
		return encodedRequest;
	}
}
