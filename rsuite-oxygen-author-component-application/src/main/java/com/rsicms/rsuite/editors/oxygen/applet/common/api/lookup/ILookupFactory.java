package com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;

public interface ILookupFactory {

	IReferenceTargetLookup getIReferenceTargetLookup(InsertReferenceElement element);
	
	ITreeOxygenLookUp getITreeOxygenLookUp(InsertReferenceElement element);
}
