package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.LegacyMetaSearch;
import edu.stanford.irt.search.impl.Result;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private LegacyMetaSearch LegacyMetaSearch;

    private Result result;

    @Before
    public void setUp() throws Exception {
        this.LegacyMetaSearch = createMock(LegacyMetaSearch.class);
        this.generator = new DescribeGenerator(this.LegacyMetaSearch, null);
        this.result = createMock(Result.class);
    }

    @Test
    public void testDoSearch() {
        expect(this.LegacyMetaSearch.describe(isA(Query.class))).andReturn(this.result);
        replay(this.LegacyMetaSearch);
        assertSame(this.result, this.generator.doSearch("query"));
        verify(this.LegacyMetaSearch);
    }

    @Test
    public void testGetKey() {
        assertEquals("describe", this.generator.getKey());
    }

    @Test
    public void testGeType() {
        assertEquals("describe", this.generator.getType());
    }

    @Test
    public void testValid() {
        assertTrue(this.generator.getValidity().isValid());
    }
}
