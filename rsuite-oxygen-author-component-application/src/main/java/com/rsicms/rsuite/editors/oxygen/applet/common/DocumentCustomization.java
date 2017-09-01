package com.rsicms.rsuite.editors.oxygen.applet.common;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IComponentInitializeHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentHandler;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ILookupFactory;

public class DocumentCustomization implements IDocumentCustomization {

	private IDocumentHandler ioHandler;
	
	private ILookupFactory lookupFactory;
	
	private IComponentInitializeHandler componentInitializer;

	@Override
	public IDocumentHandler getDocumentHandler() {
		return ioHandler;
	}

	@Override
	public ILookupFactory getLookupFactory() {
		return lookupFactory;
	}

	public void setDocumentHandler(IDocumentHandler ioHandler) {
		this.ioHandler = ioHandler;
	}

	public void setLookupFactory(ILookupFactory lookupFactory) {
		this.lookupFactory = lookupFactory;
	}

	@Override
	public IComponentInitializeHandler getIComponentInitializeHandler() {	
		return componentInitializer;
	}

	public void setComponentInitializer(
			IComponentInitializeHandler componentInitializer) {
		this.componentInitializer = componentInitializer;
	}

	
}
