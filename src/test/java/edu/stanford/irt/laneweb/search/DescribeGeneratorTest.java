package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.DefaultResult;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private MetaSearchManager<DefaultResult> metaSearchManager;

    private DefaultResult result;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.metaSearchManager = createMock(MetaSearchManager.class);
        this.generator = new DescribeGenerator(this.metaSearchManager, null);
        this.result = createMock(DefaultResult.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.metaSearchManager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        replay(this.metaSearchManager);
        assertSame(this.result, this.generator.doSearch("query"));
        verify(this.metaSearchManager);
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
