package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class BibStatusGeneratorTest {

    private BibStatusGenerator generator;

    private SAXParser parser;

    private SourceResolver resolver;

    private Source source;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.parser = createMock(SAXParser.class);
        this.resolver = createMock(SourceResolver.class);
        this.generator = new BibStatusGenerator(this.parser, this.resolver);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.source = createMock(Source.class);
    }

    @Test
    public void testDoGenerate() throws URISyntaxException, SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("http://lane.stanford.edu/voyager/items/ns", "bibs", "bibs",
                XMLUtils.EMPTY_ATTRIBUTES);
        expect(this.resolver.resolveURI(new URI("cocoon://apps/bib/1/status.xml"))).andReturn(this.source);
        this.parser.parse(eq(this.source), isA(XMLConsumer.class));
        expect(this.resolver.resolveURI(new URI("cocoon://apps/bib/2/status.xml"))).andReturn(this.source);
        this.parser.parse(eq(this.source), isA(XMLConsumer.class));
        expect(this.resolver.resolveURI(new URI("cocoon://apps/bib/3/status.xml"))).andReturn(this.source);
        this.parser.parse(eq(this.source), isA(XMLConsumer.class));
        this.xmlConsumer.endElement("http://lane.stanford.edu/voyager/items/ns", "bibs", "bibs");
        this.xmlConsumer.endDocument();
        replay(this.parser, this.resolver, this.xmlConsumer, this.source);
        this.generator.setParameters(Collections.singletonMap("bids", "1,2,3"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.parser, this.resolver, this.xmlConsumer, this.source);
    }

    @Test
    public void testDoGenerateNoBibs() throws URISyntaxException, SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("http://lane.stanford.edu/voyager/items/ns", "bibs", "bibs",
                XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.endElement("http://lane.stanford.edu/voyager/items/ns", "bibs", "bibs");
        this.xmlConsumer.endDocument();
        replay(this.parser, this.resolver, this.xmlConsumer, this.source);
        this.generator.setParameters(Collections.emptyMap());
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.parser, this.resolver, this.xmlConsumer, this.source);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowsSAXException() throws URISyntaxException, SAXException {
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.parser, this.resolver, this.xmlConsumer, this.source);
        this.generator.setParameters(Collections.singletonMap("bids", "1,2,3"));
        this.generator.doGenerate(this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowsURISyntaxException() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("http://lane.stanford.edu/voyager/items/ns", "bibs", "bibs",
                XMLUtils.EMPTY_ATTRIBUTES);
        replay(this.parser, this.resolver, this.xmlConsumer, this.source);
        this.generator.setParameters(Collections.singletonMap("bids", "\u0000"));
        this.generator.doGenerate(this.xmlConsumer);
    }
}
