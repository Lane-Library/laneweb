package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class ContentResultConversionStrategyTest {

    private Result contentParentResult;

    private ContentResult contentResult;

    private ContentResultConversionStrategy conversionStrategy;

    private Query query;

    private Result resourceResult;

    private Result result;

    private ScoreStrategy scoreStrategy;

    private Result uberResult;
    
    private ScopusDeduplicator deduplicator;

    @Before
    public void setUp() throws Exception {
        this.scoreStrategy = createMock(ScoreStrategy.class);
        this.deduplicator = createMock(ScopusDeduplicator.class);
        this.conversionStrategy = new ContentResultConversionStrategy(this.scoreStrategy, this.deduplicator);
        this.uberResult = createMock(Result.class);
        this.query = createMock(Query.class);
        this.result = createMock(Result.class);
        this.contentParentResult = createMock(Result.class);
        this.resourceResult = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }
    
    @Test
    public void testConvertResult() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(Collections.<Result> singleton(this.contentResult));
        this.contentResult.setParent(this.resourceResult);
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getParent()).andReturn(this.resourceResult);
        expect(this.contentResult.getTitle()).andReturn("title");
        this.deduplicator.removeDuplicates(isA(Set.class));
        replay(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result, this.scoreStrategy, this.uberResult, this.deduplicator);
        assertEquals(1, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result, this.scoreStrategy, this.uberResult, this.deduplicator);     
    }
    
    @Test
    public void testConvertResultNullChildren() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(null);
        expect(this.uberResult.getQuery()).andReturn(this.query);
        this.deduplicator.removeDuplicates(isA(Set.class));
        replay(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result, this.scoreStrategy, this.uberResult, this.deduplicator);
        assertEquals(0, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentParentResult, this.contentResult, this.query, this.resourceResult, this.result, this.scoreStrategy, this.uberResult, this.deduplicator);     
    }
}
