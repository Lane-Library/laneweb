package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.BrowzineException;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class BrowzineControllerTest {

    private RESTService restService;

    private HttpServletRequest request;

    private BrowzineController controller;

    private String browzineKey = "myKey";

    private String browzineUrl = "https://public-api.thirdiron.com/public/v1/libraries/1462/articles/{0}?include=journal&access_token={1}";

    private String brozinePath = "/apps/browzine/";

    
    @Before
    public void setUp()  {
        this.restService = mock(RESTService.class);
        this.request = mock(HttpServletRequest.class);
        this.controller = new BrowzineController();
        this.controller.setBrowzineToken(this.browzineKey);
        this.controller.setBrowzineUrl(this.browzineUrl);
        this.controller.setRestService(this.restService);
    }

    @Test
    public void testDOIUrl() throws URISyntaxException {
        expect(this.request.getRequestURL()).andReturn(new StringBuffer(brozinePath + "doi/123456"));
        URI url = new URI(MessageFormat.format(browzineUrl, "doi/123456", browzineKey));
        expect(this.restService.getObject(url, String.class)).andReturn("{data}");
        replay(this.request, this.restService);
        this.controller.getDoi(this.request);
        verify(this.request, this.restService);
    }

    @Test
    public void testPMIDUrl() throws URISyntaxException {
        expect(this.request.getRequestURL()).andReturn(new StringBuffer(brozinePath + "pmid/123456"));
        URI url = new URI(MessageFormat.format(browzineUrl, "pmid/123456", browzineKey));
        expect(this.restService.getObject(url, String.class)).andReturn("{data}");
        replay(this.request, this.restService);
        this.controller.getDoi(this.request);
        verify(this.request, this.restService);
    }
    
    @Test(expected = BrowzineException.class)
    public void testNotFoundValue() throws URISyntaxException {
        expect(this.request.getRequestURL()).andReturn(new StringBuffer(brozinePath + "123456"));
        URI url = new URI(MessageFormat.format(browzineUrl, "123456", browzineKey));
        expect(this.restService.getObject(url, String.class)).andThrow(new RESTException(new IOException("not found on browzine API server")));
        replay(request, this.restService);
        this.controller.getDoi(this.request);
        verify(this.request, this.restService);
    }
    
    @Test(expected = RuntimeException.class)
    public void testOtherException() throws Exception {
        expect(this.request.getRequestURL()).andReturn(new StringBuffer(brozinePath + "123456"));
        URI url = new URI(MessageFormat.format(browzineUrl, "123456", browzineKey));
        expect(this.restService.getObject(url, String.class)).andThrow(new RuntimeException("Should not throw a BrowzineException"));
        replay(request, this.restService);
        this.controller.getDoi(this.request);
        verify(this.request, this.restService);
    }
}
