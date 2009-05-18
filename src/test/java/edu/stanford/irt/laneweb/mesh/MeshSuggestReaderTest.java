/**
 * 
 */
package edu.stanford.irt.laneweb.mesh;

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

import edu.stanford.irt.lane.mesh.export.MeshDescriptorManager;

/**
 * @author ryanmax
 */
public class MeshSuggestReaderTest {

    private MeshDescriptorManager meshDescriptorManager;

    private ByteArrayOutputStream outputStream;

    private Parameters params;

    private MeshSuggestReader reader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.reader = new MeshSuggestReader();
        this.meshDescriptorManager = new MeshDescriptorManager();
        this.reader.setMeshDescriptorManager(this.meshDescriptorManager);
        this.outputStream = new ByteArrayOutputStream();
        this.reader.setOutputStream(this.outputStream);
        this.params = createMock(Parameters.class);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.mesh.MeshSuggestReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerate() throws IOException {
        expect(this.params.getParameter("query", null)).andReturn("dvt");
        expect(this.params.getParameter("limit", null)).andReturn(null);
        replay(this.params);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"mesh\":[\"Venous Thrombosis\"]}", new String(this.outputStream.toByteArray()));
        verify(this.params);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.mesh.MeshSuggestReader#generate()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGenerateNull() throws IOException {
        expect(this.params.getParameter("query", null)).andReturn("asdfgh");
        expect(this.params.getParameter("limit", null)).andReturn("p");
        replay(this.params);
        this.reader.setup(null, null, null, this.params);
        this.reader.generate();
        assertEquals("{\"mesh\":[]}", new String(this.outputStream.toByteArray()));
        verify(this.params);
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.mesh.MeshSuggestReader#setMeshDescriptorManager(edu.stanford.irt.lane.mesh.export.MeshDescriptorManager)}
     * .
     */
    @Test
    public void testSetMeshDescriptorManager() {
        assertNotNull(this.meshDescriptorManager);
    }
}
