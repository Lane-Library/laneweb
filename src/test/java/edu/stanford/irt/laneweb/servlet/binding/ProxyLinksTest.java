package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.ipgroup.CIDRRange;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class ProxyLinksTest {

    private ProxyLinks proxyLinks;

    @BeforeEach
    public void setUp() throws Exception {
        this.proxyLinks = new ProxyLinks(Collections.singletonList(new CIDRRange("171.65.44.0/24")),
                Collections.singletonList(new CIDRRange("171.64.0.0/14")));
    }

    @Test
    public void testGetProxyLinksLPCH() {
        assertTrue(this.proxyLinks.getProxyLinks(IPGroup.LPCH, "127.0.0.1"));
    }

    @Test
    public void testGetProxyLinksNull() {
        assertTrue(this.proxyLinks.getProxyLinks(null, "127.0.0.1"));
    }

    @Test
    public void testGetProxyLinksSHC() {
        assertTrue(this.proxyLinks.getProxyLinks(IPGroup.SHC, "127.0.0.1"));
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
