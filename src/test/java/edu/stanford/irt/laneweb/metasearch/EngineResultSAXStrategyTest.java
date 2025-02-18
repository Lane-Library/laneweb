package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class EngineResultSAXStrategyTest {

    private SAXStrategy<Result> resourceSAXStrategy;

    private Result result;

    private EngineResultSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.resourceSAXStrategy = mock(SAXStrategy.class);
        this.strategy = new EngineResultSAXStrategy(this.resourceSAXStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.result = mock(Result.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getURL()).andReturn("url");
        this.resourceSAXStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.result, this.resourceSAXStrategy);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", AbstractResultSAXStrategy.NAMESPACE);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EngineResultSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.resourceSAXStrategy);
    }

    @Test
    public void testToSAXNullStatus() throws SAXException, IOException {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getStatus()).andReturn(null);
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getURL()).andReturn("url");
        this.resourceSAXStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.result, this.resourceSAXStrategy);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", AbstractResultSAXStrategy.NAMESPACE);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EngineResultSAXStrategyTest-toSAXNullStatus.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.resourceSAXStrategy);
    }

    @Test
    public void testToSAXThrowsException() throws SAXException, IOException {
        XMLConsumer c = mock(XMLConsumer.class);
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getException()).andReturn(null);
        expect(this.result.getHits()).andReturn("hits");
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getTime()).andReturn("time");
        expect(this.result.getURL()).andReturn("url");
        c.startPrefixMapping("", "http://irt.stanford.edu/search/2.0");
        c.startElement(eq("http://irt.stanford.edu/search/2.0"), eq("engine"), eq("engine"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.result, this.resourceSAXStrategy, c);
        assertThrows(LanewebException.class, () -> {
            this.strategy.toSAX(this.result, c);
        });
    }
}
