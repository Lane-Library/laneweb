package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.Resource;


public class PagingXMLizableEresourceListTest {
    
    private Collection<Eresource> eresources;
    private ContentHandler handler;
    private Eresource eresource;
    private Eresource[] eresourceArray = new Eresource[256];

    @Before
    public void setUp() throws Exception {
        this.eresources = createMock(Collection.class);
        this.handler = createMock(ContentHandler.class);
        this.eresource = createMock(Eresource.class);
        Arrays.fill(this.eresourceArray, this.eresource);
    }

    @Test
    public void testPagingXMLizableEresourceList() {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        replay(this.eresources, this.handler);
        new PagingXMLizableEresourceList(this.eresources);
        verify(this.eresources, this.handler);
    }
    
    @Test
    public void testToSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(700);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(500);
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(701);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordType()).andReturn("0").times(100);
        expect(this.eresource.getTitle()).andReturn("0").times(100);
        expect(this.eresource.getDescription()).andReturn("0").times(100);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version>emptySet()).times(100);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.eresources, this.handler, this.eresource);
        new PagingXMLizableEresourceList(this.eresources).toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        verify(this.eresources, this.handler, this.eresource);
    }
    
    @Test
    public void testTo100SAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(700);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(500);
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(701);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(100);
        expect(this.eresource.getRecordType()).andReturn("0").times(100);
        expect(this.eresource.getTitle()).andReturn("0").times(100);
        expect(this.eresource.getDescription()).andReturn("0").times(100);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version>emptySet()).times(100);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.eresources, this.handler, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, 100).toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("100", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        verify(this.eresources, this.handler, this.eresource);
    }
    
    @Test
    public void testTo200SAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(392);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(280);
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(393);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(56);
        expect(this.eresource.getRecordType()).andReturn("0").times(56);
        expect(this.eresource.getTitle()).andReturn("0").times(56);
        expect(this.eresource.getDescription()).andReturn("0").times(56);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version>emptySet()).times(56);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.eresources, this.handler, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, 200).toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("200", atts.getValue().getValue("start"));
        assertEquals("56", atts.getValue().getValue("length"));
        verify(this.eresources, this.handler, this.eresource);
    }
    
    @Test
    public void testToAllSAX() throws SAXException {
        expect(this.eresources.toArray()).andReturn(this.eresourceArray);
        expect(this.eresources.size()).andReturn(Integer.valueOf(256));
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(1792);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().times(1280);
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(1793);
        expect(this.eresource.getScore()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getId()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getRecordId()).andReturn(Integer.valueOf(0)).times(256);
        expect(this.eresource.getRecordType()).andReturn("0").times(256);
        expect(this.eresource.getTitle()).andReturn("0").times(256);
        expect(this.eresource.getDescription()).andReturn("0").times(256);
        expect(this.eresource.getVersions()).andReturn(Collections.<Version>emptySet()).times(256);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.eresources, this.handler, this.eresource);
        new PagingXMLizableEresourceList(this.eresources, -1).toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("256", atts.getValue().getValue("length"));
        verify(this.eresources, this.handler, this.eresource);
    }
}
