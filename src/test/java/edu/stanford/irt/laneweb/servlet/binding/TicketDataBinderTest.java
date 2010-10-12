package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;


public class TicketDataBinderTest {
    
    private TicketDataBinder dataBinder;
    private HttpServletRequest request;
    private Map<String, Object> model;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = new TicketDataBinder();
        this.dataBinder.setEzproxyKey("foo");
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.TICKET)).andReturn(null);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        replay(this.request, this.session);
        this.model.put(Model.SUNETID, "ditenus");
        this.dataBinder.bind(this.model, this.request);
        Ticket ticket = (Ticket) this.model.get(Model.TICKET);
        assertNotNull(ticket);
        assertTrue(ticket.isValid());
        verify(this.request, this.session);
    }
}
