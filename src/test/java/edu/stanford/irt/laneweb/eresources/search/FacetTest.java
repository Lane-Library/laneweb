/**
 *
 */
package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author ryanmax
 */
public class FacetTest {

    @Test
    public final void testFacet() {
        Facet facet = new Facet("fieldName", "value", 0, "activeFacets");
        assertEquals("fieldName", facet.getFieldName());
        assertEquals("value", facet.getValue());
        assertEquals(0, facet.getCount());
        assertFalse(facet.isEnabled());
    }

    @Test
    public final void testGetUrl() {
        Facet facet = new Facet("fieldName", "value", 0, "activeFacets");
        assertEquals("activeFacets%3A%3AfieldName%3A%22value%22", facet.getUrl());
        Facet facet2 = new Facet("type", "Book", 1, "foo:\"bar\"::type:\"Book Print\"");
        assertEquals("foo%3A%22bar%22", facet2.getUrl());
    }

    @Test
    public final void testIsEnabled() {
        assertTrue(new Facet("fieldName", "value", 0, "fieldName:\"value\"").isEnabled());
        assertFalse(new Facet("fieldName", "value", 0, "").isEnabled());
        assertFalse(new Facet("fieldName", "value", 0, null).isEnabled());
        assertTrue(new Facet("type", "Book", 0, "type:\"Book Digital\"").isEnabled());
    }

    @Test
    public final void testToString() {
        Facet facet = new Facet("fieldName", "value", 0, "activeFacets");
        assertEquals("value = 0; enabled=false; url=activeFacets%3A%3AfieldName%3A%22value%22", facet.toString());
    }
}
