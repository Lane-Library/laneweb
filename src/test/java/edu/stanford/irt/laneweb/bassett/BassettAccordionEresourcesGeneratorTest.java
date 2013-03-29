package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class BassettAccordionEresourcesGeneratorTest {

    private BassettCollectionManager collectionManager;

    private BassettAccordionEresourcesGenerator generator;

    private SAXStrategy<Map<String, Integer>> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(BassettCollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BassettAccordionEresourcesGenerator(this.collectionManager, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.collectionManager.searchCount("query")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.collectionManager, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.saxStrategy, this.xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoGenerateEmptyQuery() {
        this.saxStrategy.toSAX(isA(Map.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.saxStrategy, this.xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoGenerateNullQuery() {
        this.saxStrategy.toSAX(isA(Map.class), eq(this.xmlConsumer));
        replay(this.collectionManager, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.collectionManager, this.saxStrategy, this.xmlConsumer);
    }
}
