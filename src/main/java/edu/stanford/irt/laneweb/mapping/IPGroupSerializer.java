package edu.stanford.irt.laneweb.mapping;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class IPGroupSerializer extends ValueSerializer<IPGroup> {

    @Override
    public Class<IPGroup> handledType() {
        return IPGroup.class;
    }

    @Override
    public void serialize(final IPGroup value, final JsonGenerator jgen, final SerializationContext context)
            throws JacksonException {
        jgen.writeString(value.toString());
    }
}
