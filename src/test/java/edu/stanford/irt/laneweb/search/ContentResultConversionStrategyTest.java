package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.DefaultContentResult;
import edu.stanford.irt.search.impl.DefaultResult;

public class ContentResultConversionStrategyTest {

    private DefaultResult contentParentResult;

    private DefaultContentResult contentResult;

    private ContentResultConversionStrategy conversionStrategy;

    private Query query;

    private DefaultResult resourceResult;

    private DefaultResult result;

    private ScopusDeduplicator scopusDeduplicator;

    private ScoreStrategy scoreStrategy;

    private DefaultResult uberResult;

    @Before
    public void setUp() throws Exception {
        this.scoreStrategy = createMock(ScoreStrategy.class);
        this.scopusDeduplicator = createMock(ScopusDeduplicator.class);
        this.conversionStrategy = new ContentResultConversionStrategy(this.scoreStrategy, this.scopusDeduplicator);
        this.uberResult = createMock(DefaultResult.class);
        this.query = createMock(Query.class);
        this.result = createMock(DefaultResult.class);
        this.contentParentResult = createMock(DefaultResult.class);
        this.resourceResult = createMock(DefaultResult.class);
        this.contentResult = createMock(DefaultContentResult.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertResult() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(
                Arrays.asList(new DefaultResult[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(Collections.<DefaultResult> singleton(this.contentResult));
        this.contentResult.setParent(this.resourceResult);
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getParent()).andReturn(this.resourceResult);
        expect(this.contentResult.getTitle()).andReturn("title");
        this.scopusDeduplicator.removeDuplicates(isA(Collection.class));
        replay(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result,
                this.scoreStrategy, this.uberResult, this.scopusDeduplicator);
        assertEquals(1, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result,
                this.scoreStrategy, this.uberResult, this.scopusDeduplicator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertResultNullChildren() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(
                Arrays.asList(new DefaultResult[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(null);
        expect(this.uberResult.getQuery()).andReturn(this.query);
        this.scopusDeduplicator.removeDuplicates(isA(Collection.class));
        replay(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result,
                this.scoreStrategy, this.uberResult, this.scopusDeduplicator);
        assertEquals(0, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result,
                this.scoreStrategy, this.uberResult, this.scopusDeduplicator);
    }
}
