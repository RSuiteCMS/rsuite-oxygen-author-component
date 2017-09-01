package com.rsicms.rsuite.editors.oxygen.applet.common;

import java.util.HashMap;
import java.util.Map;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.component.ditamap.DITAMapTreeComponentProvider;

public class OxygenComponentRegister {

	private static Map<String, OxygenEditorContext> contextMap = new HashMap<String, OxygenEditorContext>();
	
	private static Map<String, OxygenMapContext> contextDitaMap = new HashMap<String, OxygenMapContext>();
	
	public static void registerOxygenEditorContext(AuthorAccess authorAccess, OxygenEditorContext context){
		contextMap.put(authorAccess.toString(), context);
	}
	
	
	public static OxygenMapContext getRegisterOxygenEditorContext(DITAMapTreeComponentProvider mapComponent){
		return contextDitaMap.get(mapComponent.toString());
	}
	
	
	public static void registerOxygenEditorContext(DITAMapTreeComponentProvider mapComponent, OxygenMapContext context){
		contextDitaMap.put(mapComponent.toString(), context);
	}
	
	
	public static OxygenEditorContext getRegisterOxygenEditorContext(AuthorAccess authorAccess){
		return contextMap.get(authorAccess.toString());
	}
}
