package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class BassettCountSAXStrategyTest {

    private Map<String, Integer> map;

    private BassettCountSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new BassettCountSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.map = new LinkedHashMap<String, Integer>();
    }

    @Test
    public void testToSAX() throws SAXException {
        this.map.put("Region1", Integer.valueOf(10));
        this.map.put("Region1--subregion1", Integer.valueOf(5));
        this.map.put("Region1--subregion2", Integer.valueOf(5));
        this.map.put("Region2", Integer.valueOf(5));
        this.map.put("Region2--subregion1", Integer.valueOf(5));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.strategy.toSAX(this.map, this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testToSAXEmptyMap() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.strategy.toSAX(this.map, this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testToSAXThrowException() throws SAXException {
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.xmlConsumer);
        try {
            this.strategy.toSAX(this.map, this.xmlConsumer);
        } catch (LanewebException e) {
            assertTrue(e.getCause() instanceof SAXException);
        }
        verify(this.xmlConsumer);
    }
}
