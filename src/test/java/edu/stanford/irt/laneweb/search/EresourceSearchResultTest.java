package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.Eresource;

public class EresourceSearchResultTest {

    private Eresource eresource1;

    private Eresource eresource2;

    private Eresource eresource3;

    private EresourceSearchResult eresourceSearchResult1;

    private EresourceSearchResult eresourceSearchResult2;

    private EresourceSearchResult eresourceSearchResult3;

    @Test
    @Ignore
    public void anotherCompareToNonFiling() {
        expect(this.eresource1.getTitle()).andReturn("an foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("foo");
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    @Ignore
    public void anotherCompareToScore() {
        expect(this.eresource1.getTitle()).andReturn("foo");
        expect(this.eresource1.getScore()).andReturn(100);
        expect(this.eresource2.getTitle()).andReturn("foo");
        expect(this.eresource2.getScore()).andReturn(99);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) < 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) > 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    @Ignore
    public void anotherCompareToUnequal() {
        expect(this.eresource1.getTitle()).andReturn("foo bar");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource2.getTitle()).andReturn("bar foo");
        expect(this.eresource2.getScore()).andReturn(99);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    @Ignore
    public void anotherTestCompareToEqual() {
        expect(this.eresource1.getTitle()).andReturn("a foo");
        expect(this.eresource1.getScore()).andReturn(0);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("Foo");
        expect(this.eresource2.getScore()).andReturn(0);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        expect(this.eresource3.getTitle()).andReturn("the foo");
        expect(this.eresource3.getScore()).andReturn(0);
        expect(this.eresource3.getRecordId()).andReturn(3).times(2);
        replay(this.eresource1, this.eresource2, this.eresource3);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        this.eresourceSearchResult3 = new EresourceSearchResult(this.eresource3);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult3) > 0);
        assertTrue(this.eresourceSearchResult3.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2, this.eresource3);
    }

    @Test
    @Ignore
    public void compareNotEresource() {
        expect(this.eresource1.getTitle()).andReturn("title");
        expect(this.eresource1.getScore()).andReturn(12);
        SearchResult other = createMock(SearchResult.class);
        expect(other.getScore()).andReturn(12);
        expect(other.getSortTitle()).andReturn("title");
        replay(this.eresource1, other);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        assertEquals(-1, this.eresourceSearchResult1.compareTo(other));
        verify(this.eresource1, other);
    }

    @Before
    public void setUp() {
        this.eresource1 = createMock(Eresource.class);
        this.eresource2 = createMock(Eresource.class);
        this.eresource3 = createMock(Eresource.class);
    }

    @Test
    @Ignore
    public void testAnotherDemonstrateCase82529() {
        expect(this.eresource1.getTitle()).andReturn("actarheumatologicascandinavica");
        expect(this.eresource2.getTitle()).andReturn("rheumatology");
        expect(this.eresource1.getScore()).andReturn(-1);
        expect(this.eresource2.getScore()).andReturn(Integer.MAX_VALUE);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        int xcompareToy = this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2);
        int ycompareTox = this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1);
        assertTrue(xcompareToy > 0 && ycompareTox < 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    public void testCompareEquals() {
        expect(this.eresource1.getTitle()).andReturn("title");
        expect(this.eresource1.getScore()).andReturn(12);
        replay(this.eresource1);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        assertEquals(0, this.eresourceSearchResult1.compareTo(this.eresourceSearchResult1));
        verify(this.eresource1);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToCase() {
        expect(this.eresource1.getTitle()).andReturn("foo").times(1);
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("FOO").times(1);
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToEqual() {
        expect(this.eresource1.getTitle()).andReturn("foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("The Foo");
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToIntransitive() {
        expect(this.eresource1.getTitle()).andReturn("the bar");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource2.getTitle()).andReturn("a bar");
        expect(this.eresource2.getScore()).andReturn(100);
        expect(this.eresource3.getTitle()).andReturn("a bar");
        expect(this.eresource3.getScore()).andReturn(101);
        replay(this.eresource1, this.eresource2, this.eresource3);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        this.eresourceSearchResult3 = new EresourceSearchResult(this.eresource3);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult3) > 0);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult3) > 0);
        verify(this.eresource1, this.eresource2, this.eresource3);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToNonFiling() {
        expect(this.eresource1.getTitle()).andReturn("a foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("foo");
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToScore() {
        expect(this.eresource1.getTitle()).andReturn("foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("foo");
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.EresourceSearchResult#compareTo(edu.stanford.irt.laneweb.search.SearchResult)}
     * .
     */
    @Test
    @Ignore
    public void testCompareToUnequal() {
        expect(this.eresource1.getTitle()).andReturn("bar");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource2.getTitle()).andReturn("a bar");
        expect(this.eresource2.getScore()).andReturn(100);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    @Ignore
    public void testDemonstrateCase82529() {
        expect(this.eresource1.getTitle()).andReturn("Rheumatology");
        expect(this.eresource2.getTitle()).andReturn("Rheumatology");
        expect(this.eresource1.getScore()).andReturn(Integer.MAX_VALUE);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getScore()).andReturn(Integer.MAX_VALUE);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        int xcompareToy = this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2);
        int ycompareTox = this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1);
        assertTrue(xcompareToy > 0 && ycompareTox < 0);
        verify(this.eresource1, this.eresource2);
    }

    @Test
    public void testHasAdditionalText() {
        expect(this.eresource1.getTitle()).andReturn("the foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getDescription()).andReturn("description");
        replay(this.eresource1);
        assertTrue(new EresourceSearchResult(this.eresource1).hasAdditionalText());
        verify(this.eresource1);
    }

    @Test
    public void testHasAdditionalTextEmpty() {
        expect(this.eresource1.getTitle()).andReturn("the foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getDescription()).andReturn("");
        replay(this.eresource1);
        assertFalse(new EresourceSearchResult(this.eresource1).hasAdditionalText());
        verify(this.eresource1);
    }

    @Test
    public void testHasAdditionalTextNull() {
        expect(this.eresource1.getTitle()).andReturn("the foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getDescription()).andReturn(null);
        replay(this.eresource1);
        assertFalse(new EresourceSearchResult(this.eresource1).hasAdditionalText());
        verify(this.eresource1);
    }

    @Test
    public void testNotEquals() {
        expect(this.eresource1.getTitle()).andReturn("title");
        expect(this.eresource1.getScore()).andReturn(0);
        replay(this.eresource1);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        assertFalse(this.eresourceSearchResult1.equals(new Object()));
        verify(this.eresource1);
    }

    @Test
    public void testSameHashCode() {
        expect(this.eresource1.getTitle()).andReturn("title").times(2);
        expect(this.eresource1.getScore()).andReturn(0).times(2);
        replay(this.eresource1);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource1);
        assertEquals(this.eresourceSearchResult1.hashCode(), this.eresourceSearchResult2.hashCode());
        verify(this.eresource1);
    }

    @Test
    @Ignore
    public void yetAnotherCompareToNonFiling() {
        expect(this.eresource1.getTitle()).andReturn("the foo");
        expect(this.eresource1.getScore()).andReturn(99);
        expect(this.eresource1.getRecordId()).andReturn(1).times(2);
        expect(this.eresource2.getTitle()).andReturn("foo");
        expect(this.eresource2.getScore()).andReturn(99);
        expect(this.eresource2.getRecordId()).andReturn(2).times(2);
        replay(this.eresource1, this.eresource2);
        this.eresourceSearchResult1 = new EresourceSearchResult(this.eresource1);
        this.eresourceSearchResult2 = new EresourceSearchResult(this.eresource2);
        assertTrue(this.eresourceSearchResult1.compareTo(this.eresourceSearchResult2) > 0);
        assertTrue(this.eresourceSearchResult2.compareTo(this.eresourceSearchResult1) < 0);
        verify(this.eresource1, this.eresource2);
    }
}
