package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
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
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTProxyServersServiceTest {

    private InputStream inputStream;

    private RESTService restService;

    private RESTProxyServersService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new RESTProxyServersService(this.uri, this.restService);
        this.inputStream = new ByteArrayInputStream(new byte[] { 60 });
    }

    @Test
    public void testGetHosts() throws RestClientException, URISyntaxException {
        expect(this.restService.getObject(new URI("/proxy/hosts"), Set.class)).andReturn(Collections.emptySet());
        replay(this.restService);
        assertSame(Collections.emptySet(), this.service.getHosts());
        verify(this.restService);
    }

    @Test
    public void testWrite() throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        expect(this.restService.getInputStream(new URI("/proxy/write"))).andReturn(this.inputStream);
        replay(this.restService);
        this.service.write(baos);
        assertArrayEquals(new byte[] { 60 }, baos.toByteArray());
        verify(this.restService);
    }
}
