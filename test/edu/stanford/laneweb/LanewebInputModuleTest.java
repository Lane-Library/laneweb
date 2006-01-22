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
        parameters.setParameter("include-regex","^1(71\\.6[4-7]\\.|0\\.25[23])\\S+");
        parameters.setParameter("exclude-regex","^171\\.66\\.(1[6-9]|2[0-3])\\.\\S+");
        this.module.parameterize(parameters);
    }
    
    public void testMyIPIsStanford() {
        assertTrue("true".equals(this.module.getIsStanford("171.65.28.158")));
    }
    
    public void testMyIPIsSU() {
        assertTrue("LPCH".equals(this.module.getAffiliation("10.252.31.112")));
    }

}
