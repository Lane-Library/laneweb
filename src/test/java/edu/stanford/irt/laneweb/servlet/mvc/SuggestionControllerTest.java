package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionControllerTest {

    private SuggestionController controller;

    private SuggestionManager eresource;

    private SuggestionManager history;

    private SuggestionManager mesh;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.controller = new SuggestionController();
        this.history = createMock(SuggestionManager.class);
        this.eresource = createMock(SuggestionManager.class);
        this.mesh = createMock(SuggestionManager.class);
        this.controller.setHistorySuggestionManager(this.history);
        this.controller.setEresourceSuggestionManager(this.eresource);
        this.controller.setMeshSuggestionManager(this.mesh);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.servlet.mvc.SuggestionController#getSuggestions()}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGetSuggestions() throws IOException {
        Suggestion suggestion = createMock(Suggestion.class);
        expect(suggestion.getSuggestionTitle()).andReturn("Venous Thrombosis").atLeastOnce(); //java 7 does this
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(suggestion, this.eresource, this.history, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "mesh");
        assertEquals("Venous Thrombosis", suggestions.get("suggest").get(0));
        verify(suggestion, this.eresource, this.history, this.mesh);
    }

    @Test
    public void testMaxQuerySize() {
        assertEquals(0, this.controller.getSuggestions("123456789012345678901234567890123", "").get("suggest").size());
    }

    @Test
    public void testMinQuerySize() {
        assertEquals(0, this.controller.getSuggestions("12", "").get("suggest").size());
    }

    @Test
    public void testNullLimit() {
        expect(this.eresource.getSuggestionsForTerm("query")).andReturn(Collections.<Suggestion> emptyList());
        replay(this.eresource, this.history, this.mesh);
        assertNotNull(this.controller.getSuggestions("query", null));
        verify(this.eresource, this.history, this.mesh);
    }
}
