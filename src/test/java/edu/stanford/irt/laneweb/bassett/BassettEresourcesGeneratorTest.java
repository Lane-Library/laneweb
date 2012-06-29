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

import edu.stanford.irt.cocoon.xml.XMLizingStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class BassettEresourcesGeneratorTest {

    private BassettCollectionManager collectionManager;

    private BassettEresourcesGenerator generator;

    private XMLConsumer xmlConsumer;

    private XMLizingStrategy<Collection<BassettEresource>> xmlizingStrategy;

    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(BassettCollectionManager.class);
        this.xmlizingStrategy = createMock(XMLizingStrategy.class);
        this.generator = new BassettEresourcesGenerator(this.collectionManager, this.xmlizingStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateBassettNumber() {
        expect(this.collectionManager.getById("bn")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.BASSETT_NUMBER, "bn"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
    }

    @Test
    public void testDoGenerateNull() {
        this.xmlizingStrategy.toSAX(Collections.<BassettEresource> emptySet(), this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
    }

    @Test
    public void testDoGenerateQuery() {
        expect(this.collectionManager.search("query")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
    }

    @Test
    public void testDoGenerateQueryRegion() {
        expect(this.collectionManager.searchRegion("region", "query")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
    }

    @Test
    public void testDoGenerateRegion() {
        expect(this.collectionManager.getRegion("region")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.REGION, "region"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlConsumer, this.xmlizingStrategy);
    }
}
