package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.bassett.BassettImage;

public class ImageBassettSearchSAXStrategyTest {

    private ImageBassettSearchSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.xmlConsumer = new TestXMLConsumer();
        this.strategy = new ImageBassettSearchSAXStrategy();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        HashMap<String, Object> res = new HashMap<String, Object>();
        BassettImage bassettImage = new BassettImage("description", "title");
        bassettImage.setBassettNumber("bassettNumber");
        bassettImage.setImage("bassettSrc");
        List<BassettImage> bassettImages = new ArrayList<>();
        bassettImages.add(bassettImage);
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        this.strategy.toSAX(res, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ImageBassettSearchSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXMaxImage() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        HashMap<String, Object> res = new HashMap<String, Object>();
        List<BassettImage> bassettImages = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            BassettImage bassettImage = new BassettImage("description" + i, "title" + 1);
            bassettImage.setBassettNumber("bassettNumber_" + i);
            bassettImage.setImage("bassettSrc" + i);
            bassettImages.add(bassettImage);
        }
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        this.strategy.toSAX(res, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        String content = this.xmlConsumer.getStringValue();
        assertTrue(content.contains("bassettNumber_12"));
        assertFalse(content.contains("bassettNumber_13"));
    }

    @Test
    public void testToSAXNoImages() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        HashMap<String, Object> res = new HashMap<String, Object>();
        List<BassettImage> bassettImages = Collections.emptyList();
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        this.strategy.toSAX(res, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "ImageBassettSearchSAXStrategyTest-testToSAXNoImages.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullTitle() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        HashMap<String, Object> res = new HashMap<String, Object>();
        BassettImage bassettImage = new BassettImage("description", null);
        bassettImage.setBassettNumber("bassettNumber");
        bassettImage.setImage("bassettSrc");
        List<BassettImage> bassettImages = new ArrayList<>();
        bassettImages.add(bassettImage);
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        this.strategy.toSAX(res, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "ImageBassettSearchSAXStrategyTest-testToSAXNullTitle.xml"),
                this.xmlConsumer.getStringValue());
    }
}
