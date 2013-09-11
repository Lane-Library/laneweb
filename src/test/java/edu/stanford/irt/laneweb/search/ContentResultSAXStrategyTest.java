package edu.stanford.irt.laneweb.search;

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

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ContentResultSAXStrategyTest {

    private ContentResult contentResult;

    private Result resourceResult;

    private ContentResultSearchResult searchResult;

    private ContentResultSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new ContentResultSAXStrategy();
        this.searchResult = createMock(ContentResultSearchResult.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.contentResult = createMock(ContentResult.class);
        this.resourceResult = createMock(Result.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        expect(this.searchResult.getContentResult()).andReturn(this.contentResult);
        expect(this.searchResult.getResourceResult()).andReturn(this.resourceResult);
        expect(this.searchResult.getScore()).andReturn(1);
        Capture<Attributes> attributesCapture = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESULT), eq(Resource.RESULT),
                capture(attributesCapture));
        expect(this.resourceResult.getId()).andReturn(Resource.RESOURCE_ID);
        recordElement(Resource.RESOURCE_ID);
        expect(this.resourceResult.getDescription()).andReturn(Resource.RESOURCE_NAME);
        recordElement(Resource.RESOURCE_NAME);
        expect(this.resourceResult.getURL()).andReturn(Resource.RESOURCE_URL);
        recordElement(Resource.RESOURCE_URL);
        expect(this.resourceResult.getHits()).andReturn(Resource.RESOURCE_HITS);
        recordElement(Resource.RESOURCE_HITS);
        expect(this.contentResult.getId()).andReturn(Resource.ID);
        recordElement(Resource.ID);
        expect(this.contentResult.getContentId()).andReturn(Resource.CONTENT_ID);
        recordElement(Resource.CONTENT_ID);
        expect(this.contentResult.getTitle()).andReturn(Resource.TITLE);
        recordElement(Resource.TITLE);
        expect(this.contentResult.getDescription()).andReturn(Resource.DESCRIPTION);
        recordElement(Resource.DESCRIPTION);
        expect(this.contentResult.getAuthor()).andReturn(null);
        expect(this.contentResult.getURL()).andReturn(Resource.URL);
        recordElement(Resource.URL);
        expect(this.contentResult.getPublicationText()).andReturn(Resource.PUBLICATION_TEXT);
        recordElement(Resource.PUBLICATION_TEXT);
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.RESULT, Resource.RESULT);
        replay(this.searchResult, this.xmlConsumer, this.contentResult, this.resourceResult);
        this.strategy.toSAX(this.searchResult, this.xmlConsumer);
        assertEquals("1", attributesCapture.getValue().getValue("score"));
        assertEquals("searchContent", attributesCapture.getValue().getValue("type"));
        verify(this.searchResult, this.xmlConsumer, this.contentResult, this.resourceResult);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        expect(this.searchResult.getContentResult()).andReturn(this.contentResult);
        expect(this.searchResult.getResourceResult()).andReturn(this.resourceResult);
        expect(this.searchResult.getScore()).andReturn(1);
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESULT), eq(Resource.RESULT),
                isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.searchResult, this.xmlConsumer, this.contentResult, this.resourceResult);
        try {
            this.strategy.toSAX(this.searchResult, this.xmlConsumer);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.searchResult, this.xmlConsumer, this.contentResult, this.resourceResult);
    }

    private void recordElement(final String name) throws SAXException {
        recordElement(name, name);
    }

    private void recordElement(final String name, final String value) throws SAXException {
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(name), eq(name), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(value.toCharArray()), eq(0), eq(value.length()));
        this.xmlConsumer.endElement(Resource.NAMESPACE, name, name);
    }
}
