package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
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

import edu.stanford.irt.bassett.model.BassettImage;
import edu.stanford.irt.bassett.service.BassettImageService;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class BassettImageGeneratorTest {

    protected BassettImageService service;

    private BassettImageGenerator generator;

    private Pageable page = PageRequest.of(0, 30);

    private Page<BassettImage> resultPage;

    private SAXStrategy<Page<BassettImage>> saxStrategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.service = mock(BassettImageService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BassettImageGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.resultPage = mock(Page.class);
    }

    @Test
    public void testDoGenerateBassettNumber() {
        expect(this.service.findBassettByNumber("bn")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.resultPage, this.service, this.xmlConsumer, this.saxStrategy);
        this.generator.setModel(Collections.singletonMap(Model.BASSETT_NUMBER, "bn"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateQuery() {
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        expect(this.service.findAll(this.page)).andReturn(null);
        replay(this.service, this.xmlConsumer, this.saxStrategy, this.resultPage);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateQueryRegion() {
        expect(this.service.findBassettByRegion( "region", this.page)).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.xmlConsumer, this.saxStrategy);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testDoGenerateRegionByPage() {
        Pageable testPage = PageRequest.of(3, 30);
        expect(this.service.findBassettByRegion("region", testPage)).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.xmlConsumer, this.saxStrategy, this.resultPage);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.PAGE, "4");
        model.put(Model.REGION, "region");
        this.generator.setModel(model);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.xmlConsumer, this.saxStrategy);
    }
}
