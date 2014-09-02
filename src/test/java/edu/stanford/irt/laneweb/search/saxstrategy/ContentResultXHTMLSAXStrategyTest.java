package edu.stanford.irt.laneweb.search.saxstrategy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.search.ContentResultSearchResult;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

// TODO: match expected results up with actual xsl transforms
public class ContentResultXHTMLSAXStrategyTest {

    private ContentResult contentResult;

    private Result resourceResult;

    private ContentResultSearchResult result;

    private ContentResultXHTMLSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new ContentResultXHTMLSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.result = createMock(ContentResultSearchResult.class);
        this.contentResult = createMock(ContentResult.class);
        this.resourceResult = createMock(Result.class);
    }

    @Test(expected = LanewebException.class)
    public void testThrowsSAXException() throws SAXException {
        XMLConsumer x = createMock(XMLConsumer.class);
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        x.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(x, this.result, this.contentResult, this.resourceResult);
        this.strategy.toSAX(this.result, x);
        verify(x, this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXEmptyDescription() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn("");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "ContentResultXHTMLSAXStrategyTest-testToSAXEmptyDescription.xml"), this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXHitsLessThanMax() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("10");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn("text");
        expect(this.contentResult.getDescription()).andReturn("description");
        expect(this.contentResult.getContentId()).andReturn(null);
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "ContentResultXHTMLSAXStrategyTest-testToSAXHitsLessThanMax.xml"), this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXNoTextHitsLessThanMax() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("2");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "ContentResultXHTMLSAXStrategyTest-testToSAXNoTextHitsLessThanMax.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXNullAuthor() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn(null);
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAXNullAuthor.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXNullDescription() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn(null);
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "ContentResultXHTMLSAXStrategyTest-testToSAXNullDescription.xml"), this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXNullTextPubMed() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("PubMed");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn(null);
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "ContentResultXHTMLSAXStrategyTest-testToSAXNullTextPubMed.xml"), this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXPubMed() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("PubMed");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getContentId()).andReturn("PMID:12");
        expect(this.contentResult.getDescription()).andReturn("description");
        expect(this.contentResult.getPublicationText()).andReturn("title. date;volume(issue):pages.");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAXPubMed.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXTextEmpty() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn("");
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn("description");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXTextNotNull() throws SAXException, IOException {
        expect(this.result.getContentResult()).andReturn(this.contentResult);
        expect(this.result.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getDescription()).andReturn("description");
        expect(this.resourceResult.getHits()).andReturn("20");
        expect(this.contentResult.getURL()).andReturn("url");
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getPublicationText()).andReturn("text");
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.contentResult.getDescription()).andReturn("description");
        expect(this.contentResult.getContentId()).andReturn("id");
        replay(this.result, this.contentResult, this.resourceResult);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.result, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAXTextNotNull.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentResult, this.resourceResult);
    }
}
