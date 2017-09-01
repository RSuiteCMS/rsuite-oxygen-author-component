package com.rsicms.rsuite.editors.oxygen.launcher.update;

import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileInformation;
import com.rsicms.rsuite.editors.oxygen.launcher.update.file.FileToUpdate;

public interface OxygenApplicationCmsUpdater {

	List<FileToUpdate> obtainFilesToUpdate(List<FileInformation> localFiles) throws OxygenApplicationException;

	String createUrlForFile(FileToUpdate fileToUpdate);

	
}
