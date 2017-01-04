package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.Query;
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

    private MetaSearchService manager;

    private Query query;

    private SAXStrategy<Result> saxStrategy;;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(MetaSearchService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestAbstractMetasearchGenerator(this.manager, this.saxStrategy);
        this.query = createMock(Query.class);
    }

    @Test
    public void testDescribe() {
        expect(this.manager.describe(this.query, null)).andReturn(null);
        replay(this.manager);
        this.generator.describe(this.query, null);
        verify(this.manager);
    }

    @Test
    public void testSearch() {
        expect(this.manager.search(this.query, null, 10)).andReturn(null);
        replay(this.manager);
        this.generator.search(this.query, null, 10);
        verify(this.manager);
    }
}
