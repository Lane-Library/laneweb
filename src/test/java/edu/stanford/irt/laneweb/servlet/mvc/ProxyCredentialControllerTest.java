package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class ProxyCredentialControllerTest {

    private ProxyCredentialController controller;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private Ticket ticket = new Ticket("", "");

    @Before
    public void setUp() {
        this.controller = new ProxyCredentialController();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testNullQueryString() throws IOException {
        expect(this.request.getQueryString()).andReturn(null).times(2);
        replay(this.request, this.response);
        try {
            this.controller.proxyRedirect(this.response, this.request, "ditenus", this.ticket);
            fail("should throw IllegalStateException, null query-string");
        } catch (IllegalArgumentException e) {
        }
        verify(this.request, this.response);
    }

    @Test
    public void testRedirectForCredentials() throws IOException {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c").times(2);
        this.response.sendRedirect("http://laneproxy.stanford.edu/login?user=ditenus&ticket=" + this.ticket
                + "&url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.response);
        this.controller.proxyRedirect(this.response, this.request, "ditenus", this.ticket);
        verify(this.request, this.response);
    }
}
