package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class MeSHEresourcesGeneratorTest {

    private CollectionManager collectionManager;

    private MeSHEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @Test
    public void setModelMesh() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        assertEquals("p=0;t=;m=mesh", this.generator.createKey().toString());
    }

    @Test
    public void setModelNull() {
        this.generator.setModel(Collections.<String, Object> emptyMap());
        assertEquals("p=0;t=;m=", this.generator.createKey().toString());
    }

    @Test
    public void setParametersNoType() {
        this.generator.setParameters(Collections.<String, String> emptyMap());
        assertEquals("p=0;t=;m=", this.generator.createKey().toString());
    }

    @Test
    public void setParametersType() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        assertEquals("p=0;t=type;m=", this.generator.createKey().toString());
    }

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MeSHEresourcesGenerator("type", this.collectionManager, this.saxStrategy);
    }

    @Test
    public void testGetEresourceList() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getMesh("type", "mesh")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList(this.collectionManager);
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNullMesh() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        replay(this.collectionManager);
        assertEquals(0, this.generator.getEresourceList(this.collectionManager).size());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNullType() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        replay(this.collectionManager);
        assertEquals(0, this.generator.getEresourceList(this.collectionManager).size());
        verify(this.collectionManager);
    }
}
