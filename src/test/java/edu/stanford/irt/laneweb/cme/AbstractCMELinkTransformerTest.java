/**
 * 
 */
package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.model.Model;

import org.junit.Before;
import org.junit.Test;


/**
 * @author ryanmax
 *
 */
public class AbstractCMELinkTransformerTest {

    private AbstractCMELinkTransformer transformer;
    
    private Map<String, Object> model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractCMELinkTransformer() {
            //
        };
        this.model = new HashMap<String, Object>();
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#createCMELink(java.lang.String)}.
     */
    @Test
    public void testCreateCMELink() {
        this.model.put(Model.EMRID, "epic-123456");
        this.transformer.setup(null, this.model, null, null);
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/online"));
        assertEquals("http://www.uptodate.com/foo?bar=true&unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/foo?bar=true"));
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/"));
        assertEquals("http://www.uptodate.com", this.transformer.createCMELink("http://www.uptodate.com"));
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)}.
     */
    @Test
    public void testIsCMEHost() {
        assertTrue(this.transformer.isCMEHost("http://www.uptodate.com/online"));
    }
    
    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)}.
     */
    @Test
    public void testIsNotCMEHost() {
        assertFalse(this.transformer.isCMEHost("http://www.google.com/"));
    }

}
