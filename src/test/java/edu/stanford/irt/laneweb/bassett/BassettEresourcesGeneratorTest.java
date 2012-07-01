package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class BassettEresourcesGeneratorTest {

    private BassettCollectionManager collectionManager;

    private BassettEresourcesGenerator generator;

    private XMLConsumer xmlConsumer;

    private SAXStrategy<Collection<BassettEresource>> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(BassettCollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BassettEresourcesGenerator(this.collectionManager, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateBassettNumber() {
        expect(this.collectionManager.getById("bn")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.BASSETT_NUMBER, "bn"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateNull() {
        this.saxStrategy.toSAX(Collections.<BassettEresource> emptySet(), this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.saxStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateQuery() {
        expect(this.collectionManager.search("query")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateQueryRegion() {
        expect(this.collectionManager.searchRegion("region", "query")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.saxStrategy);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateRegion() {
        expect(this.collectionManager.getRegion("region")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.REGION, "region"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.saxStrategy);
    }
}
