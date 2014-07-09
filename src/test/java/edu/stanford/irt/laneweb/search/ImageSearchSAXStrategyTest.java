package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ImageSearchSAXStrategyTest {

    private ImageSearchSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.xmlConsumer = new TestXMLConsumer();
        ImageBassettSearchSAXStrategy bassettSAXStrategy = new ImageBassettSearchSAXStrategy();
        ImageMetasearchSAXStrategy metaSearchSAXStrategy = new ImageMetasearchSAXStrategy();
        this.strategy = new ImageSearchSAXStrategy(bassettSAXStrategy, metaSearchSAXStrategy);
    }

    // I commented because the content will change several times before the production
    // @Test
    public void testToSAX() throws SAXException, IOException {
        HashMap<String, Object> res = new HashMap<String, Object>();
        BassettImage bassettImage = new BassettImage("description", "title");
        bassettImage.setBassettNumber("100-3");
        bassettImage.setImage("bassettSrc");
        List<BassettImage> bassettImages = new ArrayList<>();
        bassettImages.add(bassettImage);
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        Result metasearch = new Result("search", "description", "url");
        Result engine = new Result("engine", "engine_description", "url");
        engine.setHits("100");
        Result resource = new Result("resource", "description", "http://resource-url.com");
        Result content = new Result("resource_content", "resource_description", "url");
        content.setHits("10");
        ContentResult contentResult = new ContentResult("_content_", "http://image.src", "http://urlcontent.com");
        contentResult.setTitle("title");
        content.addChild(contentResult);
        engine.addChild(resource);
        engine.addChild(content);
        metasearch.addChild(engine);
        res.put(ImageSearchGenerator.METASEARCH_RESULT, metasearch);
        this.strategy.toSAX(res, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ImageSearchSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
