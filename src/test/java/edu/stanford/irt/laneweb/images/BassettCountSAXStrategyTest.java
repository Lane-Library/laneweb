package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class BassettCountSAXStrategyTest {

    private  Map<String, Map<String, Integer>> facets;

    private   Map<String, Integer> page;

    private BassettCountSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new BassettCountSAXStrategy();
        this.xmlConsumer = mock(XMLConsumer.class);
        this.facets = mock(Map.class);
        this.page = mock(Map.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        Map<String, Integer> regions = new HashMap<>();
        Map<String, Integer> subregions = new HashMap<>();
        regions.put("Region1", 10);
        regions.put("Region2", 5);
        expect(this.facets.get("region")).andReturn(regions);
        subregions.put("Region1_sub_region_Subregion1", 5);
        subregions.put("Region1_sub_region_Subregion2", 5);
        subregions.put("Region2_sub_region_Subregion1", 5);
        expect(this.facets.get("sub_region")).andReturn(subregions);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"), eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.facets, this.page, this.xmlConsumer);
        this.strategy.toSAX(this.facets, this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testToSAXEmptyMap() throws SAXException {
        expect(this.facets.keySet()).andReturn(Collections.emptySet());
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"), isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        expect(this.facets.get("region")).andReturn(this.page);
        expect(this.page.entrySet()).andReturn(Collections.EMPTY_SET);
        expect(this.facets.get("sub_region")).andReturn(this.page);
        replay(this.xmlConsumer, this.facets, this.page);
        this.strategy.toSAX(this.facets, this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testToSAXThrowException() throws SAXException {
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.xmlConsumer);
        try {
            this.strategy.toSAX(this.facets, this.xmlConsumer);
        } catch (LanewebException e) {
            assertTrue(e.getCause() instanceof SAXException);
        }
        verify(this.xmlConsumer);
    }
}
