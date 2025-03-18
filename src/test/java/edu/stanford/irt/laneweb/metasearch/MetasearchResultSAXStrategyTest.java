package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class MetasearchResultSAXStrategyTest {

    private SAXStrategy<Result> engineSAXStrategy;

    private Query query;

    private Result result;

    private MetasearchResultSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.engineSAXStrategy = mock(SAXStrategy.class);
        this.strategy = new MetasearchResultSAXStrategy(this.engineSAXStrategy);
        this.xmlConsumer = new TestXMLConsumer();
        this.result = mock(Result.class);
        this.query = mock(Query.class);
    }

    @Test
    public void testToSAX() throws IOException, SAXException {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getQuery()).andReturn(this.query);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.query.getSearchText()).andReturn("query");
        this.engineSAXStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.result, this.query, this.engineSAXStrategy);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "MetasearchResultSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.query, this.engineSAXStrategy);
    }

    @Test
    public void testToSAXNullStatus() throws IOException, SAXException {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getQuery()).andReturn(this.query);
        expect(this.result.getStatus()).andReturn(null);
        expect(this.query.getSearchText()).andReturn("query");
        this.engineSAXStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.result, this.query, this.engineSAXStrategy);
        this.strategy.toSAX(this.result, this.xmlConsumer);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "MetasearchResultSAXStrategyTest-testToSAXNullStatus.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.result, this.query, this.engineSAXStrategy);
    }

    @Test
    public void testToSAXThrowsException() throws IOException, SAXException {
        XMLConsumer c = mock(XMLConsumer.class);
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getQuery()).andReturn(this.query);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        c.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(c, this.result, this.query, this.engineSAXStrategy);
        assertThrows(LanewebException.class, () -> {
            this.strategy.toSAX(this.result, c);
        });
    }
}
