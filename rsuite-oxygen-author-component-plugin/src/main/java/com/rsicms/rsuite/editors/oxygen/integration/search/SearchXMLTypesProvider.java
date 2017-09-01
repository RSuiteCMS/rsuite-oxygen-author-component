package com.rsicms.rsuite.editors.oxygen.integration.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.reallysi.rsuite.api.ManagedObjectDefinition;
import com.reallysi.rsuite.api.ManagedObjectDefinitionCatalog;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.SchemaInfo;
import com.reallysi.rsuite.service.SchemaService;

public class SearchXMLTypesProvider {

	private SchemaService schemaSvc;
	
	public SearchXMLTypesProvider(SchemaService schemaSvc) {
		super();
		this.schemaSvc = schemaSvc;
	}

	public List<SearchXMLType> provideXMLTypes() throws RSuiteException{
		
		List<SearchXMLType> xmlTypes = new ArrayList<SearchXMLType>();
		
		Collection<SchemaInfo> schemas = schemaSvc.getSchemaInfoValues();
		for (SchemaInfo schema : schemas) {
	        if (schema.getSystemLocation() != null &&
	        		schema.getSystemLocation().equals("dataTypeOptionValues.xsd")) {
	        	continue;
	        }
			ManagedObjectDefinitionCatalog moDefs = 
					schemaSvc.getManagedObjectDefinitionCatalog(schema.getSchemaId());
			for (ManagedObjectDefinition def : moDefs.getManagedObjectDefinitions().values()) {
				xmlTypes.add(new SearchXMLType(def.getName(), def.getNamespaceUri()));
			}
		}
		
		return xmlTypes;
	}
}
