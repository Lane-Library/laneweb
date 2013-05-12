package edu.stanford.irt.laneweb.mapping;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketSerializerTest {

    private JsonGenerator jgen;

    private SerializerProvider provider;

    private TicketSerializer serializer;

    private Ticket ticket;

    @Before
    public void setUp() throws Exception {
        this.serializer = new TicketSerializer();
        this.jgen = createMock(JsonGenerator.class);
        this.provider = createMock(SerializerProvider.class);
        this.ticket = createMock(Ticket.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(Ticket.class, this.serializer.handledType());
    }

    @Test
    public void testSerializeTicketJsonGeneratorSerializerProvider() throws IOException {
        this.jgen.writeString(this.ticket.toString());
        replay(this.ticket, this.jgen, this.provider);
        this.serializer.serialize(this.ticket, this.jgen, this.provider);
        verify(this.ticket, this.jgen, this.provider);
    }
}
