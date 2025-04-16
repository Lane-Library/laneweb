package edu.stanford.irt.laneweb.eresources.search;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.EresourceSearchService;
import edu.stanford.irt.laneweb.model.Model;

public class EresourcesCountGeneratorTest {

    private EresourcesCountGenerator generator;

    private EresourceSearchService searchService;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.searchService = mock(EresourceSearchService.class);
        this.generator = new EresourcesCountGenerator(this.searchService);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() throws SAXException {
        expect(this.searchService.searchCount("query")).andReturn(Collections.singletonMap("type", Integer.valueOf(1)));
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
        replay(this.searchService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.searchService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() throws SAXException {
        expect(this.searchService.searchCount("")).andReturn(Collections.singletonMap("type", Integer.valueOf(0)));
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
        replay(this.searchService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, null));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.searchService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateThrowException() throws SAXException {
        expect(this.searchService.searchCount("query")).andReturn(Collections.singletonMap("type", Integer.valueOf(1)));
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.searchService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        try {
            this.generator.doGenerate(this.xmlConsumer);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.searchService, this.xmlConsumer);
    }
}
