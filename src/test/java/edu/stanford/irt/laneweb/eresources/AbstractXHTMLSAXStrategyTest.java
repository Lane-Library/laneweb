package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class AbstractXHTMLSAXStrategyTest {

    private static class TestAbstractXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<Object> {

        @Override
        public void toSAX(final Object object, final XMLConsumer xmlConsumer) {
        }
    }

    private AbstractXHTMLSAXStrategy<Object> strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new TestAbstractXHTMLSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testStartAnchorWithNullTitle() throws SAXException {
        Capture<Attributes> attributes = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        replay(this.xmlConsumer);
        this.strategy.startAnchorWithTitle(this.xmlConsumer, "href", null);
        assertEquals("", attributes.getValue().getValue("title"));
        verify(this.xmlConsumer);
    }
}
