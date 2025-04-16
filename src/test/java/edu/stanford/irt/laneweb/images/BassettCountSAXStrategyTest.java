package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class BassettCountSAXStrategyTest {

    private Map<String, Map<String, Integer>> facetPage;

    private Map<String, Integer> page;

    private BassettCountSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.strategy = new BassettCountSAXStrategy();
        this.xmlConsumer = mock(XMLConsumer.class);
        this.facetPage = mock(Map.class);
        this.page = mock(Map.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        Set<String> regions = new HashSet<>();
        Map<String, Integer> subregions1 = new HashMap<>();
        Map<String, Integer> subregions2 = new HashMap<>();
        regions.add("Region1");
        regions.add("Region2");
        subregions1.put("Region1_sub_region_Subregion1", 5);
        subregions1.put("Region1_sub_region_Subregion2", 5);
        subregions2.put("Region2_sub_region_Subregion1", 5);
        expect(this.facetPage.keySet()).andReturn(regions);
        expect(this.facetPage.get("Region1")).andReturn(subregions1);
        expect(this.facetPage.get("Region2")).andReturn(subregions2);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("sub_region"),
                eq("sub_region"));
        this.xmlConsumer.characters(aryEq(new char[] { '5' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("region"), eq("region"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.facetPage, this.page, this.xmlConsumer);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        verify(this.xmlConsumer, this.facetPage);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testToSAXEmptyMap() throws SAXException {
        expect(this.facetPage.keySet()).andReturn(Collections.emptySet());
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"), isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        expect(this.facetPage.get("region")).andReturn(this.page);
        expect(this.page.entrySet()).andReturn(Collections.EMPTY_SET);
        expect(this.facetPage.get("sub_region")).andReturn(this.page);
        replay(this.xmlConsumer, this.facetPage, this.page);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        verify(this.xmlConsumer);
    }

    @Test
    public void testToSAXThrowException() throws SAXException {
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.xmlConsumer);
        try {
            this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        } catch (LanewebException e) {
            assertTrue(e.getCause() instanceof SAXException);
        }
        verify(this.xmlConsumer);
    }
}
