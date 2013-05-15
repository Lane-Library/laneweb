package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;

public class MergedSearchSAXStrategyTest {

    private ContentResultSearchResult contentResult;

    private SAXStrategy<ContentResultSearchResult> contentStrategy;

    private EresourceSearchResult eresourceResult;

    private SAXStrategy<Eresource> eresourceStrategy;

    private MergedSearchSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.contentStrategy = createMock(SAXStrategy.class);
        this.eresourceStrategy = createMock(SAXStrategy.class);
        this.contentResult = createMock(ContentResultSearchResult.class);
        this.eresourceResult = createMock(EresourceSearchResult.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.strategy = new MergedSearchSAXStrategy(this.contentStrategy, this.eresourceStrategy);
    }

    @Test
    public void testToSAXContentResult() {
        this.contentStrategy.toSAX(this.contentResult, this.xmlConsumer);
        replay(this.contentStrategy, this.eresourceStrategy, this.contentResult, this.xmlConsumer);
        this.strategy.toSAX(this.contentResult, this.xmlConsumer);
        verify(this.contentStrategy, this.eresourceStrategy, this.contentResult, this.xmlConsumer);
    }

    @Test
    public void testToSAXEresourceResult() {
        expect(this.eresourceResult.getEresource()).andReturn(null);
        this.eresourceStrategy.toSAX(null, this.xmlConsumer);
        replay(this.contentStrategy, this.eresourceStrategy, this.eresourceResult, this.xmlConsumer);
        this.strategy.toSAX(this.eresourceResult, this.xmlConsumer);
        verify(this.contentStrategy, this.eresourceStrategy, this.eresourceResult, this.xmlConsumer);
    }

    @Test
    public void testToSAXNeither() {
        replay(this.contentStrategy, this.eresourceStrategy, this.xmlConsumer);
        this.strategy.toSAX(createMock(SearchResult.class), this.xmlConsumer);
        verify(this.contentStrategy, this.eresourceStrategy, this.xmlConsumer);
    }
}
