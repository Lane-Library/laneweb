package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;

public class TicketDataBinderTest {

    private TicketDataBinder dataBinder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    private Ticket ticket;

    @BeforeEach
    public void setUp() throws Exception {
        this.dataBinder = new TicketDataBinder("key");
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);
        this.ticket = mock(Ticket.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(this.ticket);
        expect(this.ticket.isValid()).andReturn(true);
        replay(this.request, this.session, this.ticket);
        this.model.put(Model.USER_ID, "id");
        this.dataBinder.bind(this.model, this.request);
        assertSame(this.ticket, this.model.get(Model.TICKET));
        verify(this.request, this.session, this.ticket);
    }

    @Test
    public void testBindTicketInvalid() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(this.ticket);
        expect(this.ticket.isValid()).andReturn(false);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        replay(this.request, this.session, this.ticket);
        this.model.put(Model.USER_ID, "id");
        this.dataBinder.bind(this.model, this.request);
        assertNotNull(this.model.get(Model.TICKET));
        assertNotSame(this.ticket, this.model.get(Model.TICKET));
        verify(this.request, this.session, this.ticket);
    }

    @Test
    public void testBindTicketNull() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(null);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        replay(this.request, this.session, this.ticket);
        this.model.put(Model.USER_ID, "id");
        this.dataBinder.bind(this.model, this.request);
        assertNotNull(this.model.get(Model.TICKET));
        verify(this.request, this.session, this.ticket);
    }

    @Test
    public void testBindUserIdNull() {
        replay(this.request, this.session, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertNull(this.model.get(Model.TICKET));
        verify(this.request, this.session, this.ticket);
    }
}
