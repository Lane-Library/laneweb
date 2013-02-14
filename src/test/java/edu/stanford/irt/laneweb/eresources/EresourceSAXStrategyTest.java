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
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;

public class EresourceSAXStrategyTest {

    private Eresource eresource;

    private Link link;

    private EresourceSAXStrategy strategy;

    private Version version;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceSAXStrategy();
        this.eresource = createMock(Eresource.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.version = createMock(Version.class);
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        expect(this.eresource.getScore()).andReturn(1);
        Capture<Attributes> attributesCapture1 = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESULT), eq(Resource.RESULT),
                capture(attributesCapture1));
        expect(this.eresource.getId()).andReturn(2);
        recordElement(Resource.ID, 2);
        expect(this.eresource.getRecordId()).andReturn(3);
        recordElement(Resource.RECORD_ID, 3);
        expect(this.eresource.getRecordType()).andReturn(Resource.RECORD_TYPE);
        recordElement(Resource.RECORD_TYPE);
        expect(this.eresource.getTitle()).andReturn("title");
        recordElement(Resource.TITLE);
        expect(this.eresource.getDescription()).andReturn("");
        // this.xmlConsumer.startElement(eq(Resource.NAMESPACE),
        // eq(Resource.DESCRIPTION), eq(Resource.DESCRIPTION),
        // isA(Attributes.class));
        // this.xmlConsumer.characters(aryEq("description".toCharArray()),
        // eq(0), eq(11));
        // this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION,
        // Resource.DESCRIPTION);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.VERSIONS), eq(Resource.VERSIONS),
                isA(Attributes.class));
        expect(this.eresource.getVersions()).andReturn(Collections.singleton(this.version));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.VERSION), eq(Resource.VERSION),
                isA(Attributes.class));
        expect(this.version.getSummaryHoldings()).andReturn(Resource.SUMMARY_HOLDINGS).times(3);
        recordElement(Resource.SUMMARY_HOLDINGS);
        expect(this.version.getDates()).andReturn(Resource.DATES).atLeastOnce();
        recordElement(Resource.DATES);
        expect(this.version.getPublisher()).andReturn(Resource.PUBLISHER).atLeastOnce();
        recordElement(Resource.PUBLISHER);
        expect(this.version.getDescription()).andReturn(Resource.DESCRIPTION).atLeastOnce();
        recordElement(Resource.DESCRIPTION);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.LINKS), eq(Resource.LINKS),
                isA(Attributes.class));
        expect(this.version.getLinks()).andReturn(
                Arrays.asList(new Link[] { this.link, this.link, this.link, this.link })).atLeastOnce();
        Capture<Attributes> attributesCapture2 = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.LINK), eq(Resource.LINK),
                capture(attributesCapture2));
        expect(this.link.getLabel()).andReturn(Resource.LABEL).times(3);
        recordElement(Resource.LABEL);
        expect(this.link.getUrl()).andReturn(Resource.URL).atLeastOnce();
        recordElement(Resource.URL);
        expect(this.link.getInstruction()).andReturn(Resource.INSTRUCTION).atLeastOnce();
        recordElement(Resource.INSTRUCTION);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.LINK, Resource.LINK);
        expectLastCall().times(4);
        Capture<Attributes> attributesCapture3 = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.LINK), eq(Resource.LINK),
                capture(attributesCapture3));
        expect(this.link.getLabel()).andReturn("get password");
        recordElement(Resource.LABEL, "get password");
        recordElement(Resource.URL);
        recordElement(Resource.INSTRUCTION);
        Capture<Attributes> attributesCapture4 = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.LINK), eq(Resource.LINK),
                capture(attributesCapture4));
        expect(this.link.getLabel()).andReturn("impact factor");
        recordElement(Resource.LABEL, "impact factor");
        recordElement(Resource.URL);
        recordElement(Resource.INSTRUCTION);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.LINK), eq(Resource.LINK),
                isA(Attributes.class));
        expect(this.link.getLabel()).andReturn(null);
        recordElement(Resource.URL);
        recordElement(Resource.INSTRUCTION);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.LINKS, Resource.LINKS);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.VERSION, Resource.VERSION);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.VERSIONS, Resource.VERSIONS);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.RESULT, Resource.RESULT);
        replay(this.eresource, this.xmlConsumer, this.version, this.link);
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        assertEquals("1", attributesCapture1.getValue().getValue(Resource.SCORE));
        assertEquals("eresource", attributesCapture1.getValue().getValue(Resource.TYPE));
        assertEquals("normal", attributesCapture2.getValue().getValue(Resource.TYPE));
        assertEquals("getPassword", attributesCapture3.getValue().getValue(Resource.TYPE));
        assertEquals("impactFactor", attributesCapture4.getValue().getValue(Resource.TYPE));
        verify(this.eresource, this.xmlConsumer, this.version, this.link);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        expect(this.eresource.getScore()).andReturn(1);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESULT), eq(Resource.RESULT),
                isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.xmlConsumer, this.eresource);
        try {
            this.strategy.toSAX(this.eresource, this.xmlConsumer);
            fail();
        } catch (LanewebException e) {
        }
    }

    private void recordElement(final String name) throws SAXException {
        recordElement(name, name);
    }

    private void recordElement(final String name, final int value) throws SAXException {
        String string = Integer.toString(value);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(name), eq(name), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(string.toCharArray()), eq(0), eq(string.length()));
        this.xmlConsumer.endElement(Resource.NAMESPACE, name, name);
    }

    private void recordElement(final String name, final String value) throws SAXException {
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(name), eq(name), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(value.toCharArray()), eq(0), eq(value.length()));
        this.xmlConsumer.endElement(Resource.NAMESPACE, name, name);
    }
}
