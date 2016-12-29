package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.metasearch.AlternateContentResultSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.ContentResult;

public class AlternateContentResultSAXStrategyTest {

    private ContentResult contentResult;

    private AlternateContentResultSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new AlternateContentResultSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.contentResult.getContentId()).andReturn("contentId");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getDescription()).andReturn("description");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationDate()).andReturn("publicationDate");
        expect(this.contentResult.getPublicationTitle()).andReturn("publicationTitle");
        expect(this.contentResult.getPublicationVolume()).andReturn("publicationVolume");
        expect(this.contentResult.getPublicationIssue()).andReturn("publicationIssue");
        expect(this.contentResult.getPublicationText()).andReturn("publicationText");
        expect(this.contentResult.getURL()).andReturn("url");
        replay(this.contentResult);
        this.xmlConsumer.startDocument();
        this.strategy.toSAX(this.contentResult, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "AlternateContentResultSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.contentResult);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException, IOException {
        XMLConsumer c = createMock(XMLConsumer.class);
        c.startElement(eq("http://irt.stanford.edu/search/2.0"), eq("content"), eq("content"),
                eq(XMLUtils.EMPTY_ATTRIBUTES));
        expectLastCall().andThrow(new SAXException());
        replay(this.contentResult, c);
        this.strategy.toSAX(this.contentResult, c);
    }
}
