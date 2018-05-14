package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTProxyServersServiceTest {

    private RESTService restService;

    private RESTProxyServersService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new RESTProxyServersService(this.uri, this.restService);
    }

    @Test
    public void testGetHosts() throws RestClientException, URISyntaxException {
        expect(this.restService.getObject(new URI("/proxy/hosts"), Set.class)).andReturn(Collections.emptySet());
        replay(this.restService);
        assertSame(Collections.emptySet(), this.service.getHosts());
        verify(this.restService);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWrite() throws IOException {
        this.service.write(null);
    }
}
