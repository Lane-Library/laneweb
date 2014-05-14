package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ryanmax
 */
public class AbstractCMELinkTransformerTest {

    private Map<String, Object> model;

    private AbstractCMELinkTransformer transformer;

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
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#createCMELink(java.lang.String)} .
     */
    @Test
    public void testCreateCMELink() {
        this.model.put(Model.EMRID, "epic-123456");
        this.transformer.setModel(this.model);
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/online"));
        assertEquals("http://www.uptodate.com/foo?bar=true&unid=epic-123456&srcsys=epic90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/foo?bar=true"));
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/"));
        assertEquals("http://www.uptodate.com", this.transformer.createCMELink("http://www.uptodate.com"));
        this.model.remove(Model.EMRID);
        this.model.put(Model.AUTH, "hashedSunet");
        this.transformer.setModel(this.model);
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=hashedSunet&srcsys=EZPX90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/online"));
        assertEquals("http://www.uptodate.com/foo?bar=true&unid=hashedSunet&srcsys=EZPX90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/foo?bar=true"));
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=hashedSunet&srcsys=EZPX90710&eiv=2.1.0",
                this.transformer.createCMELink("http://www.uptodate.com/"));
        assertEquals("http://www.uptodate.com", this.transformer.createCMELink("http://www.uptodate.com"));
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)} .
     */
    @Test
    public void testIsCMEHost() {
        assertTrue(this.transformer.isCMEHost("http://www.uptodate.com/online"));
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)} .
     */
    @Test
    public void testIsNotCMEHost() {
        assertFalse(this.transformer.isCMEHost("http://www.google.com/"));
    }
}
