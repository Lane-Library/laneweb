package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ResourceResultSAXStrategyTest {

    private ContentResult contentResult;

    private SAXStrategy<ContentResult> contentSAXStrategy;

    private Result result;

    private ResourceResultSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.contentSAXStrategy = createMock(SAXStrategy.class);
        this.strategy = new ResourceResultSAXStrategy(this.contentSAXStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.result = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getChildren()).andReturn(Collections.singleton((Result) this.contentResult));
        this.contentSAXStrategy.toSAX(this.contentResult, this.xmlConsumer);
        replay(this.result, this.contentSAXStrategy);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", AbstractResultSAXStrategy.NAMESPACE);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ResourceResultSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentSAXStrategy);
    }

    @Test
    public void testToSAXNullStatus() throws SAXException, IOException {
        expect(this.result.getStatus()).andReturn(null);
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getChildren()).andReturn(Collections.singleton((Result) this.contentResult));
        this.contentSAXStrategy.toSAX(this.contentResult, this.xmlConsumer);
        replay(this.result, this.contentSAXStrategy);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", AbstractResultSAXStrategy.NAMESPACE);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ResourceResultSAXStrategyTest-toSAXNullStatus.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.contentSAXStrategy);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException, IOException {
        XMLConsumer c = createMock(XMLConsumer.class);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getChildren()).andReturn(Collections.singleton((Result) this.contentResult));
        this.contentSAXStrategy.toSAX(this.contentResult, this.xmlConsumer);
        c.startPrefixMapping("", "http://irt.stanford.edu/search/2.0");
        c.startElement(eq("http://irt.stanford.edu/search/2.0"), eq("resource"), eq("resource"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.result, this.contentSAXStrategy, c);
        this.strategy.toSAX(this.result, c);
    }
}
