package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractEresourcesGeneratorTest {

    private static final class TestAbstractEresourcesGenerator extends AbstractEresourcesGenerator {

        private Collection<Eresource> eresourceList;

        public TestAbstractEresourcesGenerator(final CollectionManager collectionManager,
                final SAXStrategy<PagingEresourceList> saxStrategy, final Collection<Eresource> eresourceList) {
            super("type", collectionManager, saxStrategy);
            this.eresourceList = eresourceList;
        }

        @Override
        protected Collection<Eresource> getEresourceList() {
            return this.eresourceList;
        }
    }

    private CollectionManager collectionManager;

    private Eresource eresource;

    private Collection<Eresource> eresourceList;

    private AbstractEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.eresource = createMock(Eresource.class);
        this.eresourceList = Collections.singleton(this.eresource);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.generator = new TestAbstractEresourcesGenerator(this.collectionManager, this.saxStrategy, this.eresourceList);
    }

    @Test
    public void testDoGenerate() {
        this.saxStrategy.toSAX(isA(PagingEresourceList.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.eresource, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.eresource, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetCore() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getCore("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getCore();
        verify(this.collectionManager);
    }

    @Test
    public void testGetCoreType() {
        assertEquals(0, this.generator.getCore().size());
    }

    @Test
    public void testGetKey() {
        assertEquals("t=;s=;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testGetKeyAgain() {
        Serializable key = this.generator.getKey();
        assertTrue(key == this.generator.getKey());
    }

    @Test
    public void testGetMesh() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getMesh("type", "mesh")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getMesh();
        verify(this.collectionManager);
    }

    @Test
    public void testGetMeshNullMesh() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        replay(this.collectionManager);
        assertEquals(0, this.generator.getMesh().size());
        verify(this.collectionManager);
    }

    @Test
    public void testGetMeshNullType() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        replay(this.collectionManager);
        assertEquals(0, this.generator.getMesh().size());
        verify(this.collectionManager);
    }

    /*
     * @Test public void testGetKeyWithValues() { this.generator.type = "type";
     * this.generator.subset = "subset"; this.generator.alpha = "alpha";
     * this.generator.mesh = "mesh"; this.generator.page = 1;
     * assertEquals("t=type;s=subset;a=alpha;m=mesh;page=1",
     * this.generator.getKey()); }
     */
    @Test
    public void testGetType() {
        assertEquals("type", this.generator.getType());
    }

    @Test
    public void testGetTypeOrSubsetNull() {
        replay(this.collectionManager);
        assertEquals(0, this.generator.getTypeOrSubset().size());
        verify(this.collectionManager);
    }

    @Test
    public void testGetTypeOrSubsetSubset() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.SUBSET, "subset"));
        expect(this.collectionManager.getSubset("subset")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getTypeOrSubset();
        verify(this.collectionManager);
    }

    @Test
    public void testGetTypeOrSubsetType() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getTypeOrSubset();
        verify(this.collectionManager);
    }

    @Test
    public void testGetTypeOrSubsetTypeAlpha() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "a"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        expect(this.collectionManager.getType("type", 'a')).andReturn(null);
        replay(this.collectionManager);
        this.generator.getTypeOrSubset();
        verify(this.collectionManager);
    }

    @Test
    public void testGetValidity() {
        assertEquals(SourceValidity.VALID, this.generator.getValidity().isValid());
    }

    @Test
    public void testGetValidityAgain() {
        SourceValidity validity = this.generator.getValidity();
        assertTrue(validity == this.generator.getValidity());
    }

    @Test
    public void testSetExpires() {
        this.generator.setExpires(-1);
        assertEquals(SourceValidity.INVALID, this.generator.getValidity().isValid());
    }

    @Test
    public void testSetModelAlpha() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "a"));
        assertEquals("t=;s=;a=a;m=;page=0", this.generator.getKey());
    }

    @Test
    // TODO: probably should not use alpha = null for all
    public void testSetModelAlphaAll() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "all"));
        assertEquals("t=;s=;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelAlphaString() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ALPHA, "a String"));
        assertEquals("t=;s=;a=a;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelMesh() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.MESH, "mesh"));
        assertEquals("t=;s=;a=;m=mesh;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelNumberFormatException() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "foo"));
        assertEquals("t=;s=;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelPage() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "1"));
        assertEquals("t=;s=;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelPageAll() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "all"));
        assertEquals("t=;s=;a=;m=;page=-1", this.generator.getKey());
    }

    @Test
    public void testSetModelSubset() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.SUBSET, "subset"));
        assertEquals("t=;s=subset;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetModelType() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TYPE, "type"));
        assertEquals("t=type;s=;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetParameters() {
        this.generator.setParameters(Collections.singletonMap(Model.EXPIRES, "-1"));
        assertEquals(SourceValidity.INVALID, this.generator.getValidity().isValid());
    }

    @Test
    public void testSetParametersNoValidity() {
        this.generator.setParameters(Collections.<String, String> emptyMap());
        assertEquals(SourceValidity.VALID, this.generator.getValidity().isValid());
    }

    @Test
    public void testSetParametersSubset() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.SUBSET, "subset"));
        assertEquals("t=;s=subset;a=;m=;page=0", this.generator.getKey());
    }

    @Test
    public void testSetParametersType() {
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        assertEquals("t=type;s=;a=;m=;page=0", this.generator.getKey());
    }
}
