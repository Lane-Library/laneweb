package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ScopusDeduplicatorTest {

    private ContentResultSearchResult anotherResult;

    private ContentResult contentResult;

    private ScopusDeduplicator deduplicator;

    private Result duplicateResource;

    private ContentResultSearchResult duplicateResult;

    private Result otherResource;

    private ContentResultSearchResult otherResult;

    private Result scopusResource;

    private ContentResultSearchResult scopusResult;

    private Set<ContentResultSearchResult> searchResults;

    @Before
    public void setUp() throws Exception {
        this.deduplicator = new ScopusDeduplicator();
        this.duplicateResult = createMock(ContentResultSearchResult.class);
        this.otherResult = createMock(ContentResultSearchResult.class);
        this.anotherResult = createMock(ContentResultSearchResult.class);
        this.scopusResult = createMock(ContentResultSearchResult.class);
        this.searchResults = new HashSet<ContentResultSearchResult>();
        this.otherResource = createMock(Result.class);
        this.duplicateResource = createMock(Result.class);
        this.scopusResource = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testRemoveDuplicates() {
        this.searchResults.add(this.duplicateResult);
        this.searchResults.add(this.otherResult);
        this.searchResults.add(this.scopusResult);
        this.searchResults.add(this.anotherResult);
        expect(this.duplicateResult.getResourceResult()).andReturn(this.duplicateResource).times(0, 4);
        expect(this.duplicateResource.getId()).andReturn("duplicate").times(0, 4);
        expect(this.duplicateResult.getContentResult()).andReturn(this.contentResult).times(0, 4);
        expect(this.otherResult.getResourceResult()).andReturn(this.otherResource).times(0, 4);
        expect(this.otherResource.getId()).andReturn("other").times(0, 4);
        expect(this.otherResult.getContentResult()).andReturn(this.contentResult).times(0, 4);
        expect(this.scopusResult.getResourceResult()).andReturn(this.scopusResource).times(0, 4);
        expect(this.scopusResource.getId()).andReturn("scopus").times(0, 4);
        expect(this.scopusResult.getContentResult()).andReturn(this.contentResult).times(0, 4);
        expect(this.anotherResult.getResourceResult()).andReturn(this.otherResource).times(0, 4);
        expect(this.anotherResult.getContentResult()).andReturn(this.contentResult).times(0, 4);
        expect(this.contentResult.isSameArticle(this.contentResult)).andReturn(false);
        expect(this.contentResult.isSameArticle(this.contentResult)).andReturn(true);
        replay(this.anotherResult, this.duplicateResult, this.otherResult, this.otherResource, this.scopusResult,
                this.scopusResource, this.duplicateResource, this.contentResult);
        this.deduplicator.removeDuplicates(this.searchResults);
        assertFalse(this.searchResults.contains(this.scopusResult));
        verify(this.anotherResult, this.otherResource, this.scopusResource, this.duplicateResource, this.otherResult,
                this.scopusResult, this.duplicateResult, this.contentResult);
    }

    @Test
    public void testRemoveDuplicatesNoScopus() {
        this.searchResults.add(this.duplicateResult);
        this.searchResults.add(this.otherResult);
        this.searchResults.add(this.anotherResult);
        expect(this.otherResult.getResourceResult()).andReturn(this.otherResource).atLeastOnce();
        expect(this.anotherResult.getResourceResult()).andReturn(this.otherResource).atLeastOnce();
        expect(this.otherResource.getId()).andReturn("other").atLeastOnce();
        expect(this.duplicateResult.getResourceResult()).andReturn(this.duplicateResource).atLeastOnce();
        expect(this.duplicateResource.getId()).andReturn("duplicate").atLeastOnce();
        replay(this.anotherResult, this.duplicateResult, this.otherResult, this.otherResource, this.scopusResult,
                this.scopusResource, this.duplicateResource);
        this.deduplicator.removeDuplicates(this.searchResults);
        assertFalse(this.searchResults.contains(this.scopusResult));
        verify(this.anotherResult, this.otherResource, this.scopusResource, this.duplicateResource, this.otherResult,
                this.scopusResult, this.duplicateResult);
    }
}
