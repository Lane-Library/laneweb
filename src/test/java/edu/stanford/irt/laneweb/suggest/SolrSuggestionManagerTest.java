package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.suggest.Suggestion;

public class SolrSuggestionManagerTest {

    private SolrSuggestionManager manager;

    private EresourceSearchService searchService;
    
    Map<String, String> result;

    @Before
    public void setUp() {
        this.searchService = mock(EresourceSearchService.class);
        this.manager = new SolrSuggestionManager(this.searchService);
        this.result = new HashMap<>();
        this.result.put("id", "title");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuggestionsForNullTerm() {
        this.manager.getSuggestionsForTerm(null);
    }

    @Test
    public void testGetSuggestionsForTerm() {
        expect(this.searchService.suggestFindAll("term")).andReturn(result);
        replay(this.searchService);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.searchService);
    }

    @Test
    public void testGetSuggestionsForTermType() {
        expect(this.searchService.suggestFindByType("term", "type")).andReturn(result);
        replay(this.searchService);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("type", "term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.searchService);
    }

    @Test
    public void testGetSuggestionsForTermWithPeriod() {
        
        
        expect(this.searchService.suggestFindAll("term")).andReturn(result);
        replay(this.searchService);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.searchService);
    }
}
