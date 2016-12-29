package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsFactory;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsGeneratorTest {

    private ClinicalSearchResultsFactory factory;

    private ClinicalSearchResultsGenerator generator;

    private MetaSearchManager manager;

    @Before
    public void setUp() {
        this.manager = createMock(MetaSearchManager.class);
        this.factory = createMock(ClinicalSearchResultsFactory.class);
        this.generator = new ClinicalSearchResultsGenerator(this.manager, null, null, this.factory);
        this.generator.setModel(Collections.singletonMap(Model.FACET, Model.FACET));
    }

    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(Query.class), eq(null), eq(20000L))).andReturn(null);
        expect(this.factory.createResults(null, "query", Collections.singletonList("facet"), 0)).andReturn(null);
        replay(this.manager, this.factory);
        this.generator.doSearch("query");
        verify(this.manager, this.factory);
    }

    @Test
    public void testDoSearchNoFacetsBadPage() {
        expect(this.manager.search(isA(Query.class), eq(null), eq(20000L))).andReturn(null);
        expect(this.factory.createResults(null, "query", Collections.emptyList(), 0)).andReturn(null);
        replay(this.manager, this.factory);
        this.generator.setModel(Collections.singletonMap(Model.PAGE, Model.PAGE));
        this.generator.doSearch("query");
        verify(this.manager, this.factory);
    }

    @Test
    public void testGetEmptyResult() {
        expect(this.factory.createResults(isA(Result.class), eq(""), eq(Collections.singletonList("facet")), eq(0)))
                .andReturn(null);
        replay(this.manager, this.factory);
        this.generator.getEmptyResult();
        verify(this.manager, this.factory);
    }
}
