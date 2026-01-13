package edu.stanford.irt.laneweb.mapping;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

public class IPGroupSerializerTest {

    private IPGroup ipgroup;

    private JsonGenerator jgen;

    private SerializationContext context;

    private IPGroupSerializer serializer;

    @BeforeEach
    public void setUp() throws Exception {
        this.serializer = new IPGroupSerializer();
        this.ipgroup = IPGroup.OTHER;
        this.jgen = mock(JsonGenerator.class);
        this.context = mock(SerializationContext.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(IPGroup.class, this.serializer.handledType());
    }

    @Test
    public void testSerializeIPGroupJsonGeneratorSerializerProvider() throws IOException {
        expect(this.jgen.writeString("OTHER")).andReturn(this.jgen);
        replay(this.jgen, this.context);
        this.serializer.serialize(this.ipgroup, this.jgen, this.context);
        verify(this.jgen, this.context);
    }
}
