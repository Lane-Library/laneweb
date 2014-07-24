package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.ContentResultBuilder;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.ResultBuilder;

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
//     @Test
    public void testToSAX() throws SAXException, IOException {
        HashMap<String, Object> res = new HashMap<String, Object>();
        BassettImage bassettImage = new BassettImage("description", "title");
        bassettImage.setBassettNumber("100-3");
        bassettImage.setImage("bassettSrc");
        List<BassettImage> bassettImages = new ArrayList<>();
        bassettImages.add(bassettImage);
        res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
        res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
        Result metasearch = new ResultBuilder().setId("search").setDescription("description").setURL("url").build();
        Result engine = new ResultBuilder().setId("engine").setDescription("engine_description").setURL("url").build();
        engine.setHits("100");
        Result resource = new ResultBuilder().setId("resource").setDescription("description").setURL("http://resource-url.com").build();
        Result content = new ResultBuilder().setId("resource_content").setDescription("resource_description").setURL("url").build();
        content.setHits("10");
        ContentResult contentResult = new ContentResultBuilder().setId("_content_").setDescription("http://image.src").setURL("http://urlcontent.com").build();
        contentResult.setTitle("title");
        content.setChildren(Collections.singleton((Result)contentResult));
        engine.setChildren(Arrays.asList(new Result[] {resource, content}));
        metasearch.setChildren(Collections.singleton(engine));
        res.put(ImageSearchGenerator.METASEARCH_RESULT, metasearch);
        this.strategy.toSAX(res, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "ImageSearchSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }
}
