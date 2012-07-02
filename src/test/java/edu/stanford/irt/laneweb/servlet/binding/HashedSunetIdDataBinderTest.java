package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

public class HashedSunetIdDataBinderTest {

    private HashedSunetIdDataBinder dataBinder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private HttpSession session;

    private SunetIdSource sunetIdSource;

    @Before
    public void setUp() throws Exception {
        this.dataBinder = new HashedSunetIdDataBinder();
        this.dataBinder.setSunetidHashKey("key");
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.sunetIdSource = createMock(SunetIdSource.class);
        this.dataBinder.setSunetIdSource(this.sunetIdSource);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.HASHED_SUNETID)).andReturn(null);
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("ditenus");
        this.session.setAttribute(Model.HASHED_SUNETID, "027204c8263a79b02a8cf231399073ed");
        this.model.put(Model.HASHED_SUNETID, "027204c8263a79b02a8cf231399073ed");
        replay(this.request, this.session, this.sunetIdSource);
        this.dataBinder.bind(this.model, this.request);
        assertNotNull(this.model.get(Model.HASHED_SUNETID));
        assertEquals("027204c8263a79b02a8cf231399073ed", this.model.get(Model.HASHED_SUNETID));
        verify(this.request, this.session, this.sunetIdSource);
    }

    @Test
    public void testBindNoSunetId() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.HASHED_SUNETID)).andReturn(null);
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn(null);
        this.model.put(Model.HASHED_SUNETID, "");
        replay(this.request, this.session, this.sunetIdSource);
        this.dataBinder.bind(this.model, this.request);
        assertNotNull(this.model.get(Model.HASHED_SUNETID));
        assertEquals("", this.model.get(Model.HASHED_SUNETID));
        verify(this.request, this.session, this.sunetIdSource);
    }
}
