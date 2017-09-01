package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers;


public class SchemaEntry {

	private String schemaId;
	
	private byte[] content;

	private int size = 0;
	
	public SchemaEntry(String schemaId, byte[] content) {
		super();
		this.schemaId = schemaId;
		this.content = content;
		if (content != null){
			size = content.length;
		}		
	}

	public String getSchemaId() {
		return schemaId;
	}

	public byte[] getContent() {
		return content;
	}

	public int getSize() {
		return size;
	}
	
	
}
