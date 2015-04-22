package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AbstractSearchResultTest {

    private static final class TestAbstractSearchResult extends AbstractSearchResult {

        public TestAbstractSearchResult(final int score, final String title) {
            super(score, title);
        }

        @Override
        public int compareTo(final SearchResult o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasAdditionalText() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testGetSortTitleNullTitle() {
        assertNotNull(new TestAbstractSearchResult(0, null).getSortTitle());
    }
}
