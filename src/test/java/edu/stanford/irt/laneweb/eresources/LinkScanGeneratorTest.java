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

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class LinkScanGeneratorTest {

    private SolrDocument document;

    private SolrDocumentList documentList;

    private LinkScanGenerator generator;

    private QueryResponse queryResponse;

    private SolrServer solrServer;

    private String versionsJson;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.solrServer = createMock(HttpSolrServer.class);
        this.generator = new LinkScanGenerator(this.solrServer);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.queryResponse = createMock(QueryResponse.class);
        this.documentList = new SolrDocumentList();
        this.document = createMock(SolrDocument.class);
        this.documentList.add(this.document);
    }

    @Test
    public void testDoGenerate() throws Exception {
        this.versionsJson = "[{\"links\":[{\"url\":\"http://foo/bar\"}]}]";
        expect(this.solrServer.query(isA(SolrQuery.class))).andReturn(this.queryResponse);
        expect(this.queryResponse.getResults()).andReturn(this.documentList);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.document.getFieldValue("id")).andReturn("type-id");
        expect(this.document.getFieldValue("title")).andReturn("title");
        expect(this.document.getFieldValue("versionsJson")).andReturn(this.versionsJson);
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
        replay(this.solrServer, this.xmlConsumer, this.queryResponse, this.document);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals("http://foo/bar", attributes.getValue().getValue("href"));
        verify(this.solrServer, this.xmlConsumer, this.queryResponse, this.document);
    }

    @Test
    public void testDoGenerateNullTitleUrl() throws Exception {
        this.versionsJson = "[{\"links\":[]}]";
        expect(this.solrServer.query(isA(SolrQuery.class))).andReturn(this.queryResponse);
        expect(this.queryResponse.getResults()).andReturn(this.documentList);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.document.getFieldValue("id")).andReturn("type-id");
        expect(this.document.getFieldValue("title")).andReturn(null);
        expect(this.document.getFieldValue("versionsJson")).andReturn(this.versionsJson);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endDocument();
        replay(this.solrServer, this.xmlConsumer, this.queryResponse, this.document);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrServer, this.xmlConsumer, this.queryResponse, this.document);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowSAXException() throws SAXException, SolrServerException {
        expect(this.solrServer.query(isA(SolrQuery.class))).andReturn(this.queryResponse);
        expect(this.queryResponse.getResults()).andReturn(this.documentList);
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.solrServer, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrServer, this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateThrowSolrException() throws SolrServerException {
        expect(this.solrServer.query(isA(SolrQuery.class))).andThrow(new SolrServerException("foo"));
        replay(this.solrServer, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrServer, this.xmlConsumer);
    }
}
