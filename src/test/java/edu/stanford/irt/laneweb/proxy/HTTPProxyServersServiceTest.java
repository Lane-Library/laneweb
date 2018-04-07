package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.util.ServiceURIResolver;

public class HTTPProxyServersServiceTest {

    private InputStream inputStream;

    private ObjectMapper objectMapper;

    private HTTPProxyServersService service;

    private URI uri;

    private ServiceURIResolver uriResolver;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.uri = getClass().getResource("").toURI();
        this.uriResolver = mock(ServiceURIResolver.class);
        this.service = new HTTPProxyServersService(this.objectMapper, this.uri, this.uriResolver);
        this.inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @Test
    public void testGetHosts() throws IOException, URISyntaxException {
        expect(this.uriResolver.getInputStream(getClass().getResource("").toURI().resolve("proxy/hosts")))
                .andReturn(this.inputStream);
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andReturn(Collections.emptySet());
        replay(this.objectMapper, this.uriResolver);
        assertSame(Collections.emptySet(), this.service.getHosts());
        verify(this.objectMapper, this.uriResolver);
    }

    @Test
    public void testWrite() throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        expect(this.uriResolver.getInputStream(getClass().getResource("").toURI().resolve("proxy/write")))
                .andReturn(this.inputStream);
        replay(this.objectMapper, this.uriResolver);
        this.service.write(baos);
        assertArrayEquals(new byte[0], baos.toByteArray());
        verify(this.objectMapper, this.uriResolver);
    }
}
