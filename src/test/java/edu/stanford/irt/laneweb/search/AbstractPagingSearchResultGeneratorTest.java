package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractPagingSearchResultGeneratorTest {

    private static final class TestAbstractPagingSearchResultGenerator extends AbstractPagingSearchResultGenerator {

        public TestAbstractPagingSearchResultGenerator(final SAXStrategy<PagingSearchResultSet> saxStrategy) {
            super(saxStrategy);
        }

        @Override
        protected Collection<SearchResult> getSearchResults(final String query) {
            return new LinkedList<SearchResult>();
        }
    }

    private TestAbstractPagingSearchResultGenerator generator;

    private SAXStrategy<PagingSearchResultSet> saxStrategy;;

    @Before
    public void setUp() throws Exception {
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestAbstractPagingSearchResultGenerator(this.saxStrategy);
    }

    @Test
    public void testDoSearchNull() {
        assertEquals(0, this.generator.doSearch(null).size());
    }

    @Test
    public void testDoSearchString() {
        assertEquals(0, this.generator.doSearch("query").getPage());
    }

    @Test
    public void testDoSearchStringAll() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "all"));
        assertEquals(-1, this.generator.doSearch("query").getPage());
    }

    @Test
    public void testDoSearchStringNFE() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "foo"));
        assertEquals(0, this.generator.doSearch("query").getPage());
    }

    @Test
    public void testDoSearchStringPage1() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.PAGE, "2"));
        assertEquals(1, this.generator.doSearch("query").getPage());
    }
}
