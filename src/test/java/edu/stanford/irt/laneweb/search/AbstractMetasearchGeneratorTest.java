package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.legacy.LegacyMetaSearch;
import edu.stanford.irt.search.legacy.Result;

public class AbstractMetasearchGeneratorTest {

    private static final class TestAbstractMetasearchGenerator extends AbstractMetasearchGenerator {

        public TestAbstractMetasearchGenerator(final LegacyMetaSearch metaSearchManager, final SAXStrategy<Result> saxStrategy) {
            super(metaSearchManager, saxStrategy);
        }

        @Override
        protected Result doSearch(final String query) {
            return null;
        }
    }

    private AbstractMetasearchGenerator generator;

    private LegacyMetaSearch manager;

    private Query query;

    private SAXStrategy<Result> saxStrategy;;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(LegacyMetaSearch.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestAbstractMetasearchGenerator(this.manager, this.saxStrategy);
        this.query = createMock(Query.class);
    }

    @Test
    public void testDescribe() {
        expect(this.manager.describe(this.query)).andReturn(null);
        replay(this.manager);
        this.generator.describe(this.query);
        verify(this.manager);
    }

    @Test
    public void testSearch() {
        expect(this.manager.search(this.query, 10, true)).andReturn(null);
        replay(this.manager);
        this.generator.search(this.query, 10, true);
        verify(this.manager);
    }
}
