package com.rsicms.rsuite.editors.oxygen.applet.extension.operations;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;

public class InsertImageOperation extends InsertReferenceOperation {

	@Override
	public String getDescription() {
		return "Insert image";
	}

	@Override
	protected InsertReferenceElement createInsertReferenceElement(
			AuthorAccess authorAccess) {
		InsertReferenceElement imageReference = new InsertReferenceElement(
				"image", "href");
		imageReference.setImage(true);

		return imageReference;
	}
}
