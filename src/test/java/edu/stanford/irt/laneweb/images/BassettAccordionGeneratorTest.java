package edu.stanford.irt.laneweb.images;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.bassett.service.BassettImageService;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class BassettAccordionGeneratorTest {

    protected BassettImageService service;

    Map<String, Map<String, Integer>> facets;

    private BassettAccordionGenerator generator;

    private SAXStrategy<Map<String, Map<String, Integer>>> saxStrategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.service = mock(BassettImageService.class);
        this.facets = mock(Map.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BassettAccordionGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.service.facetBassettOnRegionAndSubRegion()).andReturn(null);
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoGenerateEmptyQuery() {
        expect(this.service.facetBassettOnRegionAndSubRegion()).andReturn(this.facets);
        this.saxStrategy.toSAX(isA(Map.class), eq(this.xmlConsumer));
        replay(this.saxStrategy, this.xmlConsumer, this.service);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, ""));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateNullQuery() {
        expect(this.service.facetBassettOnRegionAndSubRegion()).andReturn(this.facets);
        this.saxStrategy.toSAX(this.facets, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer, this.facets);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }
}
