/**
 * 
 */
package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.suggest.MeshSuggestionManager;

/**
 * @author ryanmax
 */
public class SuggestionReaderTest {

    private MeshSuggestionManager meshSuggestionManager;

    private ByteArrayOutputStream outputStream;

    private Parameters params;

    private SuggestionReader reader;
    
    private ObjectModel objectModel;
    
    private Map laneweb;

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
        this.objectModel = createMock(ObjectModel.class);
        this.laneweb = createMock(Map.class);
        expect(this.objectModel.get("laneweb")).andReturn(this.laneweb);
        replay(this.objectModel);
        this.reader.setObjectModel(this.objectModel);
        reset(this.objectModel);
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.suggestion.SuggestionReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerate() throws IOException {
        expect(this.laneweb.get(LanewebObjectModel.QUERY)).andReturn("venous thrombosis");
        expect(this.params.getParameter("limit", null)).andReturn("mesh");
        replayMocks();
        this.reader.setup(null, null, null, this.params);
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
        expect(this.laneweb.get(LanewebObjectModel.QUERY)).andReturn("asdfgh");
        expect(this.params.getParameter("limit", null)).andReturn("mesh");
        replayMocks();
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"suggest\":[]}", new String(this.outputStream.toByteArray()));
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
    
    private void replayMocks() {
        replay(this.params);
        replay(this.objectModel);
        replay(this.laneweb);
    }
    
    private void verifyMocks() {
        verify(this.params);
        verify(this.objectModel);
        verify(this.laneweb);
    }
}
