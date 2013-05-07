package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;

/**
 * @author ryanmax
 */
public class EresourceSearchResultTest {

    private EresourceImpl eresource1;

    private EresourceImpl eresource2;

    private EresourceImpl eresource3;

    private EresourceSearchResult eresourceSearchResult1;

    private EresourceSearchResult eresourceSearchResult2;

    private EresourceSearchResult eresourceSearchResult3;

    @Before
    public void setUp() {
        this.eresource1 = new EresourceImpl();
        this.eresource1.setRecordId(1);
        this.eresource2 = new EresourceImpl();
        this.eresource2.setRecordId(2);
        this.eresource3 = new EresourceImpl();
        this.eresource3.setRecordId(3);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToCase() {
        this.eresource1.setTitle("foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("FOO");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToEqual() {
        this.eresource1.setTitle("foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("The Foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        this.eresource1.setTitle("a foo");
        this.eresource1.setScore(0);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("Foo");
        this.eresource2.setScore(0);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        this.eresource3.setTitle("the foo");
        this.eresource3.setScore(0);
        this.eresourceSearchResult3 = new EresourceSearchResult(this.eresource3);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult3) > 0);
        assertTrue(this.eresourceSearchResult3.compareTo(this.eresourceSearchResult1) < 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToIntransitive() {
        this.eresource1.setTitle("the bar");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("a bar");
        this.eresource2.setScore(100);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        this.eresource3.setTitle("a bar");
        this.eresource3.setScore(101);
        this.eresourceSearchResult3 = new EresourceSearchResult(this.eresource3);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult3) > 0);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult3) > 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToNonFiling() {
        this.eresource1.setTitle("a foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        this.eresource1.setTitle("an foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        this.eresource1.setTitle("the foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToScore() {
        this.eresource1.setTitle("foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        this.eresource1.setTitle("foo");
        this.eresource1.setScore(100);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) < 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) > 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    public void testCompareToUnequal() {
        this.eresource1.setTitle("bar");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("a bar");
        this.eresource2.setScore(100);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        this.eresource1.setTitle("foo bar");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("bar foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
    }
    
    @Test
    public void testDemonstrateCase82529() {
        Eresource eresourceA = createMock(Eresource.class);
        Eresource eresourceB = createMock(Eresource.class);
        expect(eresourceA.getTitle()).andReturn("Rheumatology");
        expect(eresourceB.getTitle()).andReturn("Rheumatology");
        expect(eresourceA.getScore()).andReturn(Integer.MAX_VALUE).times(2);
        expect(eresourceA.getRecordId()).andReturn(1).times(2);
        expect(eresourceB.getScore()).andReturn(Integer.MAX_VALUE).times(2);
        expect(eresourceB.getRecordId()).andReturn(2).times(2);
        replay(eresourceA, eresourceB);
        this.eresourceSearchResult1 = new EresourceSearchResult(eresourceA);
        this.eresourceSearchResult2 = new EresourceSearchResult(eresourceB);
        int xcompareToy = this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2);
        int ycompareTox = this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1);
        assertTrue(xcompareToy > 0 && ycompareTox < 0);
        verify(eresourceA, eresourceB);
    }
    
    @Test
    public void testAnotherDemonstrateCase82529() {
        Eresource eresourceA = createMock(Eresource.class);
        Eresource eresourceB = createMock(Eresource.class);
        expect(eresourceA.getTitle()).andReturn("actarheumatologicascandinavica");
        expect(eresourceB.getTitle()).andReturn("rheumatology");
        expect(eresourceA.getScore()).andReturn(-1).times(2);
        expect(eresourceB.getScore()).andReturn(Integer.MAX_VALUE).times(2);
        replay(eresourceA, eresourceB);
        this.eresourceSearchResult1 = new EresourceSearchResult(eresourceA);
        this.eresourceSearchResult2 = new EresourceSearchResult(eresourceB);
        int xcompareToy = this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2);
        int ycompareTox = this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1);
        System.out.println(xcompareToy);
        System.out.println(ycompareTox);
        assertTrue(xcompareToy > 0 && ycompareTox < 0);
        verify(eresourceA, eresourceB);
    }
}
