package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPEquipmentServiceTest {

    private ObjectMapper objectMapper;

    private HTTPEquipmentService service;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.service = new HTTPEquipmentService(this.objectMapper, getClass().getResource("").toURI());
    }

    @Test
    public void testGetRecords() throws IOException {
        replay(this.objectMapper);
        assertEquals(-1, this.service.getRecords(Collections.emptyList()).read());
        verify(this.objectMapper);
    }

    @Test
    public void testGetStatus() throws JsonParseException, JsonMappingException, IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andReturn(Collections.emptyList());
        replay(this.objectMapper);
        assertSame(Collections.emptyList(), this.service.getStatus(""));
        verify(this.objectMapper);
    }
}
