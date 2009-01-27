package edu.stanford.irt.laneweb;

import static org.junit.Assert.*;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

public class MTMungingInputModuleTest {
    
    private MTMungingInputModule inputModule;

    @Before
    public void setUp() throws Exception {
        this.inputModule = new MTMungingInputModule();
    }

    @Test
    public void testGetAttribute() throws ConfigurationException {
        assertEquals("1234",this.inputModule.getAttribute("_1234", null, null));
        assertEquals("abcd", this.inputModule.getAttribute("abcd", null, null));
        assertEquals("library_access", this.inputModule.getAttribute("Library Access", null, null));
        assertEquals("special_collections_archives",this.inputModule.getAttribute("Special Collections & Archives", null, null));
        assertEquals("foo_bar", this.inputModule.getAttribute(" & _ foo * ^$ bar +", null, null));
    }

    @Test
    public void testGetAttributeNames() throws ConfigurationException {
        try {
            this.inputModule.getAttributeNames(null, null);
            fail();
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void testGetAttributeValues() throws ConfigurationException {
        try {
            this.inputModule.getAttributeValues(null, null, null);
            fail();
        } catch (UnsupportedOperationException e) {}
    }

}
