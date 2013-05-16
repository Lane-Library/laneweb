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

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class IPGroupSerializerTest {

    private IPGroup ipgroup;

    private JsonGenerator jgen;

    private SerializerProvider provider;

    private IPGroupSerializer serializer;

    @Before
    public void setUp() throws Exception {
        this.serializer = new IPGroupSerializer();
        this.ipgroup = IPGroup.OTHER;
        this.jgen = createMock(JsonGenerator.class);
        this.provider = createMock(SerializerProvider.class);
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
