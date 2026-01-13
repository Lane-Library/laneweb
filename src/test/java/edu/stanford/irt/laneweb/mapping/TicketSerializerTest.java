package edu.stanford.irt.laneweb.mapping;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketSerializerTest {

    private JsonGenerator jgen;

    private SerializationContext context;

    private TicketSerializer serializer;

    private Ticket ticket;

    @BeforeEach
    public void setUp() throws Exception {
        this.serializer = new TicketSerializer();
        this.jgen = mock(JsonGenerator.class);
        this.context = mock(SerializationContext.class);
        this.ticket = new Ticket("ryanmax@stanford.edu", "ezfoo");
        // this.ticket = mock(Ticket.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(Ticket.class, this.serializer.handledType());
    }

    @Test
    public void testSerializeTicketJsonGeneratorSerializerProvider() throws IOException {
        expect(this.jgen.writeString(this.ticket.toString())).andReturn(this.jgen);
        replay(this.jgen, this.context);
        this.serializer.serialize(this.ticket, this.jgen, this.context);
        verify(this.jgen, this.context);
    }
}
