package com.rsicms.rsuite.editors.oxygen.launcher.update;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.Base64Coder;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileOperation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdate;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdateParser;

public class FileToUpdateParserTest {

	@Test
	public void shuldParseBase64EncodedFileUpdateListWithOneFileToAdd() {
		
		String testCmsResponse = "cnN1aXRlLW8yLWZyYW1ld29yay1vbml4LXBsdWdpbi9veHlnZW4vc2FtcGxlLnR4dD8wMjFkNzNlOS0yODA5LTQxZDctYjk2My02MjA5ODAwYWRlYTUgfCBhIHwgMjMzMjQ=";
		
		//System.out.println(Base64Coder.decodeString(testCmsResponse));
		
		List<FileToUpdate> filesToUpdate = FileToUpdateParser.parseBase64EncodedFilesToUpdate(testCmsResponse);
		
		assertEquals(1, filesToUpdate.size());
		
		FileToUpdate fileToUpdate = filesToUpdate.get(0); 
		assertEquals("sample.txt", fileToUpdate.getFileName());
		assertEquals(23324, fileToUpdate.getSize());
		assertEquals(FileOperation.ADD, fileToUpdate.getFileOperation());
	}
	
	@Test
	public void shuldParseBase64EncodedFileUpdateListWithOneFileToUpdateSha1() {
		
		String testCmsResponse = "c2FtcGxlLnR4dCB8IHUgfCAyMzMyNCA=";
		
		//System.out.println(Base64Coder.decodeString(testCmsResponse));
		
		List<FileToUpdate> filesToUpdate = FileToUpdateParser.parseBase64EncodedFilesToUpdate(testCmsResponse);
		
		assertEquals(1, filesToUpdate.size());
		
		FileToUpdate fileToUpdate = filesToUpdate.get(0); 
		assertEquals("sample.txt", fileToUpdate.getFileName());
		assertEquals(23324, fileToUpdate.getSize());
		assertEquals(FileOperation.UPDATE, fileToUpdate.getFileOperation());
	}

	
	@Test
	public void shuldParseBase64EncodedFileUpdateListWithTwoFiles() {
		
		String testCmsResponse = "c2FtcGxlLnR4dCB8IHUgfCAyMzMyNCANCm90aGVyLnR4dCB8IGEgfCAxMjMzMjQg";
		
		System.out.println(Base64Coder.decodeString(testCmsResponse));
		
		List<FileToUpdate> filesToUpdate = FileToUpdateParser.parseBase64EncodedFilesToUpdate(testCmsResponse);
		
		assertEquals(2, filesToUpdate.size());
		
		FileToUpdate fileToUpdate = filesToUpdate.get(0); 
		assertEquals("sample.txt", fileToUpdate.getFileName());
		assertEquals(23324, fileToUpdate.getSize());
		assertEquals(FileOperation.UPDATE, fileToUpdate.getFileOperation());
		
		fileToUpdate = filesToUpdate.get(1); 
		assertEquals("other.txt", fileToUpdate.getFileName());
		assertEquals(123324, fileToUpdate.getSize());
		assertEquals(FileOperation.ADD, fileToUpdate.getFileOperation());
	}
}
