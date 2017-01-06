package edu.stanford.irt.laneweb.eresources.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;

public class SolrPagingEresourceSAXStrategyTest {

    private Eresource eresource;

    private SAXStrategy<Eresource> eresourceStrategy;

    private Page<Eresource> page;

    private SolrSearchResult result;

    private SolrPagingEresourceSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.eresourceStrategy = createMock(SAXStrategy.class);
        this.strategy = new SolrPagingEresourceSAXStrategy(this.eresourceStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.page = createMock(Page.class);
        this.eresource = createMock(Eresource.class);
        this.result = createMock(SolrSearchResult.class);
    }

    @Test
    public final void testToSAX() throws Exception {
        expect(this.result.getPage()).andReturn(this.page);
        expect(this.page.getContent()).andReturn(Collections.singletonList(this.eresource));
        expect(this.result.getQuery()).andReturn("query");
        expect(this.page.getSize()).andReturn(0);
        expect(this.page.getNumber()).andReturn(0);
        expect(this.page.getTotalElements()).andReturn(0L);
        expect(this.page.getTotalPages()).andReturn(0);
        replay(this.page, this.result);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.result, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SolrPagingEresourceSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.page, this.result);
    }
}
