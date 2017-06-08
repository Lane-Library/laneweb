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

    private Link link;

    private EresourceSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceSAXStrategy();
        this.eresource = createMock(Eresource.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.eresource.getId()).andReturn("2");
        expect(this.eresource.getRecordId()).andReturn("3");
        expect(this.eresource.getRecordType()).andReturn(Resource.RECORD_TYPE);
        expect(this.eresource.getTitle()).andReturn(Resource.TITLE);
        expect(this.eresource.getDescription()).andReturn("");
        expect(this.eresource.getPublicationAuthorsText()).andReturn("");
        expect(this.eresource.getPublicationText()).andReturn("");
        expect(this.eresource.getPrimaryType()).andReturn("primaryType");
        expect(this.eresource.getTotal()).andReturn(10);
        expect(this.eresource.getAvailable()).andReturn(5);
        expect(this.eresource.getLinks())
                .andReturn(Arrays.asList(new Link[] { this.link, this.link, this.link, this.link }));
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(Resource.LABEL);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.link.getPublisher()).andReturn("publisher");
        expect(this.link.getVersionText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.link.getType()).andReturn(LinkType.GETPASSWORD);
        expect(this.link.getLabel()).andReturn("get password");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.link.getPublisher()).andReturn("publisher");
        expect(this.link.getVersionText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.link.getType()).andReturn(LinkType.IMPACTFACTOR);
        expect(this.link.getLabel()).andReturn("impact factor");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.link.getPublisher()).andReturn("publisher");
        expect(this.link.getVersionText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(null);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.link.getPublisher()).andReturn("publisher");
        expect(this.link.getVersionText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EresourceSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer consumer = createMock(XMLConsumer.class);
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
