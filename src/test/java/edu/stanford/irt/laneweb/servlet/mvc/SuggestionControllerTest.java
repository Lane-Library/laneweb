package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionControllerTest {

    private SuggestionManager eresource;

    private SuggestionManager history;

    private SuggestionManager mesh;

    private SuggestionController reader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.reader = new SuggestionController();
        this.history = createMock(SuggestionManager.class);
        this.eresource = createMock(SuggestionManager.class);
        this.mesh = createMock(SuggestionManager.class);
        this.reader.setHistorySuggestionManager(this.history);
        this.reader.setEresourceSuggestionManager(this.eresource);
        this.reader.setMeshSuggestionManager(this.mesh);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.SuggestionController.SuggestionReader#generate()}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGenerate() throws IOException {
        Suggestion suggestion = createMock(Suggestion.class);
        expect(suggestion.getSuggestionTitle()).andReturn("Venous Thrombosis");
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(suggestion, this.eresource, this.history, this.mesh);
        HttpEntity<String> response = this.reader.getSuggestions("venous thrombosis", "mesh", null);
        String suggestions = response.getBody();
        assertEquals("{\"suggest\":[\"Venous Thrombosis\"]}", suggestions);
        verify(suggestion, this.eresource, this.history, this.mesh);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.SuggestionController.SuggestionReader#generate()}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testGenerateCallback() throws IOException {
        expect(this.mesh.getSuggestionsForTerm("asdfgh")).andReturn(Collections.<Suggestion> emptyList());
        replay(this.eresource, this.history, this.mesh);
        HttpEntity<String> response = this.reader.getSuggestions("asdfgh", "mesh", "foo");
        String suggestions = response.getBody();
        assertEquals("foo({\"suggest\":[]});", suggestions);
        verify(this.eresource, this.history, this.mesh);
    }
}
