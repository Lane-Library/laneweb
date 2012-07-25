package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;


public class AbstractMetasearchGeneratorTest {
    
    private static final class TestAbstractMetasearchGenerator extends AbstractMetasearchGenerator {

        public TestAbstractMetasearchGenerator(MetaSearchManager metaSearchManager, SAXStrategy<Result> saxStrategy) {
            super(metaSearchManager, saxStrategy);
        }

        @Override
        protected Result doSearch(String query) {
            return null;
        }
    }

    private AbstractMetasearchGenerator generator;
    private MetaSearchManager manager;
    private SAXStrategy<Result> saxStrategy;
    private Query query;;

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
