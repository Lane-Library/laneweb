package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Link;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class PresentationSAXStrategyTest {

    private Link link;

    private Presentation presentation;

    private PresentationSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new PresentationSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.presentation = mock(Presentation.class);
        this.link = mock(Link.class);
    }

    @Test
    public void testToSAX() throws IOException, SAXException, URISyntaxException {
        expect(this.presentation.getId()).andReturn(0);
        expect(this.presentation.getDate()).andReturn(LocalDate.of(1969, 5, 5));
        expect(this.presentation.getLinks()).andReturn(Collections.singletonList(this.link));
        expect(this.link.getURI()).andReturn(new URI("uri"));
        expect(this.link.getLinkText()).andReturn("linkText");
        expect(this.presentation.getTitle()).andReturn("title");
        expect(this.presentation.isSunetRequired()).andReturn(true);
        expect(this.presentation.getPresenterList()).andReturn(Collections.singletonList("presenter"));
        expect(this.presentation.getDescriptions()).andReturn(Collections.singletonList("description"));
        replay(this.presentation, this.link);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.presentation, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "PresentationSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.presentation, this.link);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws IOException, SAXException, URISyntaxException {
        XMLConsumer mockXMLConsumer = mock(XMLConsumer.class);
        expect(this.presentation.getId()).andReturn(0);
        expect(this.presentation.getDate()).andReturn(null);
        expect(this.presentation.getLinks()).andReturn(null);
        mockXMLConsumer.startElement(eq(""), eq("presentation"), eq("presentation"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.presentation, mockXMLConsumer);
        this.strategy.toSAX(this.presentation, mockXMLConsumer);
    }
}
