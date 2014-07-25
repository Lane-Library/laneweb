package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.ContentResultBuilder;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.ResultBuilder;

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
        strategy.toSAX(metasearch, xmlConsumer);
        xmlConsumer.endDocument();
        
    }
        
    
}
