package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.suggest.Suggestion;

public class EresourceSuggestionManagerTest {

    private Eresource eresource;

    private CollectionManager eresources;

    private EresourceSuggestionManager suggestions;

    @Before
    public void setUp() {
        this.eresources = createMock(CollectionManager.class);
        this.suggestions = new EresourceSuggestionManager(this.eresources);
        this.eresource = createMock(Eresource.class);
    }

    @Test
    public void testGetSuggestionsForTerm() {
        expect(this.eresources.search("term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getId()).andReturn("id");
        expect(this.eresource.getTitle()).andReturn("title");
        replay(this.eresources, this.eresource);
        Suggestion suggestion = this.suggestions.getSuggestionsForTerm("term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.eresources, this.eresource);
    }

    @Test
    public void testGetSuggestionsForTermType() {
        expect(this.eresources.searchType("type", "term")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getId()).andReturn("id");
        expect(this.eresource.getTitle()).andReturn("title");
        replay(this.eresources, this.eresource);
        Suggestion suggestion = this.suggestions.getSuggestionsForTerm("type", "term").iterator().next();
        assertEquals("title", suggestion.getSuggestionTitle());
        verify(this.eresources, this.eresource);
    }
}
