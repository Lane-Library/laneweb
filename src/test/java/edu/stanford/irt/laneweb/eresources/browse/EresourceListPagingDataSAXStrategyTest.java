package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class EresourceListPagingDataSAXStrategyTest {

        private PagingEresourceList list;

        private EresourceListPagingData pagingData;

        private PagingLabel pagingLabel;

        private EresourceListPagingDataSAXStrategy strategy;

        private TestXMLConsumer xmlConsumer;

        @BeforeEach
        public void setUp() throws Exception {
                this.xmlConsumer = new TestXMLConsumer();
                this.strategy = new EresourceListPagingDataSAXStrategy();
                this.pagingData = mock(EresourceListPagingData.class);
                this.list = mock(PagingEresourceList.class);
                this.pagingLabel = mock(PagingLabel.class);
        }

        @Test
        public void testToSAX() throws SAXException, IOException {
                expect(this.pagingData.getSize()).andReturn(1039).times(2);
                expect(this.pagingData.getLength()).andReturn(260).times(2);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getBaseQuery()).andReturn("a=a").times(2);
                expect(this.pagingData.getAlpha()).andReturn("a");
                expect(this.pagingData.getPagingLabels()).andReturn(Arrays
                                .asList(new PagingLabel[] { this.pagingLabel, this.pagingLabel, this.pagingLabel,
                                                this.pagingLabel }));
                expect(this.pagingLabel.getStart()).andReturn("A.M.A. American journal of diseases of children");
                expect(this.pagingLabel.getEnd()).andReturn("Advances in immunology");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("Advances in insect physiology");
                expect(this.pagingLabel.getEnd()).andReturn("American journal of tropical medicine");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("American journal of tropical medicine and hygiene");
                expect(this.pagingLabel.getEnd()).andReturn("Applied optics");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("Applied psychological measurement");
                expect(this.pagingLabel.getEnd()).andReturn("Ayu");
                expect(this.pagingLabel.getResults()).andReturn(259);
                replay(this.pagingLabel, this.pagingData, this.list);
                this.xmlConsumer.startDocument();
                this.strategy.toSAX(this.pagingData, this.xmlConsumer);
                this.xmlConsumer.endDocument();
                assertEquals(this.xmlConsumer.getExpectedResult(this,
                                "EresourceListPagingDataSAXStrategyTest-testToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.pagingLabel, this.pagingData, this.list);
        }

        @Test
        public void testToSAXEmptyBaseQuery() throws SAXException, IOException {
                expect(this.pagingData.getSize()).andReturn(1039).times(2);
                expect(this.pagingData.getLength()).andReturn(260).times(2);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getBaseQuery()).andReturn("").times(2);
                expect(this.pagingData.getAlpha()).andReturn(null);
                expect(this.pagingData.getPagingLabels()).andReturn(Arrays
                                .asList(new PagingLabel[] { this.pagingLabel, this.pagingLabel, this.pagingLabel,
                                                this.pagingLabel }));
                expect(this.pagingLabel.getStart()).andReturn("A.M.A. American journal of diseases of children");
                expect(this.pagingLabel.getEnd()).andReturn("Advances in immunology");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("Advances in insect physiology");
                expect(this.pagingLabel.getEnd())
                                .andReturn("American journal of tropical medicine with text added to make it very long indeed");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("American journal of tropical medicine and hygiene");
                expect(this.pagingLabel.getEnd()).andReturn("Applied optics");
                expect(this.pagingLabel.getResults()).andReturn(260);
                expect(this.pagingLabel.getStart()).andReturn("Applied psychological measurement");
                expect(this.pagingLabel.getEnd()).andReturn("Ayu");
                expect(this.pagingLabel.getResults()).andReturn(259);
                replay(this.pagingLabel, this.pagingData, this.list);
                this.xmlConsumer.startDocument();
                this.strategy.toSAX(this.pagingData, this.xmlConsumer);
                this.xmlConsumer.endDocument();
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "EresourceListPagingDataSAXStrategyTest-testToSAXEmptyBaseQuery.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.pagingLabel, this.pagingData, this.list);
        }

        @Test
        public void testToSAXSizeEqualsLength() throws SAXException, IOException {
                expect(this.pagingData.getSize()).andReturn(50).times(2);
                expect(this.pagingData.getLength()).andReturn(50).times(2);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getAlpha()).andReturn("a");
                expect(this.pagingData.getBaseQuery()).andReturn("a=a");
                replay(this.pagingLabel, this.pagingData, this.list);
                this.xmlConsumer.startDocument();
                this.strategy.toSAX(this.pagingData, this.xmlConsumer);
                this.xmlConsumer.endDocument();
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "EresourceListPagingDataSAXStrategyTest-testToSAXSizeEqualsLength.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.pagingLabel, this.pagingData, this.list);
        }

        @Test
        public void testToSAXThrowsException() throws SAXException, IOException {
                expect(this.pagingData.getSize()).andReturn(50).times(2);
                expect(this.pagingData.getLength()).andReturn(50).times(2);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getBaseQuery()).andReturn("a=a");
                XMLConsumer consumer = mock(XMLConsumer.class);
                consumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), isA(Attributes.class));
                expectLastCall().andThrow(new SAXException());
                replay(consumer, this.pagingLabel, this.pagingData, this.list);
                assertThrows(LanewebException.class, () -> {
                        this.strategy.toSAX(this.pagingData, consumer);
                        verify(consumer, this.pagingLabel, this.pagingData, this.list);
                });

        }
}
