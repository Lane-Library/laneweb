package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

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
    public void testEquals() {
        ContentResult result = createMock(ContentResult.class);
        expect(result.getTitle()).andReturn("the title");
        replay(result);
        ContentResultSearchResult other = new ContentResultSearchResult(result, this.resourceResult, 0);
        assertTrue(this.searchResult.equals(other));
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
    public void testEqualsDifferentObject() {
        assertFalse(this.searchResult.equals(new Object()));
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
    public void testGetResourceResult() {
        assertTrue(this.resourceResult == this.searchResult.getResourceResult());
    }
    
    @Test
    public void testGetContentResult() {
        assertTrue(this.contentResult == this.searchResult.getContentResult());
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
}
