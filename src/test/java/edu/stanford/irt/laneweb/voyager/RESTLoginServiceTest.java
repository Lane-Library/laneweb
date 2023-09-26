package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;


public class RESTLoginServiceTest {

    private BasicAuthRESTService restService;

    private RESTLoginService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(BasicAuthRESTService.class);
        this.service = new RESTLoginService(this.uri, this.restService);
    }

    @Test
    public void testLogin() throws RestClientException, URISyntaxException {
        expect(this.restService.getObject(new URI("/login?univid=univid&pid=pid"), Boolean.class))
                .andReturn(Boolean.TRUE);
        replay(this.restService);
        assertTrue(this.service.login("univid", "pid"));
        verify(this.restService);
    }
}
