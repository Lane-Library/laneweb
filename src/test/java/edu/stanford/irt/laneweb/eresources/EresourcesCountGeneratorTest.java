package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrService;

public class EresourcesCountGeneratorTest {

    private SolrService solrService;

    private EresourcesCountGenerator generator;

    private Set<String> types;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.types = Collections.singleton("type");
        this.solrService = createMock(SolrService.class);
        this.generator = new EresourcesCountGenerator(this.types, this.solrService);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() throws SAXException {
        expect(this.solrService.searchCount(this.types, "query"))
                .andReturn(Collections.singletonMap("type", Integer.valueOf(1)));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://apache.org/cocoon/SQL/2.0");
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("type".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '1' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateEmptyQuery() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://apache.org/cocoon/SQL/2.0");
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("type".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '0' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateLongQuery() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://apache.org/cocoon/SQL/2.0");
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("type".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '0' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 300) {
            sb.append(" foo");
        }
        this.generator.setModel(Collections.singletonMap(Model.QUERY, sb.toString()));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://apache.org/cocoon/SQL/2.0");
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"),
                isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("type".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("genre"), eq("genre"));
        this.xmlConsumer.startElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"),
                isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(new char[] { '0' }), eq(0), eq(1));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("hits"), eq("hits"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("row"), eq("row"));
        this.xmlConsumer.endElement(eq("http://apache.org/cocoon/SQL/2.0"), eq("rowset"), eq("rowset"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.solrService, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, null));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.solrService, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateThrowException() throws SAXException {
        expect(this.solrService.searchCount(this.types, "query"))
                .andReturn(Collections.singletonMap("type", Integer.valueOf(1)));
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
