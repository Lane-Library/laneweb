package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

public class SuggestionControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SuggestionController controller;

    private SuggestionManager eresource;

    private SuggestionManager mesh;

    @Before
    public void setUp() throws Exception {
        this.eresource = createMock(SuggestionManager.class);
        this.mesh = createMock(SuggestionManager.class);
        this.controller = new SuggestionController(this.eresource, this.mesh);
    }

    @Test
    public void testGetSuggestions() throws IOException {
        Suggestion suggestion = createMock(Suggestion.class);
        expect(suggestion.getSuggestionTitle()).andReturn("Venous Thrombosis").atLeastOnce();
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(suggestion, this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "mesh");
        assertEquals("Venous Thrombosis", suggestions.get("suggest").get(0));
        verify(suggestion, this.eresource, this.mesh);
    }

    @Test
    public void testGetSuggestionsReturnLimit() throws IOException {
        Collection<Suggestion> collection = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            collection.add(new Suggestion(Integer.toString(i), Integer.toString(i)));
        }
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(collection);
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "mesh");
        assertEquals(10, suggestions.get("suggest").size());
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testHandleException() {
        IllegalArgumentException ex = new IllegalArgumentException();
        Map<String, List<String>> result = this.controller.handleIllegalArgumentException(ex);
        assertEquals(Collections.emptyList(), result.get("suggest"));
    }

    @Test
    public void testInternalGetSuggestionsBassett() {
        Suggestion suggestion = new Suggestion("1", "1");
        Collection<Suggestion> collection = new ArrayList<>();
        collection.add(suggestion);
        expect(this.eresource.getSuggestionsForTerm("Bassett", "venous thrombosis"))
                .andReturn(Collections.singleton(suggestion));
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "Bassett");
        assertTrue(suggestions.get("suggest").contains(suggestion.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsBogusLimit() {
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "bogus");
        assertEquals(0, suggestions.get("suggest").size());
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsEjMesh() {
        Suggestion suggestion1 = new Suggestion("1", "1");
        Suggestion suggestion2 = new Suggestion("2", "2");
        Collection<Suggestion> collection = new ArrayList<>();
        collection.add(suggestion1);
        expect(this.eresource.getSuggestionsForTerm("ej", "venous thrombosis")).andReturn(collection);
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion2));
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "ej-mesh");
        assertTrue(suggestions.get("suggest").contains(suggestion1.getSuggestionTitle()));
        assertTrue(suggestions.get("suggest").contains(suggestion2.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsErMesh() {
        Suggestion suggestion1 = new Suggestion("1", "1");
        Suggestion suggestion2 = new Suggestion("2", "2");
        Collection<Suggestion> collection = new ArrayList<>();
        collection.add(suggestion1);
        expect(this.eresource.getSuggestionsForTerm("venous thrombosis")).andReturn(collection);
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion2));
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "er-mesh");
        assertTrue(suggestions.get("suggest").contains(suggestion1.getSuggestionTitle()));
        assertTrue(suggestions.get("suggest").contains(suggestion2.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsMeshI() {
        Suggestion suggestion = new Suggestion("1", "1");
        Collection<Suggestion> collection = new ArrayList<>();
        collection.add(suggestion);
        expect(this.mesh.getSuggestionsForTerm("i", "venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(this.eresource, this.mesh);
        Map<String, List<String>> suggestions = this.controller.getSuggestions("venous thrombosis", "mesh-i");
        assertTrue(suggestions.get("suggest").contains(suggestion.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testMaxQuerySize() {
        String ninetyNineChars = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        expect(this.eresource.getSuggestionsForTerm(ninetyNineChars)).andReturn(Collections.emptyList());
        replay(this.eresource);
        this.controller.getSuggestions(ninetyNineChars, null);
        verify(this.eresource);
        reset(this.eresource);
        String oneOone = ninetyNineChars + "01";
        expect(this.eresource.getSuggestionsForTerm(oneOone)).andReturn(Collections.emptyList());
        replay(this.eresource);
        this.thrown.expect(AssertionError.class);
        this.thrown.expectMessage("expected: 1, actual: 0");
        this.controller.getSuggestions(oneOone, null);
        verify(this.eresource);
    }

    @Test
    public void testMinQuerySize() {
        assertEquals(0, this.controller.getSuggestions("12", "").get("suggest").size());
    }

    @Test
    public void testNullLimit() {
        expect(this.eresource.getSuggestionsForTerm("query")).andReturn(Collections.emptyList());
        replay(this.eresource, this.mesh);
        assertNotNull(this.controller.getSuggestions("query", null));
        verify(this.eresource, this.mesh);
    }
}
