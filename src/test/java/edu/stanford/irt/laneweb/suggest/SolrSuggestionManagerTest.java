package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.suggest.Suggestion;

public class SolrSuggestionManagerTest {

    private Eresource eresource;

    private SolrSuggestionManager manager;

    private SolrService solrService;

    @Before
    public void setUp() {
        this.solrService = createMock(SolrService.class);
        this.manager = new SolrSuggestionManager(this.solrService);
        this.eresource = createMock(Eresource.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuggestionsForNullTerm() {
        this.manager.getSuggestionsForTerm(null);
    }

    @Test
    public void testGetSuggestionsForTerm() {
        expect(this.solrService.suggestFindAll("term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrService, this.eresource);
    }

    @Test
    public void testGetSuggestionsForTermType() {
        expect(this.solrService.suggestFindByType("term", "type")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("type", "term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrService, this.eresource);
    }

    @Test
    public void testGetSuggestionsForTermWithPeriod() {
        expect(this.solrService.suggestFindAll("term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title.");
        expect(this.eresource.getId()).andReturn("id");
        replay(this.solrService, this.eresource);
        Suggestion suggestion = this.manager.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.solrService, this.eresource);
    }
}
