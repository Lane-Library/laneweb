package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractProxyLinkTransformerTest {

    private Map<String, Object> model;

    private Parameters parameters;

    private AbstractProxyLinkTransformer transformer;

    private ProxyHostManager proxyHostManager;

    @Before
    public void setUp() throws Exception {
        this.proxyHostManager = createMock(ProxyHostManager.class);
        this.transformer = new AbstractProxyLinkTransformer(this.proxyHostManager) {
        };
        this.model = new HashMap<String, Object>();
        this.parameters = createMock(Parameters.class);
    }

    @Test
    public void testCreateProxyLink() {
        replay(this.parameters);
        assertEquals("null/secure/apps/proxy/credential?url=foo", this.transformer.createProxyLink("foo"));
        verify(this.parameters);
    }

    @Test
    public void testSHCIP() {
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        this.transformer.setModel(this.model);
        assertEquals("http://laneproxy.stanford.edu/login?url=foo", this.transformer.createProxyLink("foo"));
    }

    @Test
    public void testLPCHIP() {
        this.model.put(Model.IPGROUP, IPGroup.LPCH);
        this.transformer.setModel(this.model);
        assertEquals("http://laneproxy.stanford.edu/login?url=foo", this.transformer.createProxyLink("foo"));
    }
    
    @Test
    public void testSunetidAndTicket() {
        this.model.put(Model.BASE_PATH, "");
        this.model.put(Model.SUNETID, "sunetid");
        Ticket ticket = new Ticket("", "");
        this.model.put(Model.TICKET, ticket);
        this.transformer.setModel(this.model);
        assertEquals("http://laneproxy.stanford.edu/login?user=sunetid&ticket=" + ticket + "&url=foo", this.transformer.createProxyLink("foo"));
        
    }
    
    @Test
    public void testIsProxyableLink() {
        expect(this.proxyHostManager.isProxyableLink("link")).andReturn(true);
        replay(this.proxyHostManager);
        assertTrue(this.transformer.isProxyableLink("link"));
        verify(this.proxyHostManager);
    }
}
