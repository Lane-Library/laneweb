package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

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
