package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractPagingSearchResultGeneratorTest {

    private static final class TestAbstractPagingSearchResultGenerator extends AbstractPagingSearchResultGenerator {

        public TestAbstractPagingSearchResultGenerator(final SAXStrategy<PagingSearchResultList> saxStrategy) {
            super(saxStrategy);
        }

        @Override
        protected List<SearchResult> getSearchResults(final String query) {
            return new ArrayList<SearchResult>();
        }
    }

    private TestAbstractPagingSearchResultGenerator generator;

    private SAXStrategy<PagingSearchResultList> saxStrategy;;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestAbstractPagingSearchResultGenerator(this.saxStrategy);
    }

    @Test
    public void testDoSearchEmpty() {
        assertEquals(0, this.generator.doSearch("").size());
    }

    @Test
    public void testDoSearchNull() {
        assertEquals(0, this.generator.doSearch(null).size());
    }

    @Test
    public void testDoSearchString() {
        assertEquals(0, this.generator.doSearch("query").getPagingData().getPage());
    }

    @Test
    public void testDoSearchStringAll() {
        this.generator.setModel(Collections.singletonMap(Model.PAGE, "all"));
        assertEquals(-1, this.generator.doSearch("query").getPagingData().getPage());
    }

    @Test
    public void testDoSearchStringNFE() {
        this.generator.setModel(Collections.singletonMap(Model.PAGE, "foo"));
        assertEquals(0, this.generator.doSearch("query").getPagingData().getPage());
    }

    @Test
    public void testDoSearchStringPage1() {
        this.generator.setModel(Collections.singletonMap(Model.PAGE, "2"));
        assertEquals(1, this.generator.doSearch("query").getPagingData().getPage());
    }
}
