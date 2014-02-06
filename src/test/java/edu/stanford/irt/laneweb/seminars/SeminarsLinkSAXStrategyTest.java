/**
 * 
 */
package edu.stanford.irt.laneweb.seminars;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

/**
 * @author ryanmax
 */
public class SeminarsLinkSAXStrategyTest {

    private Map<String, String> map;

    SeminarsLinkSAXStrategy saxStrategy;

    private XMLConsumer xmlConsumer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.map = new LinkedHashMap<String, String>();
        this.saxStrategy = new SeminarsLinkSAXStrategy();
    }

    @Test
    public void testToSAX() throws SAXException {
        Capture<Attributes> attributes = new Capture<Attributes>();
        this.map.put("type", " type");
        this.map.put("days", " days");
        this.map.put("day", " day");
        this.map.put("month", " month");
        this.map.put("year", " year");
        this.map.put("url", " url");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/seminars/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/seminars/ns"), eq("link"), eq("link"),
                capture(attributes));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/seminars/ns"), eq("link"), eq("link"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.saxStrategy.toSAX(this.map, this.xmlConsumer);
        assertEquals(" type", attributes.getValue().getValue("type"));
        assertEquals(" day", attributes.getValue().getValue("day"));
        assertEquals(" days", attributes.getValue().getValue("days"));
        assertEquals(" month", attributes.getValue().getValue("month"));
        assertEquals(" year", attributes.getValue().getValue("year"));
        assertEquals(" url", attributes.getValue().getValue("url"));
        verify(this.xmlConsumer);
    }

    @Test
    public void testToSAXEmptyMap() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://lane.stanford.edu/seminars/ns");
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/seminars/ns"), eq("link"), eq("link"),
                isA(Attributes.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/seminars/ns"), eq("link"), eq("link"));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.xmlConsumer);
        this.saxStrategy.toSAX(this.map, this.xmlConsumer);
        verify(this.xmlConsumer);
    }
}
