package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetLookup;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;

public class RSuiteLookupFactory implements ILookupFactory {

	private IDocumentURI documentUri;

	public RSuiteLookupFactory(IDocumentURI documentUri) {
		this.documentUri = documentUri;
	}

	@Override
	public IReferenceTargetLookup getIReferenceTargetLookup(
			InsertReferenceElement element) {
		return new RsuiteTargetLookupImpl(documentUri);
	}

	@Override
	public ITreeOxygenLookUp getITreeOxygenLookUp(InsertReferenceElement element) {
		String parameters = "";

		if (element.isImage()) {
			parameters = "type=image";
		}

		return new RsuiteOxygenLookUp(documentUri.getCMSUri(), parameters, "0");
	}

}
