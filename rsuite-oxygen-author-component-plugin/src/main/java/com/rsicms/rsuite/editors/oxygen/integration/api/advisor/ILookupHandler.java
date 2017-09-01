package com.rsicms.rsuite.editors.oxygen.integration.api.advisor;

import java.util.Map;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public interface ILookupHandler {

	String getMoIconName(ManagedObject mo, String defaultIcon);	
	
	String getNonXmlMoIconName(ManagedObject mo, String defaultIcon);
	
	String getContainerIconName(ContentAssemblyItem caItem, String defaultIcon);
	
	Map<String, String> getAdditionalUserMetaData(ExecutionContext context, ManagedObject mo);
}
