package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.solr.SolrRepository;

public class LinkScanGeneratorTest {

    private Eresource eresource;

    private List<Eresource> eresourceList;

    private LinkScanGenerator generator;

    private Link link;

    private List<Link> linkList;

    private SolrRepository repository;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.repository = createMock(SolrRepository.class);
        this.generator = new LinkScanGenerator(this.repository);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        this.link = createMock(Link.class);
        this.eresourceList = new LinkedList<Eresource>();
        this.eresourceList.add(this.eresource);
        this.linkList = new LinkedList<Link>();
        this.linkList.add(this.link);
    }

    @Test
    public void testDoGenerate() throws Exception {
        expect(this.repository.searchFindAllNotRecordTypePubmed(new PageRequest(0, Integer.MAX_VALUE))).andReturn(
                this.eresourceList);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.eresource.getId()).andReturn("type-id");
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getLinks()).andReturn(this.linkList);
        expect(this.link.getUrl()).andReturn("http://foo/bar");
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(" #1 ".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        Capture<Attributes> attributes = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq(" id: type-id title: title".toCharArray()), eq(0), eq(25));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endDocument();
        replay(this.repository, this.xmlConsumer, this.eresource, this.link);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals("http://foo/bar", attributes.getValue().getValue("href"));
        verify(this.repository, this.xmlConsumer, this.eresource, this.link);
    }

    @Test
    public void testDoGenerateNullTitleUrl() throws Exception {
        expect(this.repository.searchFindAllNotRecordTypePubmed(new PageRequest(0, Integer.MAX_VALUE))).andReturn(
                this.eresourceList);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.eresource.getId()).andReturn("type-id");
        expect(this.eresource.getTitle()).andReturn(null);
        expect(this.eresource.getLinks()).andReturn(this.linkList);
        expect(this.link.getUrl()).andReturn(null);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endDocument();
        replay(this.repository, this.xmlConsumer, this.eresource, this.link);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.repository, this.xmlConsumer, this.eresource, this.link);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowSAXException() throws SAXException {
        expect(this.repository.searchFindAllNotRecordTypePubmed(new PageRequest(0, Integer.MAX_VALUE))).andReturn(
                this.eresourceList);
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.repository, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.repository, this.xmlConsumer);
    }
}
