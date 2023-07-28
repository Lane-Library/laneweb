package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.binding.TicketDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class ProxyCredentialControllerTest {

    private ProxyCredentialController controller;

    private Model model;

    private HttpServletRequest request;

    private Ticket ticket = new Ticket("", "");

    private TicketDataBinder ticketBinder;

    private UserDataBinder userBinder;

    @Before
    public void setUp() {
        this.ticketBinder = mock(TicketDataBinder.class);
        this.userBinder = mock(UserDataBinder.class);
        this.controller = new ProxyCredentialController(this.ticketBinder, this.userBinder);
        this.request = mock(HttpServletRequest.class);
        this.model = mock(Model.class);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(Collections.emptyMap()).times(2);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID)).andReturn(true);
        this.ticketBinder.bind(Collections.emptyMap(), this.request);
        this.userBinder.bind(Collections.emptyMap(), this.request);
        replay(this.request, this.ticketBinder, this.userBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.ticketBinder, this.userBinder, this.model);
    }

    @Test
    public void testBindNoUserId() {
        expect(this.model.asMap()).andReturn(Collections.emptyMap()).times(2);
        expect(this.model.containsAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID)).andReturn(false);
        expect(this.model.addAttribute(edu.stanford.irt.laneweb.model.Model.USER_ID, null)).andReturn(this.model);
        expect(this.model.addAttribute(edu.stanford.irt.laneweb.model.Model.TICKET, null)).andReturn(this.model);
        this.ticketBinder.bind(Collections.emptyMap(), this.request);
        this.userBinder.bind(Collections.emptyMap(), this.request);
        replay(this.request, this.ticketBinder, this.userBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.request, this.ticketBinder, this.userBinder, this.model);
    }

    @Test
    public void testProxyRedirect() {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.ticketBinder, this.userBinder);
        RedirectView view = (RedirectView) this.controller.proxyRedirect(this.request, null, "ditenus", this.ticket);
        assertEquals("https://login.laneproxy.stanford.edu/login?user=ditenus&ticket=" + this.ticket
                + "&url=http://www.pubmed.foo/search?q=a&b=c", view.getUrl());
        verify(this.request, this.ticketBinder, this.userBinder);
    }

    @Test
    public void testProxyRedirectNullQueryString() {
        expect(this.request.getQueryString()).andReturn(null);
        replay(this.request, this.ticketBinder);
        try {
            this.controller.proxyRedirect(this.request, null, "ditenus", this.ticket);
            fail("should throw IllegalStateException, null query-string");
        } catch (IllegalArgumentException e) {
        }
        verify(this.request, this.ticketBinder);
    }

    @Test
    public void testProxyRedirectNullTicket() {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.ticketBinder);
        RedirectView view = (RedirectView) this.controller.proxyRedirect(this.request, null, "ditenus", null);
        assertEquals("/secure/apps/proxy/credential?url=http://www.pubmed.foo/search?q=a&b=c", view.getUrl());
        verify(this.request, this.ticketBinder);
    }

    @Test
    public void testProxyRedirectNullUserId() {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.ticketBinder);
        RedirectView view = (RedirectView) this.controller.proxyRedirect(this.request, null, null, this.ticket);
        assertEquals("/secure/apps/proxy/credential?url=http://www.pubmed.foo/search?q=a&b=c", view.getUrl());
        verify(this.request, this.ticketBinder);
    }

    @Test
    public void testSecureProxyRedirectNullQueryString() {
        expect(this.request.getQueryString()).andReturn(null);
        replay(this.request, this.ticketBinder);
        try {
            this.controller.secureProxyRedirect(this.request, null, "ditenus", this.ticket);
            fail("should throw IllegalStateException, null query-string");
        } catch (IllegalArgumentException e) {
        }
        verify(this.request, this.ticketBinder);
    }

    @Test
    public void testSecureRedirect() {
        expect(this.request.getQueryString()).andReturn("url=http://www.pubmed.foo/search?q=a&b=c");
        replay(this.request, this.ticketBinder);
        RedirectView view = (RedirectView) this.controller.secureProxyRedirect(this.request, null, "ditenus",
                this.ticket);
        assertEquals("https://login.laneproxy.stanford.edu/login?user=ditenus&ticket=" + this.ticket
                + "&url=http://www.pubmed.foo/search?q=a&b=c", view.getUrl());
        verify(this.request, this.ticketBinder);
    }
}
