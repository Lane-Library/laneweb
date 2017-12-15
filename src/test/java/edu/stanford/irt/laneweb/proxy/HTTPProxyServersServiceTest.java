package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPProxyServersServiceTest {

    private ObjectMapper objectMapper;

    private HTTPProxyServersService service;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.service = new HTTPProxyServersService(this.objectMapper, getClass().getResource("").toURI());
    }

    @Test
    public void testGetHosts() throws JsonParseException, JsonMappingException, IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andReturn(Collections.emptySet());
        replay(this.objectMapper);
        assertSame(Collections.emptySet(), this.service.getHosts());
        verify(this.objectMapper);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        replay(this.objectMapper);
        this.service.write(baos);
        assertArrayEquals(new byte[0], baos.toByteArray());
        verify(this.objectMapper);
    }
}
