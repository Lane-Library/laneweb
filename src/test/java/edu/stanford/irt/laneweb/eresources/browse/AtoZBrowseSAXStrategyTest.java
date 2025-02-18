package edu.stanford.irt.laneweb.eresources.browse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.TestXMLConsumer;

public class AtoZBrowseSAXStrategyTest {

    private List<BrowseLetter> letters;

    private AtoZBrowseSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.saxStrategy = new AtoZBrowseSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.letters = new ArrayList<>();
        this.letters.add(new BrowseLetter("base-path", "#", 10));
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            int count = 10;
            if ("cxz".contains(Character.toString(alphabet))) {
                count = 0;
            }
            this.letters.add(new BrowseLetter("base-path", Character.toString(alphabet), count));
        }
    }

    @Test
    public final void testToSAX() throws Exception {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("", "div", "div", new AttributesImpl());
        this.saxStrategy.toSAX(this.letters, this.xmlConsumer);
        this.xmlConsumer.endElement("", "div", "div");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "AtoZBrowseSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
