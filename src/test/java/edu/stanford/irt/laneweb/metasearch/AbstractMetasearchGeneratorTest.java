package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.impl.Result;

public class AbstractMetasearchGeneratorTest {

    private static final class TestAbstractMetasearchGenerator extends AbstractMetasearchGenerator<Result> {

        public TestAbstractMetasearchGenerator(final MetaSearchService metaSearchService,
                final SAXStrategy<Result> saxStrategy) {
            super(metaSearchService, saxStrategy);
        }

        @Override
        protected Result doSearch(final String query) {
            return null;
        }

        @Override
        protected Result getEmptyResult() {
            return null;
        }
    }

    private AbstractMetasearchGenerator<Result> generator;

    private MetaSearchService metaSearchService;

    private String query;

    private SAXStrategy<Result> saxStrategy;;

    @BeforeEach
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new TestAbstractMetasearchGenerator(this.metaSearchService, this.saxStrategy);
        this.query = "query";
    }

    @Test
    public void testDescribe() {
        expect(this.metaSearchService.describe(this.query, null)).andReturn(null);
        replay(this.metaSearchService);
        this.generator.describe(this.query, null);
        verify(this.metaSearchService);
    }

    @Test
    public void testSearch() {
        expect(this.metaSearchService.search(this.query, null, 10)).andReturn(null);
        replay(this.metaSearchService);
        this.generator.search(this.query, null, 10);
        verify(this.metaSearchService);
    }
}
