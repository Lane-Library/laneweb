package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.DefaultContentResult;

public class ContentResultSearchResultTest {

    private ContentResult contentResult;

    private Result resourceResult;

    private ContentResultSearchResult searchResult;

    @Before
    public void setUp() {
        this.contentResult = createMock(ContentResult.class);
        this.resourceResult = createMock(Result.class);
        this.searchResult = new ContentResultSearchResult(this.contentResult, this.resourceResult, 100);
    }
    
    @Test
    public void testNotContentResultSearchResult() {
        SearchResult result = createMock(SearchResult.class);
        expect(result.getScore()).andReturn(100);
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(result.getSortTitle()).andReturn("title");
        replay(this.contentResult, result);
        assertTrue(this.searchResult.compareTo(result) > 0);
        verify(this.contentResult, result);
    }

    @Test
    public void testCompareToSameTitleDifferentContentIds() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setContentId("99999");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result2.setContentId("999");
        ContentResultSearchResult first = new ContentResultSearchResult(result1, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(result2, this.resourceResult, 900);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void testCompareToSameTitleDifferentDates() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setPublicationDate("2012");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result2.setPublicationDate("2010");
        ContentResultSearchResult first = new ContentResultSearchResult(result1, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(result2, this.resourceResult, 900);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void testCompareToSameTitleDifferentScore() {
        replay(this.contentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(this.contentResult, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(this.contentResult, this.resourceResult, 0);
        assertTrue(second.compareTo(first) > 0);
        verify(this.contentResult);
    }

    @Test
    public void testCompareToSameTitleSameContentIds() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setContentId("999");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result2.setContentId("999");
        ContentResultSearchResult first = new ContentResultSearchResult(result1, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(result2, this.resourceResult, 900);
        assertEquals(0, second.compareTo(first));
    }

    @Test
    public void testCompareToTitle() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("first title");
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(result, this.contentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(result, this.resourceResult, 100);
        assertTrue(this.searchResult.compareTo(first) > 0);
        verify(result, this.contentResult);
    }

    @Test
    public void testEquals() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        ContentResultSearchResult one = new ContentResultSearchResult(result1, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(result2, this.resourceResult, 100);
        assertTrue(one.equals(two));
    }

    @Test
    public void testEqualsDifferentObject() {
        assertFalse(this.searchResult.equals(new Object()));
    }

    @Test
    public void testEqualsNullContentIdsNullCompareStrings() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        ContentResultSearchResult one = new ContentResultSearchResult(result1, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(result2, this.resourceResult, 100);
        assertTrue(one.equals(two));
    }

    @Test
    public void testGetContentResult() {
        assertTrue(this.contentResult == this.searchResult.getContentResult());
    }

    @Test
    public void testGetResourceResult() {
        assertTrue(this.resourceResult == this.searchResult.getResourceResult());
    }

    @Test
    public void testGetScore() {
        assertEquals(100, this.searchResult.getScore());
    }

    @Test
    public void testGetSortTitle() {
        expect(this.contentResult.getTitle()).andReturn("the title");
        replay(this.contentResult);
        assertEquals("title", this.searchResult.getSortTitle());
        verify(this.contentResult);
    }

    @Test
    public void testHashCode() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title");
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(result, this.contentResult);
        ContentResultSearchResult other = new ContentResultSearchResult(result, this.resourceResult, 0);
        assertEquals(this.searchResult.hashCode(), other.hashCode());
        verify(result, this.contentResult);
    }

    @Test
    public void testNotEquals() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("not the title");
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(this.contentResult, result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, this.resourceResult, 0);
        assertFalse(this.searchResult.equals(other));
        verify(result, this.contentResult);
    }

    @Test
    public void testNotEqualsDifferentContentIds() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setContentId("99999");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result2.setContentId("999");
        ContentResultSearchResult one = new ContentResultSearchResult(result1, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }

    @Test
    public void testNotEqualsDifferentContentIdsDifferentAuthors() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setContentId("cid");
        result1.setAuthor("authors");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result1.setAuthor("different authors");
        ContentResultSearchResult one = new ContentResultSearchResult(result1, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }

    @Test
    public void testNotEqualsTwoDifferentContentIds() {
        DefaultContentResult result1 = new DefaultContentResult("1");
        result1.setTitle("same title");
        result1.setContentId("cid");
        DefaultContentResult result2 = new DefaultContentResult("1");
        result2.setTitle("same title");
        result2.setContentId("different cid");
        ContentResultSearchResult one = new ContentResultSearchResult(result1, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }
}
