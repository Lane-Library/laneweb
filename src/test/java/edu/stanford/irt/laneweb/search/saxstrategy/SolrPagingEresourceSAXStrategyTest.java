package edu.stanford.irt.laneweb.search.saxstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.search.SearchResult;
import edu.stanford.irt.solr.Image;

public class SolrPagingEresourceSAXStrategyTest {

    private SolrPagingEresourceSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    private SAXStrategy<Eresource> eresourceStrategy;

    private Eresource eresource;

    private Page<Eresource> page;

    private Map<String, Object> objectMap;

    @Before
    public void setUp() throws Exception {
        this.eresourceStrategy = createMock(SAXStrategy.class);
        this.strategy = new SolrPagingEresourceSAXStrategy(this.eresourceStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.page = createMock(Page.class);
        this.eresource = createMock(Eresource.class);
        this.objectMap = createMock(Map.class);
    }

    @Test
    public final void testToSAX() throws Exception {
        expect(this.objectMap.get("resultPage")).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.eresource));
        expect(this.objectMap.get("searchTerm")).andReturn("query");
        expect(this.page.getSize()).andReturn(0);
        expect(this.page.getNumber()).andReturn(0);
        expect(this.page.getTotalElements()).andReturn(0L);
        expect(this.page.getTotalPages()).andReturn(0);
        replay(this.page, this.objectMap);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.objectMap, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrPagingEresourceSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.objectMap);
    }
}
