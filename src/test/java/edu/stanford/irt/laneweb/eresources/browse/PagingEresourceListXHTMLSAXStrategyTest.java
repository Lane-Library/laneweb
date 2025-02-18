package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.model.Eresource;

public class PagingEresourceListXHTMLSAXStrategyTest {

        private Eresource eresource;

        private Eresource[] eresourceArray = new Eresource[256];

        private SAXStrategy<Eresource> eresourceStrategy;

        private PagingEresourceList list;

        private ListIterator<Eresource> listIterator;

        private EresourceListPagingData pagingData;

        private PagingLabel pagingLabel;

        private List<PagingLabel> pagingLabels;

        private ListIterator<PagingLabel> pagingLabelsIterator;

        private SAXStrategy<EresourceListPagingData> pagingSaxStrategy;

        private PagingEresourceListXHTMLSAXStrategy strategy;

        private TestXMLConsumer xmlConsumer;

        @BeforeEach
        public void setUp() throws Exception {
                this.xmlConsumer = new TestXMLConsumer();
                this.eresource = mock(Eresource.class);
                Arrays.fill(this.eresourceArray, this.eresource);
                this.eresourceStrategy = mock(SAXStrategy.class);
                this.pagingSaxStrategy = mock(SAXStrategy.class);
                this.strategy = new PagingEresourceListXHTMLSAXStrategy(this.eresourceStrategy, this.pagingSaxStrategy);
                this.list = mock(PagingEresourceList.class);
                this.listIterator = mock(ListIterator.class);
                this.pagingLabel = mock(PagingLabel.class);
                this.pagingLabels = mock(List.class);
                this.pagingLabelsIterator = mock(ListIterator.class);
                this.pagingData = mock(EresourceListPagingData.class);
        }

        @Test
        public void testAllPagesToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(256);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(0)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(256);
                expect(this.listIterator.next()).andReturn(this.eresource).times(256);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(256);
                expect(this.listIterator.hasNext()).andReturn(false);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testAllPagesToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testPage0ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(0)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(101);
                expect(this.listIterator.next()).andReturn(this.eresource).times(100);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(100);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage0ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testPage13ToSAXWithOnly120() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(200);
                expect(this.pagingData.getLength()).andReturn(-80);
                expect(this.list.size()).andReturn(120);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage1ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testPage1ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(100);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(100)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(101);
                expect(this.listIterator.next()).andReturn(this.eresource).times(100);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(100);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage1ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testPage2ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(200);
                expect(this.pagingData.getLength()).andReturn(56);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(200)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(57);
                expect(this.listIterator.next()).andReturn(this.eresource).times(56);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(56);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage2ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testPage3With596ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(447);
                expect(this.pagingData.getLength()).andReturn(149);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(447)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(150);
                expect(this.listIterator.next()).andReturn(this.eresource).times(149);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(149);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage3With596ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testToSAXDescription() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(0)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(101);
                expect(this.listIterator.next()).andReturn(this.eresource).times(100);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(100);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testToSAXDescription.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testToSAXEmptyDescription() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(0);
                expect(this.list.listIterator(0)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(101);
                expect(this.listIterator.next()).andReturn(this.eresource).times(100);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(100);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage0ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testToSAXPagingData() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(200);
                expect(this.list.listIterator(0)).andReturn(this.listIterator);
                expect(this.listIterator.hasNext()).andReturn(true).times(101);
                expect(this.listIterator.next()).andReturn(this.eresource).times(100);
                this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                expectLastCall().times(100);
                replay(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingEresourceListXHTMLSAXStrategyTest-testPage0ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                this.pagingLabel,
                                this.pagingLabels, this.pagingLabelsIterator);
        }

        @Test
        public void testToSAXThrowsException() throws SAXException, IOException {
                XMLConsumer c = mock(XMLConsumer.class);
                c.startDocument();
                expectLastCall().andThrow(new SAXException());
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(0);
                assertThrows(LanewebException.class, () -> {
                        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
                        expectLastCall().times(100);
                        replay(c, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                        this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);

                        this.strategy.toSAX(this.list, c);
                        verify(c, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                                        this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
                });
        }
}
