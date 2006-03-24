/*
 * Created on Aug 22, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;


import junit.framework.TestCase;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;


public class LanewebInputModuleTest extends TestCase {
    
    LanewebInputModule module;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.module = new LanewebInputModule();
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
        Configuration config = builder.build("src/conf/cocoon.xconf").getChild("input-modules").getChildren()[0];
        this.module.configure(config);
    }
    
//    public void testTicket() {
//    		assertTrue(this.module.getTicket("ceyates").indexOf("user=ceyates&ticket=") == 0);
//    		assertTrue(this.module.getTicket("ceyates").length() == 66);
//    }
    
    public void testProxyMyIP() throws ConfigurationException {
        assertTrue("false".equals(this.module.proxyLinks("171.65.28.158")));
    }
    
    public void testAFewIPs() throws ConfigurationException {
    	assertTrue("true".equals(this.module.proxyLinks("171.65.44.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.45.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.46.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.43.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.128.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.201.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.256.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.127.33")));
    	assertTrue("true".equals(this.module.proxyLinks("12.22.127.33")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.56.1")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.59.255")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.124.1")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.125.255")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.55.255")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.60.1")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.123.255")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.126.1")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.195.4")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.173.37")));
    }

    public void testLPCHIP() {
        assertTrue("LPCH".equals(this.module.getAffiliation("10.252.31.112")));
    }
    
    public void testTemplates() throws ConfigurationException {
    		assertTrue("100years_index".equals(this.module.getTemplateName("/100years/index.html")));
    		assertTrue("100years_index".equals(this.module.getTemplateName("/stage/100years/index.html")));
    		assertTrue("100years".equals(this.module.getTemplateName("/100years/anotherpage.html")));
    		assertTrue("100years".equals(this.module.getTemplateName("/stage/100years/anotherpage.html")));
    		assertTrue("100years".equals(this.module.getTemplateName("/beta/100years/anotherpage.html")));
    		assertTrue("100years_index".equals(this.module.getTemplateName("/beta/stage/100years/index.html")));
    }

}
