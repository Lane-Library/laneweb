package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.eresources.impl.VersionImpl;

/**
 * @author ryanmax
 */
public class EresourceVersionComparatorTest {

    private EresourceVersionComparator comparator;

    private LinkImpl link;

    private VersionImpl v1;

    private VersionImpl v2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.v1 = new VersionImpl();
        this.v2 = new VersionImpl();
        this.link = new LinkImpl();
        this.comparator = new EresourceVersionComparator();
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.eresources.EresourceVersionComparator#compare(edu.stanford.irt.eresources.Version, edu.stanford.irt.eresources.Version)}
     * .
     */
    @Test
    public void testCompare() {
        this.v1.setDates("1999.");
        this.v2.setDates("1999.");
        assertEquals(1, this.comparator.compare(this.v1, this.v2));
    }

    @Test
    public void testCompareClosedDates() {
        this.v1.setDates("1999-2000.");
        this.v2.setDates("1999-2000.");
        assertTrue(this.comparator.compare(this.v1, this.v2) == 1);
        this.v1.setDates("1999-2010.");
        this.v2.setDates("1999-2000.");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
    }

    @Test
    public void testCompareDelayedHoldings() {
        this.v1.setSummaryHoldings("v. 1-");
        this.v2.setSummaryHoldings("v. 1-");
        this.v2.setDescription("foo delayed bar");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
    }

    @Test
    public void testCompareHoldings() {
        this.v1.setSummaryHoldings("v. 1-");
        this.v2.setSummaryHoldings("v. 1.");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
        this.v1.setSummaryHoldings("v. 10-20.");
        this.v2.setSummaryHoldings("v. 10-");
        assertTrue(this.comparator.compare(this.v1, this.v2) > 0);
    }

    @Test
    public void testCompareImpactFactorHoldings() {
        this.v1.setSummaryHoldings("v. 1.");
        this.v2.setSummaryHoldings("v. 1.");
        this.v1.setDates("1999-2000.");
        this.v2.setDates("1999-2000.");
        this.link.setLabel("Impact Factor");
        this.v2.addLink(this.link);
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
    }

    @Test
    public void testCompareOpenDates() {
        this.v1.setDates("1999-");
        this.v2.setDates("1999-2000.");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
        this.v1.setDates("1999.");
        this.v2.setDates("1999-");
        assertTrue(this.comparator.compare(this.v1, this.v2) > 0);
    }

    @Test
    public void testComparePublishers() {
        this.v1.setDates("1999.");
        this.v1.setPublisher("ScienceDirect");
        this.v2.setDates("1999.");
        this.v2.setPublisher("Karger");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
        this.v1.setDates("1999.");
        this.v1.setPublisher("Karger");
        this.v2.setDates("1999.");
        this.v2.setPublisher("ScienceDirect");
        assertTrue(this.comparator.compare(this.v1, this.v2) > 0);
        this.v1.setDates("1999.");
        this.v1.setPublisher("PubMed Central");
        this.v2.setDates("1999.");
        this.v2.setPublisher("");
        assertTrue(this.comparator.compare(this.v1, this.v2) < 0);
    }
}
