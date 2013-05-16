package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
