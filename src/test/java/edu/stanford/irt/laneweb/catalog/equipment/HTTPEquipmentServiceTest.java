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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.util.ServiceURIResolver;

public class HTTPEquipmentServiceTest {

    private ObjectMapper objectMapper;

    private HTTPEquipmentService service;

    private URI uri;

    private ServiceURIResolver uriResolver;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.uri = getClass().getResource("").toURI();
        this.uriResolver = mock(ServiceURIResolver.class);
        this.service = new HTTPEquipmentService(this.objectMapper, this.uri, this.uriResolver);
    }

    @Test
    public void testGetRecords() throws IOException {
        expect(this.uriResolver.getInputStream(this.uri.resolve("equipment/records")))
                .andReturn(getClass().getResourceAsStream(("equipment/records")));
        replay(this.objectMapper, this.uriResolver);
        assertEquals(-1, this.service.getRecords(Collections.emptyList()).read());
        verify(this.objectMapper, this.uriResolver);
    }

    @Test
    public void testGetStatus() throws IOException {
        expect(this.uriResolver.getInputStream(this.uri.resolve("equipment/status?idList=")))
                .andReturn(getClass().getResourceAsStream(("equipment/status")));
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andReturn(Collections.emptyList());
        replay(this.objectMapper, this.uriResolver);
        assertSame(Collections.emptyList(), this.service.getStatus(""));
        verify(this.objectMapper, this.uriResolver);
    }
}
