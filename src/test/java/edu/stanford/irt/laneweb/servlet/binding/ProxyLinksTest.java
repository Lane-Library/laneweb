package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;


public class ProxyLinksTest {
    
    private ProxyLinks proxyLinks;

    @Before
    public void setUp() throws Exception {
        this.proxyLinks = new ProxyLinks(Collections.<String>singletonList("^171\\.65\\.44\\.\\S+"), Collections.<String>singletonList("^171\\.6[4-7]\\.\\S+"));
    }

    @Test
    public void testGetProxyLinksLPCH() {
        assertTrue(this.proxyLinks.getProxyLinks(IPGroup.LPCH, "127.0.0.1"));
    }

    @Test
    public void testGetProxyLinksSHC() {
        assertTrue(this.proxyLinks.getProxyLinks(IPGroup.SHC, "127.0.0.1"));
    }

    @Test
    public void testGetProxyLinksNull() {
        assertTrue(this.proxyLinks.getProxyLinks(null, "127.0.0.1"));
    }

    @Test
    public void testProxyLinks() {
        assertTrue(this.proxyLinks.proxyLinks("127.0.0.1"));
    }

    @Test
    public void testProxyLinksIsNoProxy() {
        assertFalse(this.proxyLinks.proxyLinks("171.64.0.1"));
    }

    @Test
    public void testProxyLinksIsProxy() {
        assertTrue(this.proxyLinks.proxyLinks("171.65.44.1"));
    }
}
