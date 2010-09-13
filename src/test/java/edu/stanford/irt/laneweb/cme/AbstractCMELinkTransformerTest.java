/**
 * 
 */
package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.*;

import edu.stanford.irt.laneweb.model.Model;

import org.junit.Before;
import org.junit.Test;


/**
 * @author ryanmax
 *
 */
public class AbstractCMELinkTransformerTest {

    private AbstractCMELinkTransformer transformer;
    
    private Model model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractCMELinkTransformer() {
            //
        };
        this.model = createMock(Model.class);
        this.transformer.setModel(this.model);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#createCMELink(java.lang.String)}.
     */
    @Test
    public void testCreateCMELink() {
        expect(this.model.getString(Model.EMRID)).andReturn("epic-123456");
        replayMocks();
        this.transformer.initialize();
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/online"));
        assertEquals("http://www.uptodate.com/foo?bar=true&unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/foo?bar=true"));
        assertEquals("http://www.uptodate.com/online/content/search.do?unid=epic-123456&srcsys=epic90710&eiv=2.1.0", this.transformer.createCMELink("http://www.uptodate.com/"));
        assertEquals("http://www.uptodate.com", this.transformer.createCMELink("http://www.uptodate.com"));
        verifyMocks();
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
    
    private void replayMocks() {
        replay(this.model);
    }

    private void verifyMocks() {
        verify(this.model);
    }

}
