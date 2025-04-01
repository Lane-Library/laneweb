package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.suggest.SuggestionService;

public class ExtensionsSuggestControllerTest {

    private ExtensionsSuggestController controller;

    private SuggestionService suggestionService;

    @BeforeEach
    public void setUp() throws Exception {
        this.suggestionService = mock(SuggestionService.class);
        this.controller = new ExtensionsSuggestController(this.suggestionService);
    }

    @Test
    public void testGetFewerSuggestions() {
        String[] suggestions = new String[5];
        Arrays.fill(suggestions, "suggestion");
        expect(this.suggestionService.getSuggestions("query", "er-mesh")).andReturn(Arrays.asList(suggestions));
        replay(this.suggestionService);
        List<Object> result = this.controller.getSuggestions("query");
        assertEquals("query", result.get(0));
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) result.get(1);
        assertEquals(5, list.size());
        verify(this.suggestionService);
    }
}
