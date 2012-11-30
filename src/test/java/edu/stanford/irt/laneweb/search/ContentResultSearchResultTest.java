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

public class ContentResultSearchResultTest {

    private ContentResult contentResult;

    private Result resourceResult;

    private ContentResultSearchResult searchResult;

    @Before
    public void setUp() {
        this.contentResult = createMock(ContentResult.class);
        this.resourceResult = createMock(Result.class);
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(this.contentResult);
        this.searchResult = new ContentResultSearchResult(this.contentResult, this.resourceResult, 100);
        verify(this.contentResult);
    }

    @Test
    public void testCompareToSameTitleDifferentContentIds() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title").times(2);
        expect(aContentResult.getContentId()).andReturn("99999");
        expect(aContentResult.getContentId()).andReturn("999");
        replay(aContentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(aContentResult, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(aContentResult, this.resourceResult, 0);
        assertTrue(second.compareTo(first) > 0);
        verify(aContentResult);
    }

    @Test
    public void testCompareToSameTitleDifferentDates() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title").times(2);
        expect(aContentResult.getContentId()).andReturn(null).times(2);
        expect(aContentResult.getPublicationDate()).andReturn("2012");
        expect(aContentResult.getPublicationDate()).andReturn("2010");
        expect(aContentResult.getPublicationVolume()).andReturn(null).times(2);
        expect(aContentResult.getPublicationIssue()).andReturn(null).times(2);
        expect(aContentResult.getAuthor()).andReturn(null).times(2);
        replay(aContentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(aContentResult, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(aContentResult, this.resourceResult, 0);
        assertTrue(second.compareTo(first) > 0);
        verify(aContentResult);
    }

    @Test
    public void testCompareToSameTitleDifferentScore() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title").times(2);
        expect(aContentResult.getContentId()).andReturn(null).times(2);
        expect(aContentResult.getPublicationDate()).andReturn(null).times(2);
        expect(aContentResult.getPublicationVolume()).andReturn(null).times(2);
        expect(aContentResult.getPublicationIssue()).andReturn(null).times(2);
        expect(aContentResult.getAuthor()).andReturn(null).times(2);
        replay(aContentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(aContentResult, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(aContentResult, this.resourceResult, 0);
        assertTrue(second.compareTo(first) > 0);
        verify(aContentResult);
    }

    @Test
    public void testCompareToSameTitleSameContentIds() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title").times(2);
        expect(aContentResult.getContentId()).andReturn("999").times(2);
        replay(aContentResult);
        ContentResultSearchResult first = new ContentResultSearchResult(aContentResult, this.resourceResult, 900);
        ContentResultSearchResult second = new ContentResultSearchResult(aContentResult, this.resourceResult, 0);
        assertEquals(0, second.compareTo(first));
        verify(aContentResult);
    }

    @Test
    public void testCompareToTitle() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("first title");
        replay(result);
        ContentResultSearchResult first = new ContentResultSearchResult(result, this.resourceResult, 100);
        assertTrue(this.searchResult.compareTo(first) > 0);
        verify(result);
    }

    @Test
    public void testEquals() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getContentId()).andReturn("cid").times(2);
        expect(aContentResult.getTitle()).andReturn("the title").times(2);
        replay(aContentResult);
        ContentResultSearchResult one = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        assertTrue(one.equals(two));
        verify(aContentResult);
    }

    @Test
    public void testEqualsDifferentObject() {
        assertFalse(this.searchResult.equals(new Object()));
    }

    @Test
    public void testEqualsNullContentIdsNullCompareStrings() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title").times(2);
        expect(aContentResult.getContentId()).andReturn(null).times(2);
        expect(aContentResult.getPublicationDate()).andReturn(null).times(2);
        expect(aContentResult.getPublicationVolume()).andReturn(null).times(2);
        expect(aContentResult.getPublicationIssue()).andReturn(null).times(2);
        expect(aContentResult.getAuthor()).andReturn(null).times(2);
        replay(aContentResult);
        ContentResultSearchResult one = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        assertTrue(one.equals(two));
        verify(aContentResult);
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
        assertEquals("title", this.searchResult.getSortTitle());
    }

    @Test
    public void testHashCode() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title");
        replay(result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, this.resourceResult, 0);
        assertEquals(this.searchResult.hashCode(), other.hashCode());
        verify(result);
    }

    @Test
    public void testNotEquals() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("not the title");
        replay(result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, this.resourceResult, 0);
        assertFalse(this.searchResult.equals(other));
        verify(result);
    }

    @Test
    public void testNotEqualsDifferentContentIds() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getTitle()).andReturn("same title");
        expect(aContentResult.getContentId()).andReturn("cid");
        replay(aContentResult);
        ContentResult bContentResult = createMock(ContentResult.class);
        expect(bContentResult.getTitle()).andReturn("same title");
        expect(bContentResult.getContentId()).andReturn("different cid");
        replay(bContentResult);
        ContentResultSearchResult one = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(bContentResult, this.resourceResult, 100);
        assertFalse(one.equals(two));
        verify(aContentResult);
        verify(bContentResult);
    }

    @Test
    public void testNotEqualsDifferentContentIdsDifferentAuthors() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getContentId()).andReturn("cid");
        expect(aContentResult.getTitle()).andReturn("same title");
        expect(aContentResult.getPublicationDate()).andReturn("2000 Nov");
        expect(aContentResult.getPublicationVolume()).andReturn("vol");
        expect(aContentResult.getPublicationIssue()).andReturn("issue");
        expect(aContentResult.getAuthor()).andReturn("authors");
        replay(aContentResult);
        ContentResult bContentResult = createMock(ContentResult.class);
        expect(bContentResult.getContentId()).andReturn(null);
        expect(bContentResult.getTitle()).andReturn("same title");
        expect(bContentResult.getPublicationDate()).andReturn("2000 Oct");
        expect(bContentResult.getPublicationVolume()).andReturn("vol");
        expect(bContentResult.getPublicationIssue()).andReturn("issue");
        expect(bContentResult.getAuthor()).andReturn("diffferent authors");
        replay(bContentResult);
        ContentResultSearchResult one = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(bContentResult, this.resourceResult, 100);
        assertFalse(one.equals(two));
        verify(aContentResult);
        verify(bContentResult);
    }

    @Test
    public void testNotEqualsTwoDifferentContentIds() {
        ContentResult aContentResult = createMock(ContentResult.class);
        expect(aContentResult.getContentId()).andReturn("cid");
        expect(aContentResult.getTitle()).andReturn("same title");
        replay(aContentResult);
        ContentResult bContentResult = createMock(ContentResult.class);
        expect(bContentResult.getContentId()).andReturn("different cid");
        expect(bContentResult.getTitle()).andReturn("same title");
        replay(bContentResult);
        ContentResultSearchResult one = new ContentResultSearchResult(aContentResult, this.resourceResult, 100);
        ContentResultSearchResult two = new ContentResultSearchResult(bContentResult, this.resourceResult, 100);
        assertFalse(one.equals(two));
        verify(aContentResult);
        verify(bContentResult);
    }
}
