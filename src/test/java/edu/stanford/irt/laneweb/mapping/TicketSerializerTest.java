package edu.stanford.irt.laneweb.mapping;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketSerializerTest {

    private JsonGenerator jgen;

    private SerializerProvider provider;

    private TicketSerializer serializer;

    private Ticket ticket;

    @Before
    public void setUp() throws Exception {
        this.serializer = new TicketSerializer();
        this.jgen = mock(JsonGenerator.class);
        this.provider = mock(SerializerProvider.class);
        this.ticket = new Ticket("ryanmax@stanford.edu", "ezfoo");
        //this.ticket = mock(Ticket.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(Ticket.class, this.serializer.handledType());
    }

    @Test
    public void testSerializeTicketJsonGeneratorSerializerProvider() throws IOException {
        this.jgen.writeString(this.ticket.toString());
        replay(this.jgen, this.provider);
        this.serializer.serialize(this.ticket, this.jgen, this.provider);
        verify(this.jgen, this.provider);
    }
}
