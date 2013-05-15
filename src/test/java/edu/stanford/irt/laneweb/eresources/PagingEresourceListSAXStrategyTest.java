package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;

public class PagingEresourceListSAXStrategyTest {

    private Eresource eresource;

    private Eresource[] eresourceArray = new Eresource[256];

    private SAXStrategy<Eresource> eresourceStrategy;

    private PagingEresourceList list;

    private ListIterator<Eresource> listIterator;

    private EresourceListPagingData pagingData;

    private PagingLabel pagingLabel;

    private List<PagingLabel> pagingLabels;

    private ListIterator<PagingLabel> pagingLabelsIterator;

    private PagingEresourceListSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        Arrays.fill(this.eresourceArray, this.eresource);
        this.eresourceStrategy = createMock(SAXStrategy.class);
        this.strategy = new PagingEresourceListSAXStrategy(this.eresourceStrategy);
        this.list = createMock(PagingEresourceList.class);
        this.listIterator = createMock(ListIterator.class);
        this.pagingLabel = createMock(PagingLabel.class);
        this.pagingLabels = createMock(List.class);
        this.pagingLabelsIterator = createMock(ListIterator.class);
        this.pagingData = createMock(EresourceListPagingData.class);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(256);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expect(this.list.size()).andReturn(256);
        expect(this.pagingData.getPage()).andReturn(-1);
        expect(this.pagingData.getPages()).andReturn(3);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES),
                capture(atts));
        expect(this.pagingData.getPagingLabels()).andReturn(this.pagingLabels);
        expect(this.pagingLabels.listIterator()).andReturn(this.pagingLabelsIterator);
        expect(this.pagingLabelsIterator.hasNext()).andReturn(false);
        expect(this.list.listIterator(0)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(256);
        expect(this.listIterator.next()).andReturn(this.eresource).times(256);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(256);
        expect(this.listIterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("256", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(100);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expect(this.list.size()).andReturn(256);
        expect(this.pagingData.getPage()).andReturn(0);
        expect(this.pagingData.getPages()).andReturn(3);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES),
                capture(atts));
        expect(this.pagingData.getPagingLabels()).andReturn(this.pagingLabels);
        expect(this.pagingLabels.listIterator()).andReturn(this.pagingLabelsIterator);
        List<Capture<Attributes>> pagingLabelAtts = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            expect(this.pagingLabelsIterator.hasNext()).andReturn(true);
            expect(this.pagingLabelsIterator.next()).andReturn(this.pagingLabel);
            pagingLabelAtts.add(new Capture<Attributes>());
            expect(this.pagingLabel.getStart()).andReturn("start title");
            expect(this.pagingLabel.getEnd()).andReturn("end title");
            if (i == 2) {
                expect(this.pagingLabel.getResults()).andReturn(56);
            } else {
                expect(this.pagingLabel.getResults()).andReturn(100);
            }
            this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"),
                    capture(pagingLabelAtts.get(i)));
            this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"));
        }
        expect(this.pagingLabelsIterator.hasNext()).andReturn(false);
        expect(this.list.listIterator(0)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(101);
        expect(this.listIterator.next()).andReturn(this.eresource).times(100);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("0", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        for (int i = 0; i < 3; i++) {
            assertEquals("start title", pagingLabelAtts.get(i).getValue().getValue("start"));
            assertEquals("end title", pagingLabelAtts.get(i).getValue().getValue("end"));
            if (i == 2) {
                assertEquals("56", pagingLabelAtts.get(i).getValue().getValue("results"));
            } else {
                assertEquals("100", pagingLabelAtts.get(i).getValue().getValue("results"));
            }
        }
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(100);
        expect(this.pagingData.getLength()).andReturn(100);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expect(this.list.size()).andReturn(256);
        expect(this.pagingData.getPage()).andReturn(1);
        expect(this.pagingData.getPages()).andReturn(3);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES),
                capture(atts));
        expect(this.pagingData.getPagingLabels()).andReturn(this.pagingLabels);
        expect(this.pagingLabels.listIterator()).andReturn(this.pagingLabelsIterator);
        List<Capture<Attributes>> pagingLabelAtts = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            expect(this.pagingLabelsIterator.hasNext()).andReturn(true);
            expect(this.pagingLabelsIterator.next()).andReturn(this.pagingLabel);
            pagingLabelAtts.add(new Capture<Attributes>());
            expect(this.pagingLabel.getStart()).andReturn("start title");
            expect(this.pagingLabel.getEnd()).andReturn("end title");
            if (i == 2) {
                expect(this.pagingLabel.getResults()).andReturn(56);
            } else {
                expect(this.pagingLabel.getResults()).andReturn(100);
            }
            this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"),
                    capture(pagingLabelAtts.get(i)));
            this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"));
        }
        expect(this.pagingLabelsIterator.hasNext()).andReturn(false);
        expect(this.list.listIterator(100)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(101);
        expect(this.listIterator.next()).andReturn(this.eresource).times(100);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("100", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        for (int i = 0; i < 3; i++) {
            assertEquals("start title", pagingLabelAtts.get(i).getValue().getValue("start"));
            assertEquals("end title", pagingLabelAtts.get(i).getValue().getValue("end"));
            if (i == 2) {
                assertEquals("56", pagingLabelAtts.get(i).getValue().getValue("results"));
            } else {
                assertEquals("100", pagingLabelAtts.get(i).getValue().getValue("results"));
            }
        }
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(200);
        expect(this.pagingData.getLength()).andReturn(56);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expect(this.list.size()).andReturn(256);
        expect(this.pagingData.getPage()).andReturn(2);
        expect(this.pagingData.getPages()).andReturn(3);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES),
                capture(atts));
        expect(this.pagingData.getPagingLabels()).andReturn(this.pagingLabels);
        expect(this.pagingLabels.listIterator()).andReturn(this.pagingLabelsIterator);
        List<Capture<Attributes>> pagingLabelAtts = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            expect(this.pagingLabelsIterator.hasNext()).andReturn(true);
            expect(this.pagingLabelsIterator.next()).andReturn(this.pagingLabel);
            pagingLabelAtts.add(new Capture<Attributes>());
            expect(this.pagingLabel.getStart()).andReturn("start title");
            expect(this.pagingLabel.getEnd()).andReturn("end title");
            if (i == 2) {
                expect(this.pagingLabel.getResults()).andReturn(56);
            } else {
                expect(this.pagingLabel.getResults()).andReturn(100);
            }
            this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"),
                    capture(pagingLabelAtts.get(i)));
            this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"));
        }
        expect(this.pagingLabelsIterator.hasNext()).andReturn(false);
        expect(this.list.listIterator(200)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(57);
        expect(this.listIterator.next()).andReturn(this.eresource).times(56);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(56);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("200", atts.getValue().getValue("start"));
        assertEquals("56", atts.getValue().getValue("length"));
        assertEquals("2", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        for (int i = 0; i < 3; i++) {
            assertEquals("start title", pagingLabelAtts.get(i).getValue().getValue("start"));
            assertEquals("end title", pagingLabelAtts.get(i).getValue().getValue("end"));
            if (i == 2) {
                assertEquals("56", pagingLabelAtts.get(i).getValue().getValue("results"));
            } else {
                assertEquals("100", pagingLabelAtts.get(i).getValue().getValue("results"));
            }
        }
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage3With596ToSAX() throws SAXException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(447);
        expect(this.pagingData.getLength()).andReturn(149);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expect(this.list.size()).andReturn(596);
        expect(this.pagingData.getPage()).andReturn(3);
        expect(this.pagingData.getPages()).andReturn(4);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES),
                capture(atts));
        expect(this.pagingData.getPagingLabels()).andReturn(this.pagingLabels);
        expect(this.pagingLabels.listIterator()).andReturn(this.pagingLabelsIterator);
        List<Capture<Attributes>> pagingLabelAtts = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            expect(this.pagingLabelsIterator.hasNext()).andReturn(true);
            expect(this.pagingLabelsIterator.next()).andReturn(this.pagingLabel);
            pagingLabelAtts.add(new Capture<Attributes>());
            expect(this.pagingLabel.getStart()).andReturn("start title");
            expect(this.pagingLabel.getEnd()).andReturn("end title");
            expect(this.pagingLabel.getResults()).andReturn(149);
            this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"),
                    capture(pagingLabelAtts.get(i)));
            this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq("pagingLabel"), eq("pagingLabel"));
        }
        expect(this.pagingLabelsIterator.hasNext()).andReturn(false);
        expect(this.list.listIterator(447)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(150);
        expect(this.listIterator.next()).andReturn(this.eresource).times(149);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(149);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals("596", atts.getValue().getValue("size"));
        assertEquals("447", atts.getValue().getValue("start"));
        assertEquals("149", atts.getValue().getValue("length"));
        assertEquals("3", atts.getValue().getValue("page"));
        assertEquals("4", atts.getValue().getValue("pages"));
        for (int i = 0; i < 4; i++) {
            assertEquals("start title", pagingLabelAtts.get(i).getValue().getValue("start"));
            assertEquals("end title", pagingLabelAtts.get(i).getValue().getValue("end"));
            assertEquals("149", pagingLabelAtts.get(i).getValue().getValue("results"));
        }
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator, this.pagingData,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }
}
