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

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.suggest.MeshSuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionReaderTest {

    private MeshSuggestionManager meshSuggestionManager;

    private ByteArrayOutputStream outputStream;

    private Parameters params;

    private SuggestionReader reader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.reader = new SuggestionReader();
        this.meshSuggestionManager = new MeshSuggestionManager();
        this.reader.setMeshSuggestionManager(this.meshSuggestionManager);
        this.outputStream = new ByteArrayOutputStream();
        this.reader.setOutputStream(this.outputStream);
        this.params = createMock(Parameters.class);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerate() throws IOException {
        expect(this.params.getParameter("query", null)).andReturn("venous thrombosis");
        expect(this.params.getParameter("limit", null)).andReturn("mesh");
        replay(this.params);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"suggest\":[\"Venous Thrombosis\"]}", new String(this.outputStream.toByteArray()));
        verify(this.params);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerateNull() throws IOException {
        expect(this.params.getParameter("query", null)).andReturn("asdfgh");
        expect(this.params.getParameter("limit", null)).andReturn("mesh");
        replay(this.params);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"suggest\":[]}", new String(this.outputStream.toByteArray()));
        verify(this.params);
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
}
