package edu.stanford.irt.laneweb.crm;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTCRMServiceTest {

    private RESTCRMService crmService;

    private RESTService restService;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.crmService = new RESTCRMService(this.uri, this.restService);
    }

    @Test
    public void testSubmitRequest() throws UnsupportedEncodingException {
        expect(this.restService.postURLEncodedString(this.uri, "key=value&id=&ip=ip")).andReturn(200);
        replay(this.restService);
        assertEquals(200, this.crmService.submitRequest(Collections.singletonMap("key", "value"), "ip"));
        verify(this.restService);
    }
}
