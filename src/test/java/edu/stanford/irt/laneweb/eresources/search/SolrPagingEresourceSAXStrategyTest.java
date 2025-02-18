package edu.stanford.irt.laneweb.eresources.search;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.text.IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.RestResult;

public class SolrPagingEresourceSAXStrategyTest {

    private Eresource eresource;

    private SAXStrategy<Eresource> eresourceStrategy;

    private Page<Eresource> page;

    private RestResult<Eresource> result;

    private SolrPagingEresourceSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.eresourceStrategy = mock(SAXStrategy.class);
        this.strategy = new SolrPagingEresourceSAXStrategy(this.eresourceStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.page = mock(Page.class);
        this.eresource = mock(Eresource.class);
        this.result = mock(RestResult.class);
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
        assertTrue(equalToCompressingWhiteSpace(
                this.xmlConsumer.getExpectedResult(this, "SolrPagingEresourceSAXStrategyTest-testToSAX.xml"))
                .matches(this.xmlConsumer.getStringValue()));
        verify(this.page, this.result);
    }
}
