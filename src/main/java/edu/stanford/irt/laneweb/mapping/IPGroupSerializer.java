package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class IPGroupSerializer extends JsonSerializer<IPGroup> {

    @Override
    public Class<IPGroup> handledType() {
        return IPGroup.class;
    }

    @Override
    public void serialize(final IPGroup value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
