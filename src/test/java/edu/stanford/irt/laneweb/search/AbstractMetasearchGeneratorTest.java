package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class AbstractMetasearchGeneratorTest {

    private static final class TestAbstractMetasearchGenerator extends AbstractMetasearchGenerator<Result> {

        public TestAbstractMetasearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<Result> saxStrategy) {
            super(metaSearchManager, saxStrategy);
        }

        @Override
        protected Result doSearch(final String query) {
            return null;
        }
    }

    private AbstractMetasearchGenerator<Result> generator;

    private MetaSearchManager manager;

    private Query query;

    private SAXStrategy<Result> saxStrategy;;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(MetaSearchManager.class);
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
        expect(this.manager.search(this.query, 10, null, true)).andReturn(null);
        replay(this.manager);
        this.generator.search(this.query, 10, null, true);
        verify(this.manager);
    }
}
