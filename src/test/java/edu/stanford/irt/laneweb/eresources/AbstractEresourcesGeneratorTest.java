package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractEresourcesGeneratorTest {

    private static final class TestAbstractEresourcesGenerator extends AbstractEresourcesGenerator {

        private List<Eresource> eresourceList;

        public TestAbstractEresourcesGenerator(final CollectionManager collectionManager,
                final SAXStrategy<PagingEresourceList> saxStrategy, final List<Eresource> eresourceList) {
            super("type", collectionManager, saxStrategy);
            this.eresourceList = eresourceList;
        }

        @Override
        protected List<Eresource> getEresourceList(final CollectionManager collectionManager) {
            return this.eresourceList;
        }

        @Override
        protected String getHeading() {
            return "A";
        }
    }

    private CollectionManager collectionManager;

    private Eresource eresource;

    private List<Eresource> eresourceList;

    private AbstractEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.eresource = createMock(Eresource.class);
        this.eresourceList = Collections.singletonList(this.eresource);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.generator = new TestAbstractEresourcesGenerator(this.collectionManager, this.saxStrategy,
                this.eresourceList);
    }

    @Test
    public void testDoGenerate() {
        expect(this.eresource.getTitle()).andReturn("title").times(2);
        this.generator.setModel(Collections.<String, Object>emptyMap());
        this.saxStrategy.toSAX(isA(PagingEresourceList.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.eresource, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.eresource, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        assertEquals("p=0", this.generator.getKey());
    }

    @Test
    public void testGetKeyAgain() {
        Serializable key = this.generator.getKey();
        assertTrue(key == this.generator.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("type", this.generator.getType());
    }

    @Test
    public void testGetValidity() {
        assertTrue(this.generator.getValidity().isValid());
    }

    @Test
    public void testGetValidityAgain() {
        Validity validity = this.generator.getValidity();
        assertTrue(validity == this.generator.getValidity());
    }

    @Test
    public void testSetExpires() {
        this.generator.setExpires(-1);
        assertFalse(this.generator.getValidity().isValid());
    }

    @Test
    public void testSetModel() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "2"));
        assertEquals("p=1", this.generator.getKey());
    }

    @Test
    public void testSetModelAll() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "all"));
        assertEquals("p=-1", this.generator.getKey());
    }

    @Test
    public void testSetModelNumberFormatException() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "page"));
        assertEquals("p=0", this.generator.getKey());
    }

    @Test
    public void testSetParameters() {
        this.generator.setParameters(Collections.singletonMap(Model.EXPIRES, "-1"));
        assertFalse(this.generator.getValidity().isValid());
    }

    @Test
    public void testSetParametersNoValidity() {
        this.generator.setParameters(Collections.<String, String> emptyMap());
        assertTrue(this.generator.getValidity().isValid());
    }
}
