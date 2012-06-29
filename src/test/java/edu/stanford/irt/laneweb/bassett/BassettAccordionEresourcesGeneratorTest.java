package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.XMLizingStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class BassettAccordionEresourcesGeneratorTest {

    private BassettCollectionManager collectionManager;

    private BassettAccordionEresourcesGenerator generator;

    private XMLConsumer xmlConsumer;

    private XMLizingStrategy<Map<String, Integer>> xmlizingStrategy;

    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(BassettCollectionManager.class);
        this.xmlizingStrategy = createMock(XMLizingStrategy.class);
        this.generator = new BassettAccordionEresourcesGenerator(this.collectionManager, this.xmlizingStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.collectionManager.searchCount("query")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateEmptyQuery() {
        expect(this.collectionManager.searchCount("bassett")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() {
        expect(this.collectionManager.searchCount("bassett")).andReturn(null);
        this.xmlizingStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.xmlizingStrategy, this.xmlConsumer);
    }
}
