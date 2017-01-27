package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.BassettImage;
import edu.stanford.irt.solr.service.SolrImageService;

public class BassettImageGeneratorTest {

    protected SolrImageService service;

    
    private BassettImageGenerator generator;

    private SAXStrategy<Page<BassettImage>> saxStrategy;

    private XMLConsumer xmlConsumer;

    private Pageable page = new PageRequest(0, 30);
    
    private Page<BassettImage> resultPage;
    
  
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.service = createMock(SolrImageService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BassettImageGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.resultPage = createMock(Page.class);
       
    }

    @Test
    public void testDoGenerateBassettNumber() {
        expect( this.service.findBassettByNumber("bn")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.resultPage, this.service,this.xmlConsumer, this.saxStrategy);
        this.generator.setModel(Collections.singletonMap(Model.BASSETT_NUMBER, "bn"));
        this.generator.doGenerate(this.xmlConsumer);
        
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }


    @Test
    public void testDoGenerateQuery() {
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        expect( this.service.findBassettByQuery("query", page)).andReturn(null);
        replay(this.service, this.xmlConsumer, this.saxStrategy, this.resultPage);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateQueryRegion() {
        expect( this.service.findBassettByQueryFilterByRegion("term", "region",page)).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.xmlConsumer, this.saxStrategy);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "term");
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateRegionByPage() {
        Pageable testPage = new PageRequest(3, 30);
        expect(this.service.findBassettByQueryFilterByRegion("query", "region", testPage)).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.xmlConsumer, this.saxStrategy, this.resultPage);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put(Model.PAGE, "4"); 
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }
}