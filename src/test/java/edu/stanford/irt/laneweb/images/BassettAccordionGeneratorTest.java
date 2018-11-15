package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.solr.BassettImage;
import edu.stanford.irt.solr.service.SolrImageService;

public class BassettAccordionGeneratorTest {

    protected SolrImageService service;

    FacetPage<BassettImage> facetPage;

    private BassettAccordionGenerator generator;

    private SAXStrategy<FacetPage<BassettImage>> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.service = mock(SolrImageService.class);
        this.facetPage = mock(FacetPage.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BassettAccordionGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.service.facetBassettOnRegionAndSubRegion("query")).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoGenerateEmptyQuery() {
        expect(this.service.facetBassettOnRegionAndSubRegion("*")).andReturn(this.facetPage);
        this.saxStrategy.toSAX(isA(FacetPage.class), eq(this.xmlConsumer));
        replay(this.saxStrategy, this.xmlConsumer, this.service);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() {
        expect(this.service.facetBassettOnRegionAndSubRegion("*")).andReturn(this.facetPage);
        this.saxStrategy.toSAX(this.facetPage, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer, this.facetPage);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }
}
