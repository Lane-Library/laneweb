package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
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

    private Link link;

    private Version version;
    
    private EresourceSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceSAXStrategy();
        this.eresource = mock(Eresource.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.link = mock(Link.class);
        this.version = mock(Version.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        int[] itemCount = { 1, 1 };
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
        expect(this.eresource.getDois()).andReturn(Collections.singletonList("doi"));
        // link 1
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(Resource.LABEL);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getVersion()).andReturn(this.version);
        expect(this.version.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getAdditionalText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.version.getCallnumber()).andReturn("callnumber");
        expect(this.version.getItemCount()).andReturn(itemCount);
        expect(this.version.getLocationName()).andReturn("locationName");
        expect(this.version.getLocationUrl()).andReturn("locationUrl");
        // link 2
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn("get password");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getVersion()).andReturn(this.version);
        expect(this.version.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getAdditionalText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.version.getCallnumber()).andReturn("callnumber");
        expect(this.version.getItemCount()).andReturn(itemCount);
        expect(this.version.getLocationName()).andReturn("locationName");
        expect(this.version.getLocationUrl()).andReturn("locationUrl");
        // link 3
        expect(this.link.getType()).andReturn(LinkType.LANE_IMPACTFACTOR);
        expect(this.link.getLabel()).andReturn("impact factor");
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getVersion()).andReturn(this.version);
        expect(this.version.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getAdditionalText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.version.getCallnumber()).andReturn(null);
        expect(this.version.getItemCount()).andReturn(null);
        expect(this.version.getLocationName()).andReturn("locationName");
        expect(this.version.getLocationUrl()).andReturn(null);
        // link 4
        expect(this.eresource.getIsAnExactMatch()).andReturn("false");
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getLabel()).andReturn(null);
        expect(this.link.getUrl()).andReturn(Resource.URL);
        expect(this.link.getLinkText()).andReturn(Resource.LINK_TEXT);
        expect(this.link.getVersion()).andReturn(this.version);
        expect(this.version.getHoldingsAndDates()).andReturn("holdings-dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getAdditionalText()).andReturn("version-text");
        expect(this.link.getAdditionalText()).andReturn(Resource.ADDITIONAL_TEXT);
        expect(this.version.getCallnumber()).andReturn("callnumber");
        expect(this.version.getItemCount()).andReturn(itemCount);
        expect(this.version.getLocationName()).andReturn("locationName");
        expect(this.version.getLocationUrl()).andReturn("locationUrl");
        replay(this.eresource, this.link, this.version);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EresourceSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link, this.version);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer consumer = mock(XMLConsumer.class);
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
