package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ContentResultConversionStrategyTest {

    private ContentResult contentResult;

    private ContentResultConversionStrategy conversionStrategy;

    private Result hitCountResult;

    private Query query;

    private Result result;

    private ScoreStrategy scoreStrategy;

    private Result uberResult;

    @BeforeEach
    public void setUp() throws Exception {
        this.scoreStrategy = mock(ScoreStrategy.class);
        this.conversionStrategy = new ContentResultConversionStrategy(this.scoreStrategy);
        this.uberResult = mock(Result.class);
        this.query = mock(Query.class);
        this.result = mock(Result.class);
        this.hitCountResult = mock(Result.class);
        this.contentResult = mock(ContentResult.class);
    }

    @Test
    public void testConvertResult() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.hitCountResult));
        expect(this.hitCountResult.getChildren()).andReturn(Collections.singleton(this.contentResult));
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.query.getSearchText()).andReturn("");
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
        assertEquals(1, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
    }

    @Test
    public void testConvertResultCollection() {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.contentResult));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("title");
        replay(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
        assertEquals(1, this.conversionStrategy.convertResults(Collections.singletonList(this.result), "query").size());
        verify(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
    }

    @Test
    public void testConvertResultNoContent() {
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.hitCountResult));
        expect(this.hitCountResult.getChildren()).andReturn(Collections.emptySet());
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.query.getSearchText()).andReturn("");
        replay(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
        assertEquals(0, this.conversionStrategy.convertResult(this.uberResult).size());
        verify(this.contentResult, this.query, this.hitCountResult, this.result, this.scoreStrategy, this.uberResult);
    }
}
