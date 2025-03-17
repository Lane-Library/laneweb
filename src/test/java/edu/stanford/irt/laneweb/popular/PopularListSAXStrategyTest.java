package edu.stanford.irt.laneweb.popular;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class PopularListSAXStrategyTest {

    private Map<String, String> resource = mock(Map.class);

    private PopularResourcesSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.saxStrategy = new PopularResourcesSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        expect(this.resource.get("id")).andReturn("solr-id");
        replay(this.resource);
        this.saxStrategy.toSAX(Collections.singletonList(this.resource), this.xmlConsumer);
        verify(this.resource);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "PopularListSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = mock(XMLConsumer.class);
        mock.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(mock);
        assertThrows(LanewebException.class, () -> {
            this.saxStrategy.toSAX(Collections.singletonList(this.resource), mock);
        });
    }
}
