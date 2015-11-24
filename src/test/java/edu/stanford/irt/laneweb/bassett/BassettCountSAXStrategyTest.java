package edu.stanford.irt.laneweb.bassett;


import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.solr.BassettImage;

public class BassettCountSAXStrategyTest {


    private BassettCountSAXStrategy strategy;

    private XMLConsumer xmlConsumer;
    
    private FacetPage<BassettImage> facetPage;

    private Page<FacetFieldEntry> page; 
    
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.strategy = new BassettCountSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.facetPage = createMock(FacetPage.class);
        this.page = createMock(Page.class); 
           
    }

    @Test
    public void testToSAX() throws SAXException {
        List<FacetFieldEntry> list = new ArrayList<FacetFieldEntry>();
        list.add( new SimpleFacetFieldEntry(new SimpleField("region"), "Region1", 10));
        list.add( new SimpleFacetFieldEntry(new SimpleField("region"), "Region2", 5));
        expect(facetPage.getFacetResultPage("region")).andReturn(this.page);
        expect(this.page.iterator()).andReturn(list.iterator());
        List<FacetFieldEntry> list2 = new ArrayList<FacetFieldEntry>();
        list2.add( new SimpleFacetFieldEntry(new SimpleField("sub_region"), "Region1_sub_region_Subregion1", 5));
        list2.add( new SimpleFacetFieldEntry(new SimpleField("sub_region"), "Region1_sub_region_Subregion2", 5));
        list2.add( new SimpleFacetFieldEntry(new SimpleField("sub_region"), "Region2_sub_region_Subregion1", 5));
        expect(facetPage.getFacetResultPage("sub_region")).andReturn(this.page);
        expect(this.page.iterator()).andReturn(list2.iterator());
        expect(facetPage.getFacetResultPage("sub_region")).andReturn(this.page);
        expect(this.page.iterator()).andReturn(list2.iterator());
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
        this.xmlConsumer
                .endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.facetPage,  this.page, this.xmlConsumer);
        this.strategy.toSAX(this.facetPage, this.xmlConsumer);
        verify(this.xmlConsumer);
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testToSAXEmptyMap() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/bassett/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"),
                eq("bassett_count"), isA(Attributes.class));
        this.xmlConsumer
                .endElement(eq("http://lane.stanford.edu/bassett/ns"), eq("bassett_count"), eq("bassett_count"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        expect(this.facetPage.getFacetResultPage("region")).andReturn(this.page);
        expect(this.page.iterator()).andReturn(Collections.EMPTY_LIST.iterator());
        expect(this.facetPage.getFacetResultPage("sub_region")).andReturn(this.page);
        expect(this.page.iterator()).andReturn(Collections.EMPTY_LIST.iterator());
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
