package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;

public class EresourceSAXStrategyTest {

    private Eresource eresource;

    private TypedLink link;

    private EresourceSAXStrategy strategy;

    private Version version;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceSAXStrategy();
        this.eresource = createMock(Eresource.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.version = createMock(Version.class);
        this.link = createMock(TypedLink.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.eresource.getScore()).andReturn(1);
        expect(this.eresource.getId()).andReturn(2);
        expect(this.eresource.getRecordId()).andReturn(3);
        expect(this.eresource.getRecordType()).andReturn(Resource.RECORD_TYPE);
        expect(this.eresource.getTitle()).andReturn(Resource.TITLE);
        expect(this.eresource.getDescription()).andReturn("");
        expect(this.eresource.getVersions()).andReturn(Collections.singleton(this.version));
        expect(this.version.getSummaryHoldings()).andReturn(Resource.SUMMARY_HOLDINGS);
        expect(this.version.getDates()).andReturn(Resource.DATES);
        expect(this.version.getPublisher()).andReturn(Resource.PUBLISHER);
        expect(this.version.getDescription()).andReturn(Resource.DESCRIPTION);
        expect(this.version.getLinks()).andReturn(Arrays.asList(new Link[] { this.link, this.link, this.link, this.link }));

        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(Resource.LABEL);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getInstruction()).andReturn(Resource.INSTRUCTION);
        expect(this.link.getType()).andReturn(LinkType.GETPASSWORD);
        expect(this.link.getLabel()).andReturn("get password");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getInstruction()).andReturn(Resource.INSTRUCTION);
        expect(this.link.getType()).andReturn(LinkType.IMPACTFACTOR);
        expect(this.link.getLabel()).andReturn("impact factor");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getInstruction()).andReturn(Resource.INSTRUCTION);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(null);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getInstruction()).andReturn(Resource.INSTRUCTION);
        replay(this.eresource, this.version, this.link);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EresourceSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.version, this.link);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer consumer = createMock(XMLConsumer.class);
        expect(this.eresource.getScore()).andReturn(1);
        consumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESULT), eq(Resource.RESULT), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(consumer, this.eresource);
        try {
            this.strategy.toSAX(this.eresource, consumer);
            fail();
        } catch (LanewebException e) {
        }
        verify(consumer, this.eresource);
    }
}
