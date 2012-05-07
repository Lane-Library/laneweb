package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.cocoon.xml.XMLConsumer;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.resource.Resource;

public class PagingXMLizableEresourceListTest {

    private Eresource eresource;

    private Eresource[] eresourceArray = new Eresource[256];

    private Collection<Eresource> eresources;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.eresources = createMock(Collection.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        Arrays.fill(this.eresourceArray, this.eresource);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(1792);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(1280);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(1793);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getRecordType()).andReturn("0").times(256);
        expect(this.eresource.getTitle()).andReturn("0").times(256);
        expect(this.eresource.getDescription()).andReturn("0").times(256);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet()).times(256);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.eresources, this.xmlConsumer, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, -1).toSAX(this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("256", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.eresources, this.xmlConsumer, this.eresource);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(700);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(500);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(701);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordType()).andReturn("0").times(100);
        expect(this.eresource.getTitle()).andReturn("0").times(100);
        expect(this.eresource.getDescription()).andReturn("0").times(100);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet()).times(100);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.eresources, this.xmlConsumer, this.eresource);
        new PagingXMLizableEresourceList(this.eresources).toSAX(this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("0", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.eresources, this.xmlConsumer, this.eresource);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(700);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(500);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(701);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordType()).andReturn("0").times(100);
        expect(this.eresource.getTitle()).andReturn("0").times(100);
        expect(this.eresource.getDescription()).andReturn("0").times(100);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet()).times(100);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.eresources, this.xmlConsumer, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, 1).toSAX(this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("100", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.eresources, this.xmlConsumer, this.eresource);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(392);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(280);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(393);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getRecordType()).andReturn("0").times(56);
        expect(this.eresource.getTitle()).andReturn("0").times(56);
        expect(this.eresource.getDescription()).andReturn("0").times(56);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet()).times(56);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.eresources, this.xmlConsumer, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, 2).toSAX(this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("200", atts.getValue().getValue("start"));
        assertEquals("56", atts.getValue().getValue("length"));
        assertEquals("2", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.eresources, this.xmlConsumer, this.eresource);
    }

    @Test
    public void testPage3With596ToSAX() throws SAXException {
        this.eresourceArray = new Eresource[596];
        Arrays.fill(this.eresourceArray, this.eresource);
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(596));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(1043);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(745);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(1044);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(149);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(149);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(149);
        expect(this.eresource.getRecordType()).andReturn("0").times(149);
        expect(this.eresource.getTitle()).andReturn("0").times(149);
        expect(this.eresource.getDescription()).andReturn("0").times(149);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version> emptySet()).times(149);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.eresources, this.xmlConsumer, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, 3).toSAX(this.xmlConsumer);
        assertEquals("596", atts.getValue().getValue("size"));
        assertEquals("447", atts.getValue().getValue("start"));
        assertEquals("149", atts.getValue().getValue("length"));
        assertEquals("3", atts.getValue().getValue("page"));
        assertEquals("4", atts.getValue().getValue("pages"));
        verify(this.eresources, this.xmlConsumer, this.eresource);
    }

    @Test
    public void testPagingXMLizableEresourceList() {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        replay(this.eresources, this.xmlConsumer);
        new PagingXMLizableEresourceList(this.eresources);
        verify(this.eresources, this.xmlConsumer);
    }
}
