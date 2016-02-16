package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class SearchResultTest {

    private ContentResult contentResult;

    private Result resourceResult;

    private SearchResult searchResult;

    @Before
    public void setUp() {
        this.contentResult = createMock(ContentResult.class);
        this.resourceResult = createMock(Result.class);
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(this.contentResult);
        this.searchResult = new SearchResult(this.contentResult, this.resourceResult, 100);
        reset(this.contentResult);
    }

    @Test
    public void testCompareToSameTitleDifferentContentIds() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("99999").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("999").build();
        SearchResult first = new SearchResult(result1, this.resourceResult, 900);
        SearchResult second = new SearchResult(result2, this.resourceResult, 900);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void testCompareToSameTitleDifferentDates() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").date("2012").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").date("2010").build();
        SearchResult first = new SearchResult(result1, this.resourceResult, 900);
        SearchResult second = new SearchResult(result2, this.resourceResult, 900);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void testCompareToSameTitleDifferentScore() {
        expect(this.contentResult.getTitle()).andReturn("title").times(2);
        replay(this.contentResult);
        SearchResult first = new SearchResult(this.contentResult, this.resourceResult, 900);
        SearchResult second = new SearchResult(this.contentResult, this.resourceResult, 0);
        assertTrue(second.compareTo(first) > 0);
        verify(this.contentResult);
    }

    @Test
    public void testCompareToSameTitleSameContentIds() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("999").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("999").build();
        SearchResult first = new SearchResult(result1, this.resourceResult, 900);
        SearchResult second = new SearchResult(result2, this.resourceResult, 900);
        assertEquals(0, second.compareTo(first));
    }

    @Test
    public void testCompareToTitle() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("first title");
        replay(result, this.contentResult);
        SearchResult first = new SearchResult(result, this.resourceResult, 100);
        assertTrue(this.searchResult.compareTo(first) > 0);
        verify(result, this.contentResult);
    }

    @Test
    public void testEquals() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").build();
        SearchResult one = new SearchResult(result1, this.resourceResult, 100);
        SearchResult two = new SearchResult(result2, this.resourceResult, 100);
        assertTrue(one.equals(two));
    }

    @Test
    public void testEqualsDifferentObject() {
        assertFalse(this.searchResult.equals(new Object()));
    }

    @Test
    public void testEqualsNullContentIdsNullCompareStrings() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").build();
        SearchResult one = new SearchResult(result1, this.resourceResult, 100);
        SearchResult two = new SearchResult(result2, this.resourceResult, 100);
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
        ContentResult r = createMock(ContentResult.class);
        expect(r.getTitle()).andReturn("the title");
        replay(r);
        SearchResult c = new SearchResult(r, this.resourceResult, 0);
        assertEquals("title", c.getSortTitle());
        verify(r);
    }

    @Test
    public void testHasAdditionalText() {
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.contentResult);
        assertTrue(this.searchResult.hasAdditionalText());
        verify(this.contentResult);
    }

    @Test
    public void testHasAdditionalTextEmpty() {
        expect(this.contentResult.getDescription()).andReturn("");
        replay(this.contentResult);
        assertFalse(this.searchResult.hasAdditionalText());
        verify(this.contentResult);
    }

    @Test
    public void testHasAdditionalTextNull() {
        expect(this.contentResult.getDescription()).andReturn(null);
        replay(this.contentResult);
        assertFalse(this.searchResult.hasAdditionalText());
        verify(this.contentResult);
    }

    @Test
    public void testHashCode() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title");
        replay(result, this.contentResult);
        SearchResult other = new SearchResult(result, this.resourceResult, 0);
        assertEquals(this.searchResult.hashCode(), other.hashCode());
        verify(result, this.contentResult);
    }

    @Test
    public void testNotEquals() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("not the title");
        replay(this.contentResult, result);
        SearchResult other = new SearchResult(result, this.resourceResult, 0);
        assertFalse(this.searchResult.equals(other));
        verify(result, this.contentResult);
    }

    @Test
    public void testNotEqualsDifferentContentIds() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("99999").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("999").build();
        SearchResult one = new SearchResult(result1, this.resourceResult, 100);
        SearchResult two = new SearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }

    @Test
    public void testNotEqualsDifferentContentIdsDifferentAuthors() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("cid").author("authors").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").author("different authors").build();
        SearchResult one = new SearchResult(result1, this.resourceResult, 100);
        SearchResult two = new SearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }

    @Test
    public void testNotEqualsTwoDifferentContentIds() {
        ContentResult result1 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("cid").build();
        ContentResult result2 = ContentResult.newContentResultBuilder().id("id").description("description").url("url")
                .title("same title").contentId("different cid").build();
        SearchResult one = new SearchResult(result1, this.resourceResult, 100);
        SearchResult two = new SearchResult(result2, this.resourceResult, 100);
        assertFalse(one.equals(two));
    }
}
