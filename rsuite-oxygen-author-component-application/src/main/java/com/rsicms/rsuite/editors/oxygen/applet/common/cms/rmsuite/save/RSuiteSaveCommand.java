package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICommand;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveProgressListener;

public class RSuiteSaveCommand implements ICommand {

	private ICmsActions cmsAction;

	private IOxygenDocument documentComponent;

	private SaveProgressListener progressListener;

	private Map<String, String> existingRSuiteIDs = new HashMap<String, String>();

	public RSuiteSaveCommand(SaveProgressListener progressListener,
			ICmsActions cmsAction, IOxygenDocument documentComponent) {
		this.progressListener = progressListener;
		this.cmsAction = cmsAction;
		this.documentComponent = documentComponent;
	}

	@Override
	public void execute() throws Exception {

		String document = documentComponent.getSerializedDocument();

		existingRSuiteIDs = RSuiteIdToXpathMapper
				.getRSuiteIDsMapFromDocument(IOUtils.toInputStream(document,
						"utf-8"));

		byte[] bytes = document.getBytes("utf-8");
		progressListener.setSize(bytes.length);

		cmsAction.saveDocument(documentComponent.getDocumentUri(),
				progressListener, bytes);
	}

	@Override
	public void executeAfter() {
		NewRSuiteIdSetter rsuiteIdSetter = new NewRSuiteIdSetter(cmsAction, documentComponent);
		rsuiteIdSetter.addRSuiteIDsForNewSubMOs(existingRSuiteIDs.keySet());
		documentComponent.restoreFrameTitle();

	}

}
