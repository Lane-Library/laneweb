package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ImageMetasearchSAXStrategyTest {

    private TestXMLConsumer xmlConsumer;
    
    private ImageMetasearchSAXStrategy strategy;
    

    @Before
    public void setUp() {
     this.xmlConsumer = new TestXMLConsumer();
     this.strategy = new ImageMetasearchSAXStrategy();
    }
    
    
    @Test
    public void testToSAX() throws SAXException, IOException {
        xmlConsumer.startDocument();
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
        strategy.toSAX(metasearch, xmlConsumer);
        xmlConsumer.endDocument();
        
    }
        
    
}
