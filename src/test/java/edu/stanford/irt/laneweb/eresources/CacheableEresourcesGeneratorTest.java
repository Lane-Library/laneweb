package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;

public class CacheableEresourcesGeneratorTest {

    private static final class TestCacheableEresourcesGenerator extends CacheableEresourcesGenerator {

        public TestCacheableEresourcesGenerator(final CollectionManager collectionManager,
                final SAXStrategy<PagingEresourceList> saxStrategy) {
            super("type", collectionManager, saxStrategy);
        }

        @Override
        protected Collection<Eresource> getEresourceList() {
            return null;
        }
    }

    private CollectionManager collectionManager;

    private CacheableEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestCacheableEresourcesGenerator(this.collectionManager, this.saxStrategy);
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
    public void testGetKeyWithValues() {
        this.generator.type = "type";
        this.generator.subset = "subset";
        this.generator.alpha = "alpha";
        this.generator.mesh = "mesh";
        this.generator.page = 1;
        assertEquals("t=type;s=subset;a=alpha;m=mesh;page=1", this.generator.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("type", this.generator.getType());
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
    public void testSetParameters() {
        this.generator.setParameters(Collections.singletonMap(Model.EXPIRES, "-1"));
        assertEquals(SourceValidity.INVALID, this.generator.getValidity().isValid());
    }

    @Test
    public void testSetParametersNoValidity() {
        this.generator.setParameters(Collections.<String, String> emptyMap());
        assertEquals(SourceValidity.VALID, this.generator.getValidity().isValid());
    }
}
