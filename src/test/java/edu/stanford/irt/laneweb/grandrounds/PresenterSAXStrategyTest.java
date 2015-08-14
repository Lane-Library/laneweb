package edu.stanford.irt.laneweb.grandrounds;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.grandrounds.Affiliation;
import edu.stanford.irt.grandrounds.Presenter;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class PresenterSAXStrategyTest {

    private Affiliation affiliation;

    private Presenter presenter;

    private PresenterSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new PresenterSAXStrategy();
        this.presenter = createMock(Presenter.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.affiliation = createMock(Affiliation.class);
    }

    @Test
    public void testToSAX() throws IOException, SAXException, URISyntaxException {
        expect(this.presenter.getId()).andReturn(0);
        expect(this.presenter.getName()).andReturn("name");
        expect(this.presenter.getURI()).andReturn(new URI("uri"));
        expect(this.presenter.getAffiliations()).andReturn(Collections.singletonList(this.affiliation));
        expect(this.affiliation.getTitle()).andReturn("title");
        expect(this.affiliation.getName()).andReturn("name");
        expect(this.affiliation.getStart()).andReturn("start");
        expect(this.affiliation.getEnd()).andReturn("end");
        replay(this.affiliation, this.presenter);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.presenter, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "PresenterSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.affiliation, this.presenter);
    }

    @Test
    public void testToSAXNoID() throws IOException, SAXException, URISyntaxException {
        expect(this.presenter.getId()).andReturn(-1);
        expect(this.presenter.getName()).andReturn("name");
        expect(this.presenter.getURI()).andReturn(new URI("uri"));
        expect(this.presenter.getAffiliations()).andReturn(Collections.singletonList(this.affiliation));
        expect(this.affiliation.getTitle()).andReturn("title");
        expect(this.affiliation.getName()).andReturn("name");
        expect(this.affiliation.getStart()).andReturn("start");
        expect(this.affiliation.getEnd()).andReturn("end");
        replay(this.affiliation, this.presenter);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.presenter, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "PresenterSAXStrategyTest-testToSAXNoID.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.affiliation, this.presenter);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws IOException, SAXException, URISyntaxException {
        XMLConsumer mockXMLConsumer = createMock(XMLConsumer.class);
        expect(this.presenter.getId()).andReturn(0);
        mockXMLConsumer.startElement(eq(""), eq("presenter"), eq("presenter"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.presenter, mockXMLConsumer);
        this.strategy.toSAX(this.presenter, mockXMLConsumer);
    }
}
