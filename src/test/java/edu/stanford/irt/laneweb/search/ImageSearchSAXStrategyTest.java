package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ImageSearchSAXStrategyTest {

    private TestXMLConsumer xmlConsumer;

    private ImageSearchSAXStrategy strategy;

    @Before
    public void setUp() {
        this.xmlConsumer = new TestXMLConsumer();
        ImageBassettSearchSAXStrategy bassettSAXStrategy = new ImageBassettSearchSAXStrategy();
        ImageMetasearchSAXStrategy metaSearchSAXStrategy = new ImageMetasearchSAXStrategy();
        this.strategy = new ImageSearchSAXStrategy(bassettSAXStrategy, metaSearchSAXStrategy);
    }

    
    @Test
    public void testToSAX() throws SAXException, IOException {
        HashMap<String, Object> res = new HashMap<String, Object>();
        BassettImage bassettImage = new BassettImage("description", "title");
        bassettImage.setBassettNumber("bassettNumber");
        bassettImage.setImage("bassettSrc");
        List<BassettImage> bassettImages = new ArrayList<>();
        bassettImages.add(bassettImage);
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        Result metasearch = new Result("search");
        Result engine = new Result("engine");
        engine.setHits("100");
        engine.setDescription("engine_description");
        Result resource = new Result("resource");
        resource.setURL("http://resource-url.com");
        Result content = new Result("resource_content");
        content.setHits("10");
        content.setDescription("resource_description");
        ContentResult contentResult = new ContentResult("_content_");
        contentResult.setDescription("http://image.src");
        contentResult.setURL("http://urlcontent.com");
        contentResult.setTitle("title");
        content.addChild(contentResult);
        engine.addChild(resource);
        engine.addChild(content);
        metasearch.addChild(engine);
        res.put(ImageSearchGenerator.METASEARCH_RESULT, metasearch);
        this.strategy.toSAX(res, xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ImageSearchSAXStrategyTest-testToSAX.xml"),this.xmlConsumer.getStringValue());
    }
}
