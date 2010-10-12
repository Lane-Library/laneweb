/**
 * 
 */
package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.suggest.Suggestion;
import edu.stanford.irt.suggest.SuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionReaderTest {

    private SuggestionManager history;
    
    private SuggestionManager eresource;
    
    private SuggestionManager mesh;

    private Model model;

    private ByteArrayOutputStream outputStream;

    private SuggestionReader reader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.reader = new SuggestionReader();
      this.history = createMock(SuggestionManager.class);
      this.eresource = createMock(SuggestionManager.class);
      this.mesh = createMock(SuggestionManager.class);
        this.reader.setHistorySuggestionManager(this.history);
        this.reader.setEresourceSuggestionManager(this.eresource);
        this.reader.setMeshSuggestionManager(this.mesh);
        this.outputStream = new ByteArrayOutputStream();
        this.reader.setOutputStream(this.outputStream);
        this.model = createMock(Model.class);
        this.reader.setModel(this.model);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerate() throws IOException {
        Suggestion suggestion = createMock(Suggestion.class);
        expect(suggestion.getSuggestionTitle()).andReturn("Venous Thrombosis");
        expect(this.model.getString(Model.CALLBACK, "")).andReturn("");
        expect(this.model.getString(Model.QUERY)).andReturn("venous thrombosis");
        expect(this.model.getString(Model.LIMIT, "")).andReturn("mesh");
        expect(this.mesh.getSuggestionsForTerm("venous thrombosis")).andReturn(Collections.singleton(suggestion));
        replay(suggestion, this.eresource, this.history, this.mesh, this.model);
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("{\"suggest\":[\"Venous Thrombosis\"]}", new String(this.outputStream.toByteArray()));
        verify(suggestion, this.eresource, this.history, this.mesh, this.model);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerateNull() throws IOException {
        expect(this.model.getString(Model.CALLBACK, "")).andReturn("");
        expect(this.model.getString(Model.QUERY)).andReturn("asdfgh");
        expect(this.model.getString(Model.LIMIT, "")).andReturn("mesh");
        expect(this.mesh.getSuggestionsForTerm("asdfgh")).andReturn(Collections.<Suggestion>emptyList());
        replay(this.eresource, this.history, this.mesh, this.model);
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("{\"suggest\":[]}", new String(this.outputStream.toByteArray()));
        verify(this.eresource, this.history, this.mesh, this.model);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerateCallback() throws IOException {
        expect(this.model.getString(Model.CALLBACK, "")).andReturn("foo");
        expect(this.model.getString(Model.QUERY)).andReturn("asdfgh");
        expect(this.model.getString(Model.LIMIT, "")).andReturn("mesh");
        expect(this.mesh.getSuggestionsForTerm("asdfgh")).andReturn(Collections.<Suggestion>emptyList());
        replay(this.eresource, this.history, this.mesh, this.model);
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("foo({\"suggest\":[]});", new String(this.outputStream.toByteArray()));
        verify(this.eresource, this.history, this.mesh, this.model);
    }
}
