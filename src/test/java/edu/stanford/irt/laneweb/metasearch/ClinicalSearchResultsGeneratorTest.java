package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsGeneratorTest {

    private ClinicalSearchResultsFactory factory;

    private ClinicalSearchResultsGenerator generator;

    private MetaSearchService metaSearchService;

    @BeforeEach
    public void setUp() {
        this.metaSearchService = mock(MetaSearchService.class);
        this.factory = mock(ClinicalSearchResultsFactory.class);
        this.generator = new ClinicalSearchResultsGenerator(this.metaSearchService, null, null, this.factory);
        this.generator.setModel(Collections.singletonMap(Model.FACET, Model.FACET));
    }

    @Test
    public void testDoSearch() {
        expect(this.metaSearchService.search("query", null, 14000L)).andReturn(null);
        expect(this.factory.createResults(null, "query", Collections.singletonList("facet"), 0)).andReturn(null);
        replay(this.metaSearchService, this.factory);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.factory);
    }

    @Test
    public void testDoSearchNoFacetsBadPage() {
        expect(this.metaSearchService.search("query", null, 14000L)).andReturn(null);
        expect(this.factory.createResults(null, "query", Collections.emptyList(), 0)).andReturn(null);
        replay(this.metaSearchService, this.factory);
        this.generator.setModel(Collections.singletonMap(Model.PAGE, Model.PAGE));
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.factory);
    }

    @Test
    public void testGetEmptyResult() {
        expect(this.factory.createResults(isA(Result.class), eq(""), eq(Collections.singletonList("facet")), eq(0)))
                .andReturn(null);
        replay(this.metaSearchService, this.factory);
        this.generator.getEmptyResult();
        verify(this.metaSearchService, this.factory);
    }
}
