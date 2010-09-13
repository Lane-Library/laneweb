/**
 * 
 */
package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.suggest.EresourceSuggestionManager;
import edu.stanford.irt.suggest.HistorySuggestionManager;
import edu.stanford.irt.suggest.MeshSuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionReaderTest {

    private HistorySuggestionManager historySuggestionManager;
    
    private EresourceSuggestionManager eresourceSuggestionManager;
    
    private MeshSuggestionManager meshSuggestionManager;

    private Model model;

    private ByteArrayOutputStream outputStream;

    private SuggestionReader reader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.reader = new SuggestionReader();
        this.historySuggestionManager = new HistorySuggestionManager();
        this.eresourceSuggestionManager = new EresourceSuggestionManager();
        this.meshSuggestionManager = new MeshSuggestionManager();
        this.reader.setHistorySuggestionManager(this.historySuggestionManager);
        this.reader.setEresourceSuggestionManager(this.eresourceSuggestionManager);
        this.reader.setMeshSuggestionManager(this.meshSuggestionManager);
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
        expect(this.model.getString(Model.CALLBACK, "")).andReturn("");
        expect(this.model.getString(Model.QUERY)).andReturn("venous thrombosis");
        expect(this.model.getString(Model.LIMIT, "")).andReturn("mesh");
        replayMocks();
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("{\"suggest\":[\"Venous Thrombosis\"]}", new String(this.outputStream.toByteArray()));
        verifyMocks();
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
        replayMocks();
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("{\"suggest\":[]}", new String(this.outputStream.toByteArray()));
        verifyMocks();
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
        replayMocks();
        this.reader.setup(null, null, null, null);
        this.reader.generate();
        assertEquals("foo({\"suggest\":[]});", new String(this.outputStream.toByteArray()));
        verifyMocks();
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#setMeshSuggestionManager(edu.stanford.irt.lane.suggest.MeshSuggestionManager)}
     * .
     */
    @Test
    public void testSetMeshSuggestionManager() {
        assertNotNull(this.meshSuggestionManager);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggest.SuggestionReader#setEresourceSuggestionManager(edu.stanford.irt.suggest.SuggestionManager)}.
     */
    @Test
    public void testSetEresourceSuggestionManager() {
        assertNotNull(this.eresourceSuggestionManager);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggest.SuggestionReader#setHistorySuggestionManager(edu.stanford.irt.suggest.SuggestionManager)}.
     */
    @Test
    public void testSetHistorySuggestionManager() {
        assertNotNull(this.historySuggestionManager);
    }

    private void replayMocks() {
        replay(this.model);
    }

    private void verifyMocks() {
        verify(this.model);
    }
}
