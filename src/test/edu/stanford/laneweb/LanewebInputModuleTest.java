/*
 * Created on Aug 22, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

import junit.framework.TestCase;

import org.apache.avalon.framework.parameters.Parameters;

public class LanewebInputModuleTest extends TestCase {
    
    LanewebInputModule module;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.module = new LanewebInputModule();
        Parameters parameters = new Parameters();
        parameters.setParameter("noproxy-regex","^171\\.6[4-7]\\.\\S+");
        parameters.setParameter("proxy-regex","^171\\.6(6\\.(1[6-9]|2[0-3])|5\\.(4[4-5]|(12[8-9]|2[0-9][0-9])))\\.\\S+");
        this.module.parameterize(parameters);
    }
    
    public void testProxyMyIP() {
        assertTrue("false".equals(this.module.proxyLinks("171.65.28.158")));
    }
    
    public void testAFewIPs() {
    	assertTrue("true".equals(this.module.proxyLinks("171.65.44.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.45.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.46.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.43.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.128.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.201.22")));
    	assertTrue("true".equals(this.module.proxyLinks("171.65.256.22")));
    	assertTrue("false".equals(this.module.proxyLinks("171.65.127.33")));
    	assertTrue("true".equals(this.module.proxyLinks("12.22.127.33")));
    }
    
    public void testMyIPIsSU() {
        assertTrue("LPCH".equals(this.module.getAffiliation("10.252.31.112")));
    }

}
