package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class RemoteProxyIPDataBinderTest {

    private RemoteProxyIPDataBinder dataBinder;

    private Map<String, Object> model;

    private ProxyLinks proxyLinks;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() {
        this.dataBinder = new RemoteProxyIPDataBinder();
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.dataBinder.setProxyLinks(this.proxyLinks);
    }

    @Test
    public void testBindDifferentIP() {
        expect(this.request.getRemoteAddr()).andReturn("171.65.1.202");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("X-FORWARDED-FOR")).andReturn(null);
        expect(this.session.getAttribute(Model.REMOTE_ADDR)).andReturn("97.126.62.121");
        expect(this.session.getAttribute(Model.IPGROUP)).andReturn(IPGroup.OTHER);
        expect(this.request.getParameter(Model.PROXY_LINKS)).andReturn(null);
        expect(this.proxyLinks.getProxyLinks(IPGroup.SOM_OTHER, "171.65.1.202")).andReturn(Boolean.FALSE);
        this.session.setAttribute(Model.REMOTE_ADDR, "171.65.1.202");
        this.session.setAttribute(Model.IPGROUP, IPGroup.SOM_OTHER);
        this.session.setAttribute(Model.PROXY_LINKS, Boolean.FALSE);
        replay(this.request, this.session, this.proxyLinks);
        this.dataBinder.bind(this.model, this.request);
        assertFalse((Boolean) this.model.get(Model.PROXY_LINKS));
        assertEquals(IPGroup.SOM_OTHER, this.model.get(Model.IPGROUP));
        assertEquals("171.65.1.202", this.model.get(Model.REMOTE_ADDR));
        verify(this.request, this.session, this.proxyLinks);
    }

    @Test
    public void testBindNoPreviousIPOther() {
        expect(this.request.getRemoteAddr()).andReturn("97.126.62.121");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("X-FORWARDED-FOR")).andReturn(null);
        expect(this.request.getParameter(Model.PROXY_LINKS)).andReturn(null);
        expect(this.session.getAttribute(Model.REMOTE_ADDR)).andReturn(null);
        expect(this.session.getAttribute(Model.IPGROUP)).andReturn(null);
        this.session.setAttribute(Model.REMOTE_ADDR, "97.126.62.121");
        this.session.setAttribute(Model.PROXY_LINKS, Boolean.TRUE);
        this.session.setAttribute(Model.IPGROUP, IPGroup.OTHER);
        expect(this.proxyLinks.getProxyLinks(IPGroup.OTHER, "97.126.62.121")).andReturn(Boolean.TRUE);
        replay(this.request, this.session, this.proxyLinks);
        this.dataBinder.bind(this.model, this.request);
        assertTrue((Boolean) this.model.get(Model.PROXY_LINKS));
        assertEquals(IPGroup.OTHER, this.model.get(Model.IPGROUP));
        assertEquals("97.126.62.121", this.model.get(Model.REMOTE_ADDR));
        verify(this.request, this.session, this.proxyLinks);
    }

    @Test
    public void testBindSameIP() {
        expect(this.request.getRemoteAddr()).andReturn("97.126.62.121");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("X-FORWARDED-FOR")).andReturn(null);
        expect(this.session.getAttribute(Model.REMOTE_ADDR)).andReturn("97.126.62.121");
        expect(this.session.getAttribute(Model.IPGROUP)).andReturn(IPGroup.OTHER);
        expect(this.request.getParameter(Model.PROXY_LINKS)).andReturn(null);
        expect(this.session.getAttribute(Model.PROXY_LINKS)).andReturn(Boolean.TRUE);
        replay(this.request, this.session, this.proxyLinks);
        this.dataBinder.bind(this.model, this.request);
        assertTrue((Boolean) this.model.get(Model.PROXY_LINKS));
        assertEquals(IPGroup.OTHER, this.model.get(Model.IPGROUP));
        assertEquals("97.126.62.121", this.model.get(Model.REMOTE_ADDR));
        verify(this.request, this.session, this.proxyLinks);
    }
}
