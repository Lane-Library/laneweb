package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.Eresource.EresourceBuilder;

public class EresourceTest {

    private EresourceBuilder builder;

    @Before
    public void setUp() {
        this.builder = Eresource.builder();
    }

    @Test
    public void testIsValidOnlyImpactFactory() {
        assertFalse(this.builder.addLink(new Link(null, LinkType.IMPACTFACTOR, null, null, null, null, null)).build()
                .isValid());
    }

    @Test
    public void testIsValidOnlyNormal() {
        assertTrue(this.builder.addLink(new Link(null, LinkType.NORMAL, null, null, null, null, null)).build()
                .isValid());
    }
}
