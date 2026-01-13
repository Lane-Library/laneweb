package edu.stanford.irt.laneweb.mapping;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketSerializer extends ValueSerializer<Ticket> {

    @Override
    public Class<Ticket> handledType() {
        return Ticket.class;
    }

    @Override
    public void serialize(final Ticket value, final JsonGenerator jgen, final SerializationContext context)
            throws JacksonException {
        jgen.writeString(value.toString());
    }
}
