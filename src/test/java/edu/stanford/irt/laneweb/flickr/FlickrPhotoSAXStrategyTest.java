package edu.stanford.irt.laneweb.flickr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.TestXMLConsumer;

public class FlickrPhotoSAXStrategyTest {

    private FlickrPhotoSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new FlickrPhotoSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        this.strategy.toSAX(Collections.singletonList(new FlickrPhoto("page", "thumbnail")), this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "FlickrPhotoSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
