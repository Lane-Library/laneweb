package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.solr.SolrSearchService;
import edu.stanford.irt.suggest.Suggestion;

public class SolrSuggestionManagerTest {

    private Eresource eresource;

    private SolrSuggestionManager manager;

    private SolrSearchService solrSearchService;

    @Before
    public void setUp() {
        this.solrSearchService = createMock(SolrSearchService.class);
        this.manager = new SolrSuggestionManager(this.solrSearchService);
        this.eresource = createMock(Eresource.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuggestionsForNullTerm() {
        this.manager.getSuggestionsForTerm(null);
    }

    @Test
    public void testGetSuggestionsForTerm() {
        expect(this.solrSearchService.suggestFindAll("term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrSearchService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrSearchService, this.eresource);
    }

    @Test
    public void testGetSuggestionsForTermType() {
        expect(this.solrSearchService.suggestFindByType("term", "type")).andReturn(
                Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrSearchService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("type", "term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrSearchService, this.eresource);
    }

    @Test
    public void testGetSuggestionsForTermWithPeriod() {
        expect(this.solrSearchService.suggestFindAll("term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title.");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrSearchService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrSearchService, this.eresource);
    }
}
