package com.rsicms.rsuite.editors.oxygen.integration.search.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResult;

public class ManagedObjectJsonSerializer extends JsonSerializer<MoResult> {

	@Override
	public void serialize(MoResult mo, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		jgen.writeStartObject();

		jgen.writeStringField("id", mo.getId());
		jgen.writeStringField("dn", mo.getDisplayName());
		jgen.writeStringField("dc", mo.getDateCreated());
		jgen.writeStringField("dm", mo.getDateModified());

		jgen.writeStringField("ln", mo.getLocalName());

		jgen.writeStringField("pid", mo.getParentId());

		jgen.writeEndObject();

	}

}
