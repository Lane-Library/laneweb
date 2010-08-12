/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;

/**
 * @author ryanmax
 */
public class EresourceSearchResultTest {

    private Eresource eresource1;

    private Eresource eresource2;

    private Eresource eresource3;

    private EresourceSearchResult eresourceSearchResult1;

    private EresourceSearchResult eresourceSearchResult2;

    private EresourceSearchResult eresourceSearchResult3;

    @Before
    public void setUp() {
        this.eresource1 = new EresourceImpl();
        this.eresource2 = new EresourceImpl();
        this.eresource3 = new EresourceImpl();
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
        this.eresource1.setTitle("a foo");
        this.eresource1.setScore(0);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("Foo");
        this.eresource2.setScore(0);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        this.eresource3.setTitle("the foo");
        this.eresource3.setScore(0);
        this.eresourceSearchResult3 = new EresourceSearchResult(this.eresource3);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult3) == 0);
        assertTrue(this.eresourceSearchResult3.compareTo(this.eresourceSearchResult1) == 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
        this.eresource1.setTitle("an foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
        this.eresource1.setTitle("the foo");
        this.eresource1.setScore(99);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresource2.setTitle("foo");
        this.eresource2.setScore(99);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) == 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) == 0);
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
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.searchresults.SearchResult)}
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
}
