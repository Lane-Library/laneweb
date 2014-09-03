package edu.stanford.irt.laneweb.search.saxstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.resource.PagingData;

public class SearchListPagingDataSAXStrategyTest {

    private PagingData pagingData;

    private SearchListPagingDataSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new SearchListPagingDataSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.pagingData = createMock(PagingData.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.pagingData.getSize()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(0);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getBaseQuery()).andReturn("");
        replay(this.pagingData);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.pagingData, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SearchListPagingDataSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.pagingData);
    }

    @Test
    public void testToSAX320() throws SAXException, IOException {
        expect(this.pagingData.getSize()).andReturn(320);
        expect(this.pagingData.getLength()).andReturn(100);
        expect(this.pagingData.getStart()).andReturn(100);
        expect(this.pagingData.getBaseQuery()).andReturn("");
        expect(this.pagingData.getPages()).andReturn(4);
        expect(this.pagingData.getPage()).andReturn(1);
        replay(this.pagingData);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.pagingData, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SearchListPagingDataSAXStrategyTest-testToSAX320.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.pagingData);
    }

    @Test
    public void testToSAX220Alpha() throws SAXException, IOException {
        expect(this.pagingData.getSize()).andReturn(220);
        expect(this.pagingData.getLength()).andReturn(100);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getBaseQuery()).andReturn("a=a");
        expect(this.pagingData.getPages()).andReturn(3);
        expect(this.pagingData.getPage()).andReturn(0);
        replay(this.pagingData);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.pagingData, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "SearchListPagingDataSAXStrategyTest-testToSAX220Alpha.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.pagingData);
    }
}
