package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AttrValue;
import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenDocument;

public class NewRSuiteIdSetter {

	private ICmsActions cmsAction;

	private IOxygenDocument documentComponent;

	private Logger logger = Logger.getLogger(getClass());

	public NewRSuiteIdSetter(ICmsActions cmsAction,
			IOxygenDocument documentComponent) {
		this.cmsAction = cmsAction;
		this.documentComponent = documentComponent;
	}

	public void addRSuiteIDsForNewSubMOs(Set<String> existingRSuiteIDs) {
		try {
			String document = cmsAction.loadDocument(
					documentComponent.getDocumentCustomization(),
					documentComponent.getDocumentUri());

			AuthorDocumentController documentController = documentComponent
					.getOxygenDocumentController();
			Map<String, String> newRSuiteIDsMap = getRSuiteIDsMapFromDocument(IOUtils
					.toInputStream(document, "utf-8"));

			Set<String> newRSuiteIDs = newRSuiteIDsMap.keySet();
			newRSuiteIDs.removeAll(existingRSuiteIDs);

			for (String rsuiteIdToAdd : newRSuiteIDs) {
				String xpath = newRSuiteIDsMap.get(rsuiteIdToAdd);
				AuthorNode[] nodesByXPath = documentController
						.findNodesByXPath(xpath, false, false, false);
				if (nodesByXPath.length > 0
						&& nodesByXPath[0].getType() == AuthorNode.NODE_TYPE_ELEMENT) {
					AuthorElement element = (AuthorElement) nodesByXPath[0];
					element.setAttribute("r:rsuiteId", new AttrValue(
							rsuiteIdToAdd));
				}
			}

		} catch (AuthorOperationException | IOException
				| OxygenIntegrationException e) {
			OxygenUtils.handleException(logger, e);
		}
	}

	private Map<String, String> getRSuiteIDsMapFromDocument(
			InputStream inputStream) throws OxygenIntegrationException {
		RSuiteIdToXpathMapper rsuiteIdMapper = new RSuiteIdToXpathMapper();

		RSuiteDocumentParser documentParser = new RSuiteDocumentParser(
				rsuiteIdMapper);
		documentParser.parseRSuiteDocument(inputStream);

		return rsuiteIdMapper.getRsuiteIDsToXpathMap();
	}
}
