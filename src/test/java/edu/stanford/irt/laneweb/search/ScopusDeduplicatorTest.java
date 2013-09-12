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

import edu.stanford.irt.search.Result;

public class ScopusDeduplicatorTest {

    private ContentResultSearchResult anotherResult;

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
    }

    @Test
    public void testRemoveDuplicates() {
        this.searchResults.add(this.duplicateResult);
        this.searchResults.add(this.otherResult);
        this.searchResults.add(this.scopusResult);
        this.searchResults.add(this.anotherResult);
        expect(this.otherResult.getResourceResult()).andReturn(this.otherResource).atLeastOnce();
        expect(this.anotherResult.getResourceResult()).andReturn(this.otherResource).atLeastOnce();
        expect(this.otherResource.getId()).andReturn("other").atLeastOnce();
        expect(this.scopusResult.getResourceResult()).andReturn(this.scopusResource).atLeastOnce();
        expect(this.scopusResource.getId()).andReturn("scopus").atLeastOnce();
        expect(this.duplicateResult.getResourceResult()).andReturn(this.duplicateResource).atLeastOnce();
        expect(this.duplicateResource.getId()).andReturn("duplicate").atLeastOnce();
        expect(this.scopusResult.getSortTitle()).andReturn("title");
        expect(this.otherResult.getSortTitle()).andReturn("other title").times(0, 2);
        expect(this.anotherResult.getSortTitle()).andReturn("other title").times(0, 2);
        expect(this.duplicateResult.getSortTitle()).andReturn("title");
        replay(this.anotherResult, this.duplicateResult, this.otherResult, this.otherResource, this.scopusResult,
                this.scopusResource, this.duplicateResource);
        this.deduplicator.removeDuplicates(this.searchResults);
        assertFalse(this.searchResults.contains(this.scopusResult));
        verify(this.anotherResult, this.otherResource, this.scopusResource, this.duplicateResource, this.otherResult,
                this.scopusResult, this.duplicateResult);
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
