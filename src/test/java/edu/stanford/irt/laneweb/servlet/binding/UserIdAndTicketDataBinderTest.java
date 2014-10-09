package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;
import edu.stanford.irt.laneweb.servlet.UserIdSource;

public class UserIdAndTicketDataBinderTest {

    private UserIdAndTicketDataBinder dataBinder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    private UserIdSource userIdSource;

    private Ticket ticket;

    @Before
    public void setUp() throws Exception {
        this.userIdSource = createMock(UserIdSource.class);
        this.dataBinder = new UserIdAndTicketDataBinder(this.userIdSource, "foo", "key");
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.ticket = createMock(Ticket.class);
    }

    @Test
    public void testBind() {
        expect(this.userIdSource.getUserId(this.request)).andReturn("ditenus");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(this.ticket);
        expect(this.ticket.isValid()).andReturn(true);
        expect(this.session.getAttribute(Model.AUTH)).andReturn("auth");
        replay(this.request, this.session, this.userIdSource, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("ditenus", this.model.get(Model.USER_ID));
        assertEquals(this.ticket, this.model.get(Model.TICKET));
        assertEquals("auth", this.model.get(Model.AUTH));
        verify(this.request, this.session, this.userIdSource, this.ticket);
    }

    @Test
    public void testBindAuthNull() {
        expect(this.userIdSource.getUserId(this.request)).andReturn("ditenus");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(this.ticket);
        expect(this.ticket.isValid()).andReturn(true);
        expect(this.session.getAttribute(Model.AUTH)).andReturn(null);
        this.session.setAttribute(Model.AUTH, "027204c8263a79b02a8cf231399073ed");
        replay(this.request, this.session, this.userIdSource, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("ditenus", this.model.get(Model.USER_ID));
        assertEquals(this.ticket, this.model.get(Model.TICKET));
        assertEquals("027204c8263a79b02a8cf231399073ed", this.model.get(Model.AUTH));
        verify(this.request, this.session, this.userIdSource, this.ticket);
    }

    @Test
    public void testBindUserIdNull() {
        expect(this.userIdSource.getUserId(this.request)).andReturn(null);
        replay(this.request, this.session, this.userIdSource, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertNull(this.model.get(Model.USER_ID));
        assertNull(this.model.get(Model.TICKET));
        assertNull(this.model.get(Model.AUTH));
        verify(this.request, this.session, this.userIdSource, this.ticket);
    }

    @Test
    public void testBindTicketInvalid() {
        expect(this.userIdSource.getUserId(this.request)).andReturn("ditenus");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(this.ticket);
        expect(this.ticket.isValid()).andReturn(false);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        expect(this.session.getAttribute(Model.AUTH)).andReturn("auth");
        replay(this.request, this.session, this.userIdSource, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("ditenus", this.model.get(Model.USER_ID));
        assertNotNull(this.model.get(Model.TICKET));
        assertEquals("auth", this.model.get(Model.AUTH));
        verify(this.request, this.session, this.userIdSource, this.ticket);
    }

    @Test
    public void testBindTicketNull() {
        expect(this.userIdSource.getUserId(this.request)).andReturn("ditenus");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(null);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        expect(this.session.getAttribute(Model.AUTH)).andReturn("auth");
        replay(this.request, this.session, this.userIdSource, this.ticket);
        this.dataBinder.bind(this.model, this.request);
        assertEquals("ditenus", this.model.get(Model.USER_ID));
        assertNotNull(this.model.get(Model.TICKET));
        assertEquals("auth", this.model.get(Model.AUTH));
        verify(this.request, this.session, this.userIdSource, this.ticket);
    }
}
