package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.stanford.irt.suggest.MeshSuggestionManager;
import edu.stanford.irt.suggest.Suggestion;

public class DefaultSuggestionServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SolrSuggestionManager eresource;

    private MeshSuggestionManager mesh;

    private DefaultSuggestionService service;

    @Before
    public void setUp() throws Exception {
        this.eresource = mock(SolrSuggestionManager.class);
        this.mesh = mock(MeshSuggestionManager.class);
        this.service = new DefaultSuggestionService(this.eresource, this.mesh);
    }

    @Test
    public void testGetSuggestions() throws IOException {
        Suggestion suggestion = mock(Suggestion.class);
        expect(suggestion.getSuggestionTitle()).andReturn("Venous Thrombosis").atLeastOnce();
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(suggestion, this.eresource, this.mesh);
        Collection<String> suggestions = this.service.getSuggestions("venous thrombosis", "mesh");
        assertEquals("Venous Thrombosis", suggestions.stream().findFirst().orElse(null));
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
        Collection<String> suggestions = this.service.getSuggestions("venous thrombosis", "mesh");
        assertEquals(10, suggestions.size());
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsBogusLimit() {
        replay(this.eresource, this.mesh);
        Collection<String> suggestions = this.service.getSuggestions("venous thrombosis", "bogus");
        assertEquals(0, suggestions.size());
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
        Collection<String> suggestions = this.service.getSuggestions("venous thrombosis", "er-mesh");
        assertTrue(suggestions.contains(suggestion1.getSuggestionTitle()));
        assertTrue(suggestions.contains(suggestion2.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testInternalGetSuggestionsJournal() {
        Suggestion suggestion = new Suggestion("1", "JAMA");
        Collection<Suggestion> collection = new ArrayList<>();
        collection.add(suggestion);
        expect(this.eresource.getSuggestionsForTerm("Journal", "jama")).andReturn(Collections.singleton(suggestion));
        replay(this.eresource, this.mesh);
        Collection<String> suggestions = this.service.getSuggestions("jama", "Journal");
        assertTrue(suggestions.contains(suggestion.getSuggestionTitle()));
        verify(this.eresource, this.mesh);
    }

    @Test
    public void testMaxQuerySize() {
        String ninetyNineChars = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        expect(this.eresource.getSuggestionsForTerm(ninetyNineChars)).andReturn(Collections.emptyList());
        replay(this.eresource);
        this.service.getSuggestions(ninetyNineChars, null);
        verify(this.eresource);
        reset(this.eresource);
        String oneOone = ninetyNineChars + "01";
        expect(this.eresource.getSuggestionsForTerm(oneOone)).andReturn(Collections.emptyList());
        replay(this.eresource);
        this.thrown.expect(AssertionError.class);
        this.thrown.expectMessage("expected: 1, actual: 0");
        this.service.getSuggestions(oneOone, null);
        verify(this.eresource);
    }

    @Test
    public void testMinQuerySize() {
        assertEquals(0, this.service.getSuggestions("12", "").size());
    }

    @Test
    public void testNullLimit() {
        expect(this.eresource.getSuggestionsForTerm("query")).andReturn(Collections.emptyList());
        replay(this.eresource, this.mesh);
        assertNotNull(this.service.getSuggestions("query", null));
        verify(this.eresource, this.mesh);
    }
}
