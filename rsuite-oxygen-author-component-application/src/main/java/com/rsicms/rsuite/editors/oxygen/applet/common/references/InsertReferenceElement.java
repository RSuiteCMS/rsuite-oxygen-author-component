package com.rsicms.rsuite.editors.oxygen.applet.common.references;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.InsertReferenceHandler;

public class InsertReferenceElement {

	private String elementName;
	
	private String attributeName;
	
	private boolean isImage;
	
	private boolean isCrossReference;
	
	private InsertReferenceHandler referenceHandler;

	private String targetNodeWS;
	
	private String targetNodeListStylesheet;
	
	private boolean isConfRef;
	
	private boolean validateInsertContext = true;
	
	private LookupMethod lookup = LookupMethod.SEARCH;
	
	public InsertReferenceElement(String elementName, String attributeName) {
		this.elementName = elementName;
		this.attributeName = attributeName;		
	}

	private InsertReferenceHandler createDefultReferenceHandler() {
		return new InsertReferenceHandlerDefault();
	}

	public String getElementName() {
		return elementName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

	public boolean isCrossReference() {
		return isCrossReference;
	}

	public void setCrossReference(boolean isCrossReference) {
		this.isCrossReference = isCrossReference;
	}
	
	public void setReferenceHandler(InsertReferenceHandler referenceHandler){
		this.referenceHandler = referenceHandler;
	}

	public InsertReferenceHandler getReferenceHandler() {
		
		if (referenceHandler == null){
			referenceHandler = createDefultReferenceHandler();
		}
		
		return referenceHandler;
	}

	public String getTargetNodeWS() {
		return targetNodeWS;
	}

	public void setTargetNodeWS(String targetNodeWS) {
		this.targetNodeWS = targetNodeWS;
	}

	public String getTargetNodeListStylesheet() {
		return targetNodeListStylesheet;
	}

	public void setTargetNodeListStylesheet(String targetNodeListStylesheet) {
		this.targetNodeListStylesheet = targetNodeListStylesheet;
	}

	public boolean isConfRef() {
		return isConfRef;
	}

	public void setConfRef(boolean isConfRef) {
		this.isConfRef = isConfRef;
		this.isCrossReference = isConfRef;
		this.validateInsertContext = false;
	}

	public boolean isValidateInsertContext() {
		return validateInsertContext;
	}

	public void setValidateInsertContext(boolean validateInsertContext) {
		this.validateInsertContext = validateInsertContext;
	}

	public LookupMethod getLookup() {
		return lookup;
	}

	public void setLookup(LookupMethod lookup) {
		this.lookup = lookup;
	}

}
