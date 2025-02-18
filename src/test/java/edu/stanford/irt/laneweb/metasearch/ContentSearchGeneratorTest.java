package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class ContentSearchGeneratorTest {

    private ContentResultConversionStrategy conversionStrategy;

    private ContentSearchGenerator generator;

    private MetaSearchService metaSearchService;

    private Map<String, Object> model;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @BeforeEach
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.conversionStrategy = mock(ContentResultConversionStrategy.class);
        this.generator = new ContentSearchGenerator(this.metaSearchService, this.saxStrategy, this.conversionStrategy);
        this.model = new HashMap<>();
        this.model.put(Model.QUERY, "query");
    }

    @Test
    public void testDoSearch() {
        expect(this.metaSearchService.search("query", null, 20000L)).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testDoSearchEngines() {
        this.model.put(Model.TIMEOUT, "1000");
        this.generator.setModel(this.model);
        expect(this.metaSearchService.search("query", Arrays.asList(new String[] { "a", "b", "c" }), 1000L))
                .andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testDoSearchTimeout() {
        expect(this.metaSearchService.search("query", Collections.emptyList(), 1000L)).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
        this.model.put(Model.TIMEOUT, "1000");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testDoSearchTimeoutNFE() {
        expect(this.metaSearchService.search("query", Collections.emptyList(), 20000L)).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
        this.model.put(Model.TIMEOUT, "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testDoSearchTimeoutParameter() {
        expect(this.metaSearchService.search("query", Collections.emptyList(), 1000L)).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap(Model.TIMEOUT, "1000"));
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.conversionStrategy, this.saxStrategy);
    }
}
