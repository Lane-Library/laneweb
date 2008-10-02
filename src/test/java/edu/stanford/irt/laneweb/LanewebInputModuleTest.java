/*
 * Created on Aug 22, 2005 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb;

import junit.framework.TestCase;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

public class LanewebInputModuleTest extends TestCase {

    LanewebInputModule module;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.module = new LanewebInputModule();
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        Configuration config = builder.build(
                "src/main/webapp/WEB-INF/cocoon.xconf").getChild(
                "input-modules").getChildren()[0];
        this.module.configure(config);
    }

    public void testProxyMyIP() throws ConfigurationException {
        assertFalse((this.module.proxyLinks("171.65.28.158")));
    }

    public void testAFewIPs() throws ConfigurationException {

        // ITS VPN
        assertTrue((this.module.proxyLinks("171.66.16.0")));
        assertFalse((this.module.proxyLinks("171.66.15.255")));
        assertTrue((this.module.proxyLinks("171.66.19.230")));
        assertTrue((this.module.proxyLinks("171.66.20.55")));
        assertTrue((this.module.proxyLinks("171.66.22.10")));
        assertTrue((this.module.proxyLinks("171.66.30.55")));
        assertTrue((this.module.proxyLinks("171.66.31.255")));
        assertFalse((this.module.proxyLinks("171.66.32.0")));

        // Med School VPN
        assertTrue((this.module.proxyLinks("64.9.230.210")));
        assertTrue((this.module.proxyLinks("171.65.44.22")));
        assertTrue((this.module.proxyLinks("171.65.45.22")));
        assertFalse((this.module.proxyLinks("171.65.46.22")));
        assertFalse((this.module.proxyLinks("171.65.43.22")));
        assertTrue((this.module.proxyLinks("171.65.128.22")));
        assertTrue((this.module.proxyLinks("171.65.201.22")));
        assertTrue((this.module.proxyLinks("171.65.256.22")));
        assertFalse((this.module.proxyLinks("171.65.127.33")));
        assertTrue((this.module.proxyLinks("12.22.127.33")));
        assertTrue((this.module.proxyLinks("171.65.56.1")));
        assertTrue((this.module.proxyLinks("171.65.59.255")));
        assertTrue((this.module.proxyLinks("171.65.124.1")));
        assertTrue((this.module.proxyLinks("171.65.125.255")));
        assertFalse((this.module.proxyLinks("171.65.55.255")));
        assertFalse((this.module.proxyLinks("171.65.60.1")));
        assertFalse((this.module.proxyLinks("171.65.123.255")));
        assertFalse((this.module.proxyLinks("171.65.126.1")));
        assertTrue((this.module.proxyLinks("171.65.195.4")));
        assertTrue((this.module.proxyLinks("171.65.173.37")));
    }

    public void testTemplates() throws ConfigurationException {
        assertTrue("shc".equals(this.module
                .getTemplateName("shc/foo.html")));
    }

}
