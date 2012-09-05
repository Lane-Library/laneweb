package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
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

public class HashedSunetIdDataBinderTest {

    private HashedSunetIdDataBinder dataBinder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = new HashedSunetIdDataBinder();
        this.dataBinder.setSunetidHashKey("key");
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBind() {
        this.model.put(Model.SUNETID, "ditenus");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.AUTH)).andReturn(null);
        this.session.setAttribute(Model.AUTH, "027204c8263a79b02a8cf231399073ed");
        this.model.put(Model.AUTH, "027204c8263a79b02a8cf231399073ed");
        replay(this.request, this.session);
        this.dataBinder.bind(this.model, this.request);
        assertNotNull(this.model.get(Model.AUTH));
        assertEquals("027204c8263a79b02a8cf231399073ed", this.model.get(Model.AUTH));
        verify(this.request, this.session);
    }

    @Test
    public void testBindNoSunetId() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.AUTH)).andReturn(null);
        replay(this.request, this.session);
        this.dataBinder.bind(this.model, this.request);
        assertNull(this.model.get(Model.AUTH));
        verify(this.request, this.session);
    }
    
    @Test
    public void testHashedInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.AUTH)).andReturn(Model.AUTH);
        replay(this.request, this.session);
        this.dataBinder.bind(this.model, this.request);
        assertEquals(Model.AUTH, this.model.get(Model.AUTH));
        verify(this.request, this.session);
    }
}
