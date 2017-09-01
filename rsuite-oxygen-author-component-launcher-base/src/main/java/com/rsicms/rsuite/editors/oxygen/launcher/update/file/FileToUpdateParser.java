package com.rsicms.rsuite.editors.oxygen.launcher.update.file;

import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.Base64Coder;

public class FileToUpdateParser {

	public static List<FileToUpdate> parseBase64EncodedFilesToUpdate(
			String filesToUpdateResponse) {

		String decodedString = Base64Coder.decodeString(filesToUpdateResponse);

		List<FileToUpdate> filesToUpdate = new ArrayList<>();

		String[] lines = decodedString.split("\n");

		for (String line : lines) {
			if (line.trim().isEmpty()){
				continue;
			}
			filesToUpdate.add(parseLine(line));
		}

		return filesToUpdate;
	}

	private static FileToUpdate parseLine(String line) {
		String[] fileToUpdate = line.split("\\|");

		String fileName = getValue(fileToUpdate[0]);
		
		String operation = getValue(getValue(fileToUpdate[1]));
		FileOperation fileOperation = FileOperation.fromStringValue(operation);
		
		long fileSize = -1;
		if (fileToUpdate.length > 2 && !"".equals(fileToUpdate[2].trim())){
			fileSize = Long.parseLong(getValue(fileToUpdate[2]));	
		}

		return new FileToUpdate(fileName, fileSize, fileOperation);
	}

	private static String getValue(String value) {
		return value.trim();
	}
}
