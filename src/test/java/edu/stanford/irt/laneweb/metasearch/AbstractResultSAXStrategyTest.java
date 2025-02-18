package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.Result;

public class AbstractResultSAXStrategyTest {

    private static class TestAbstractResultSAXStrategy extends AbstractResultSAXStrategy<Result> {

        @Override
        public void toSAX(final Result object, final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractResultSAXStrategy<Result> strategy;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.strategy = new TestAbstractResultSAXStrategy();
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoToSAXException() throws SAXException, IOException {
        this.xmlConsumer.startElement("http://irt.stanford.edu/search/2.0", "exception", "exception",
                XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.startElement("http://irt.stanford.edu/search/2.0", "message", "message",
                XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.characters(aryEq("oopsie".toCharArray()), eq(0), eq(6));
        this.xmlConsumer.endElement("http://irt.stanford.edu/search/2.0", "message", "message");
        this.xmlConsumer.endElement("http://irt.stanford.edu/search/2.0", "exception", "exception");
        replay(this.xmlConsumer);
        this.strategy.doToSAXException(this.xmlConsumer, new Exception("oopsie"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testDoToSAXExceptionNull() throws SAXException, IOException {
        replay(this.xmlConsumer);
        this.strategy.doToSAXException(this.xmlConsumer, null);
        verify(this.xmlConsumer);
    }

    @Test
    public void testHandleElement() throws SAXException {
        this.xmlConsumer.startElement("http://irt.stanford.edu/search/2.0", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.characters(aryEq("content".toCharArray()), eq(0), eq(7));
        this.xmlConsumer.endElement("http://irt.stanford.edu/search/2.0", "name", "name");
        replay(this.xmlConsumer);
        this.strategy.handleElement(this.xmlConsumer, "name", "content");
        verify(this.xmlConsumer);
    }

    @Test
    public void testHandleElementEmpty() throws SAXException {
        replay(this.xmlConsumer);
        this.strategy.handleElement(this.xmlConsumer, "name", "");
        verify(this.xmlConsumer);
    }

    @Test
    public void testHandleElementNull() throws SAXException {
        replay(this.xmlConsumer);
        this.strategy.handleElement(this.xmlConsumer, "name", null);
        verify(this.xmlConsumer);
    }
}
