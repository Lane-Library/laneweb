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
        strategy.toSAX(metasearch, xmlConsumer);
        xmlConsumer.endDocument();
        
    }
        
    
}
