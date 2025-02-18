package edu.stanford.irt.laneweb.mapping;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class IPGroupSerializerTest {

    private IPGroup ipgroup;

    private JsonGenerator jgen;

    private SerializerProvider provider;

    private IPGroupSerializer serializer;

    @BeforeEach
    public void setUp() throws Exception {
        this.serializer = new IPGroupSerializer();
        this.ipgroup = IPGroup.OTHER;
        this.jgen = mock(JsonGenerator.class);
        this.provider = mock(SerializerProvider.class);
    }

    @Test
    public void testHandledType() {
        assertEquals(IPGroup.class, this.serializer.handledType());
    }

    @Test
    public void testSerializeIPGroupJsonGeneratorSerializerProvider() throws IOException {
        this.jgen.writeString("OTHER");
        replay(this.jgen, this.provider);
        this.serializer.serialize(this.ipgroup, this.jgen, this.provider);
        verify(this.jgen, this.provider);
    }
}
