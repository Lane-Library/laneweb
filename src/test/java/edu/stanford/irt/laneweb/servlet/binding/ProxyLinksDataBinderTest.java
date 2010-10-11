package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.*;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.ProxyLinks;


public class ProxyLinksDataBinderTest {
    
    private ProxyLinksDataBinder dataBinder;
    private Map<String, Object> model;
    private HttpServletRequest request;
    private HttpSession session;
    private ProxyLinks proxyLinks;
    
    @Before
    public void setUp() {
        this.dataBinder = new ProxyLinksDataBinder();
        this.dataBinder.setModelKey(Model.PROXY_LINKS);
        this.dataBinder.setParameterName(Model.PROXY_LINKS);
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.dataBinder.setProxyLinks(this.proxyLinks);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.request.getParameter(Model.PROXY_LINKS)).andReturn(null);
        expect(this.session.getAttribute(Model.PROXY_LINKS)).andReturn(null);
        this.session.setAttribute(Model.PROXY_LINKS, Boolean.TRUE);
        expect(this.proxyLinks.getProxyLinks(this.request, this.session, IPGroup.OTHER, "174.31.153.109")).andReturn(Boolean.TRUE);
        replay(this.request, this.session, this.proxyLinks);
        this.model.put(Model.IPGROUP, IPGroup.OTHER);
        this.model.put(Model.REMOTE_ADDR, "174.31.153.109");
        this.dataBinder.bind(this.model, this.request);
        assertTrue((Boolean)this.model.get(Model.PROXY_LINKS));
        verify(this.request, this.session, this.proxyLinks);
    }
}
