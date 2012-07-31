package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

public class ProxyCredentialControllerTest {

    private SunetIdAndTicketDataBinder binder;

    private ProxyCredentialController controller;

    private Model model;

    private HttpServletRequest request;

    private Ticket ticket = new Ticket("", "");

    @Before
    public void setUp() {
        this.binder = createMock(SunetIdAndTicketDataBinder.class);
        this.controller = new ProxyCredentialController(this.binder);
        this.request = createMock(HttpServletRequest.class);
        this.model = createMock(Model.class);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(Collections.<String, Object> emptyMap());
        this.binder.bind(Collections.<String, Object> emptyMap(), this.request);
        replay(this.request, this.binder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.binder, this.model);
    }

    @Test
    public void testProxyRedirect() throws IOException {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.binder);
        assertEquals("redirect:http://laneproxy.stanford.edu/login?user=ditenus&ticket=" + this.ticket
                + "&url=http://www.pubmed.foo/search?q=a&b=c", this.controller.proxyRedirect(this.request, "ditenus", this.ticket));
        verify(this.request, this.binder);
    }

    @Test
    public void testProxyRedirectNullQueryString() throws IOException {
        expect(this.request.getQueryString()).andReturn(null);
        replay(this.request, this.binder);
        try {
            this.controller.proxyRedirect(this.request, "ditenus", this.ticket);
            fail("should throw IllegalStateException, null query-string");
        } catch (IllegalArgumentException e) {
        }
        verify(this.request, this.binder);
    }

    @Test
    public void testProxyRedirectNullSunetid() throws IOException {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.binder);
        assertEquals("redirect:/secure/apps/proxy/credential?url=http://www.pubmed.foo/search?q=a&b=c",
                this.controller.proxyRedirect(this.request, null, this.ticket));
        verify(this.request, this.binder);
    }

    @Test
    public void testProxyRedirectNullTicket() throws IOException {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.binder);
        assertEquals("redirect:/secure/apps/proxy/credential?url=http://www.pubmed.foo/search?q=a&b=c",
                this.controller.proxyRedirect(this.request, "ditenus", null));
        verify(this.request, this.binder);
    }

    @Test
    public void testSecureProxyRedirectNullQueryString() throws IOException {
        expect(this.request.getQueryString()).andReturn(null);
        replay(this.request, this.binder);
        try {
            this.controller.secureProxyRedirect(this.request, "ditenus", this.ticket);
            fail("should throw IllegalStateException, null query-string");
        } catch (IllegalArgumentException e) {
        }
        verify(this.request, this.binder);
    }

    @Test
    public void testSecureRedirect() {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.binder);
        assertEquals("redirect:http://laneproxy.stanford.edu/login?user=ditenus&ticket=" + this.ticket
                + "&url=http://www.pubmed.foo/search?q=a&b=c",
                this.controller.secureProxyRedirect(this.request, "ditenus", this.ticket));
        verify(this.request, this.binder);
    }
}
