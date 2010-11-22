package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.proxy.Ticket;


public class SunetIdAndTicketDataBinderTest {
    
    private SunetIdAndTicketDataBinder dataBinder;
    private HttpServletRequest request;
    private Map<String, Object> model;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = new SunetIdAndTicketDataBinder();
        this.dataBinder.setEzproxyKey("foo");
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.TICKET)).andReturn(null);
        this.session.setAttribute(eq(Model.TICKET), isA(Ticket.class));
        replay(this.request, this.session);
        this.model.put(Model.SUNETID, "ditenus");
        this.dataBinder.bind(this.model, this.request);
        Ticket ticket = (Ticket) this.model.get(Model.TICKET);
        assertNotNull(ticket);
        assertTrue(ticket.isValid());
        assertEquals("ditenus", this.model.get(Model.SUNETID));
        verify(this.request, this.session);
    }
}
