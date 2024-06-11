package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class UnividDataBinderTest {

    private UnividDataBinder dataBinder;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() {
        this.dataBinder = new UnividDataBinder();
        this.request = mock(HttpServletRequest.class);
        this.session = mock(HttpSession.class);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER_ID, "user@stanford.edu");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.UNIVID)).andReturn(null);
        expect(this.request.getAttribute("suUnivID")).andReturn("univid");
        this.session.setAttribute(Model.UNIVID, "univid");
        replay(this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertEquals("univid", model.get(Model.UNIVID));
        verify(this.request, this.session);
    }

    @Test
    public void testBindInSession() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER_ID, "user@stanford.edu");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.UNIVID)).andReturn("univid");
        replay(this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertEquals("univid", model.get(Model.UNIVID));
        verify(this.request, this.session);
    }

    @Test
    public void testBindNoAttribute() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER_ID, "user@stanford.edu");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.UNIVID)).andReturn(null);
        expect(this.request.getAttribute("suUnivID")).andReturn(null);
        replay(this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertNull(model.get(Model.UNIVID));
        verify(this.request, this.session);
    }

    @Test
    public void testBindNotStanfordUser() {
        Map<String, Object> model = new HashMap<>();
        model.put(Model.USER_ID, "user@notstanford.edu");
        replay(this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertNull(model.get(Model.UNIVID));
        verify(this.request, this.session);
    }

    @Test
    public void testBindNullUser() {
        Map<String, Object> model = new HashMap<>();
        replay(this.request, this.session);
        this.dataBinder.bind(model, this.request);
        assertNull(model.get(Model.UNIVID));
        verify(this.request, this.session);
    }
}
