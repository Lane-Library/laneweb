package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class ExtensionsSuggestControllerTest {

    private ExtensionsSuggestController controller;

    private Suggestion suggestion;

    private SuggestionManager suggestionManager;

    @Before
    public void setUp() throws Exception {
        this.suggestionManager = createMock(SuggestionManager.class);
        this.controller = new ExtensionsSuggestController(this.suggestionManager);
        this.suggestion = createMock(Suggestion.class);
    }

    @Test
    public void testGetFewerSuggestions() {
        Suggestion[] suggestions = new Suggestion[5];
        Arrays.fill(suggestions, this.suggestion);
        expect(this.suggestionManager.getSuggestionsForTerm("query")).andReturn(Arrays.asList(suggestions));
        expect(this.suggestion.getSuggestionTitle()).andReturn("title").times(5);
        replay(this.suggestionManager, this.suggestion);
        List<Object> result = this.controller.getSuggestions("query");
        assertEquals("query", result.get(0));
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) result.get(1);
        assertEquals(5, list.size());
        verify(this.suggestionManager, this.suggestion);
    }

    @Test
    public void testGetSuggestions() {
        Suggestion[] suggestions = new Suggestion[20];
        Arrays.fill(suggestions, this.suggestion);
        expect(this.suggestionManager.getSuggestionsForTerm("query")).andReturn(Arrays.asList(suggestions));
        expect(this.suggestion.getSuggestionTitle()).andReturn("title").times(10);
        replay(this.suggestionManager, this.suggestion);
        List<Object> result = this.controller.getSuggestions("query");
        assertEquals("query", result.get(0));
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) result.get(1);
        assertEquals(10, list.size());
        verify(this.suggestionManager, this.suggestion);
    }
}
