package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketSerializer extends JsonSerializer<Ticket> {

    @Override
    public Class<Ticket> handledType() {
        return Ticket.class;
    }

    @Override
    public void serialize(final Ticket value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
