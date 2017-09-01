package com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetLookup;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;

public class LookupFactoryRegister {

	private ILookupFactory lookupFactory;

	public LookupFactoryRegister(ILookupFactory lookupFactory) {
		this.lookupFactory = lookupFactory;
	}

	public IReferenceTargetLookup getIReferenceTargetLookup(
			InsertReferenceElement element) {
		if (lookupFactory == null) {
			throw new RuntimeException(
					"The lookup factory register is not initialized");
		}
		return lookupFactory.getIReferenceTargetLookup(element);
	}

	public  IReferenceTargetLookup getIReferenceTargetLookup() {
		return getIReferenceTargetLookup(null);
	}

	public ITreeOxygenLookUp getITreeOxygenLookUp(
			InsertReferenceElement element) {
		if (lookupFactory == null) {
			throw new RuntimeException(
					"The lookup factory register is not initialized");
		}
		return lookupFactory.getITreeOxygenLookUp(element);
	}

}
