package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrService;

public class EresourcesCountGeneratorTest {

    private EresourcesCountGenerator generator;

    private SolrService solrService;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.solrService = createMock(SolrService.class);
        this.generator = new EresourcesCountGenerator(this.solrService);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() throws SAXException {
        expect(this.solrService.searchCount("query")).andReturn(Collections.singletonMap("type", Long.valueOf(1)));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/hitcounts/1.0");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("hitcounts"), eq("hitcounts"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("facet"), eq("facet"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("facet"), eq("facet"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("hitcounts"), eq("hitcounts"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() throws SAXException {
        expect(this.solrService.searchCount("")).andReturn(Collections.singletonMap("type", Long.valueOf(0)));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/hitcounts/1.0");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("hitcounts"), eq("hitcounts"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("facet"), eq("facet"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("facet"), eq("facet"));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/hitcounts/1.0"), eq("hitcounts"), eq("hitcounts"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, null));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateThrowException() throws SAXException {
        expect(this.solrService.searchCount("query")).andReturn(Collections.singletonMap("type", Long.valueOf(1)));
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        try {
            this.generator.doGenerate(this.xmlConsumer);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.solrService, this.xmlConsumer);
    }
}
