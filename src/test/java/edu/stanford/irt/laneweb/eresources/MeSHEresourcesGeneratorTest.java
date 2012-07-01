package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;

public class MeSHEresourcesGeneratorTest {

    private CollectionManager collectionManager;

    private MeSHEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MeSHEresourcesGenerator("type", this.collectionManager, this.saxStrategy);
    }

    @Test
    public void testGetEresourceList() {
        this.generator.type = "type";
        this.generator.mesh = "mesh";
        expect(this.collectionManager.getMesh("type", "mesh")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList();
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNullMesh() {
        this.generator.type = "type";
        replay(this.collectionManager);
        assertEquals(0, this.generator.getEresourceList().size());
        verify(this.collectionManager);
    }

    @Test
    public void testGetEresourceListNullType() {
        this.generator.mesh = "mesh";
        replay(this.collectionManager);
        assertEquals(0, this.generator.getEresourceList().size());
        verify(this.collectionManager);
    }
}
