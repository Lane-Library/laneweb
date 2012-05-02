package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collection;

import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private MetaSearchManager metaSearchManager;

    private MetaSearchManagerSource metaSearchManagerSource;

    private Result result;

    @Before
    public void setUp() throws Exception {
        this.generator = new DescribeGenerator();
        this.metaSearchManagerSource = createMock(MetaSearchManagerSource.class);
        this.metaSearchManager = createMock(MetaSearchManager.class);
        expect(this.metaSearchManagerSource.getMetaSearchManager()).andReturn(this.metaSearchManager);
        replay(this.metaSearchManagerSource);
        this.generator.setMetaSearchManagerSource(this.metaSearchManagerSource);
        verify(this.metaSearchManagerSource);
        this.result = createMock(Result.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.metaSearchManager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        replay(this.metaSearchManager);
        assertSame(this.result, this.generator.doSearch());
        verify(this.metaSearchManager);
    }

    @Test
    public void testGetKey() {
        assertEquals("describe", this.generator.getKey());
    }

    @Test
    public void testValid() {
        assertEquals(SourceValidity.VALID, this.generator.getValidity().isValid());
    }
}
