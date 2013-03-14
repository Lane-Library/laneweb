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

    @Before
    public void setUp() throws Exception {
        this.scoreStrategy = createMock(ScoreStrategy.class);
        this.conversionStrategy = new ContentResultConversionStrategy(this.scoreStrategy);
        this.uberResult = createMock(Result.class);
        this.query = createMock(Query.class);
        this.result = createMock(Result.class);
        this.contentParentResult = createMock(Result.class);
        this.resourceResult = createMock(Result.class);
        this.contentResult = createMock(ContentResult.class);
    }

    @Test
    public void testConvertResult() {
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(Collections.<Result> singleton(this.contentResult));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getPublicationDate()).andReturn("date");
        expect(this.contentResult.getPublicationVolume()).andReturn("volume");
        expect(this.contentResult.getPublicationIssue()).andReturn("issue");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getContentId()).andReturn("contentId");
        replay(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
        Collection<SearchResult> searchResults = this.conversionStrategy.convertResult(this.uberResult);
        assertEquals(1, searchResults.size());
        SearchResult searchResult = searchResults.iterator().next();
        assertEquals(1, searchResult.getScore());
        assertEquals("title", searchResult.getSortTitle());
        verify(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
    }

    @Test
    public void testConvertResultNulContentId() {
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(Collections.<Result> singleton(this.contentResult));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("title");
        expect(this.contentResult.getPublicationDate()).andReturn("date");
        expect(this.contentResult.getPublicationVolume()).andReturn("volume");
        expect(this.contentResult.getPublicationIssue()).andReturn("issue");
        expect(this.contentResult.getAuthor()).andReturn("author");
        expect(this.contentResult.getContentId()).andReturn(null);
        expect(this.contentResult.getURL()).andReturn("url");
        replay(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
        Collection<SearchResult> searchResults = this.conversionStrategy.convertResult(this.uberResult);
        assertEquals(1, searchResults.size());
        SearchResult searchResult = searchResults.iterator().next();
        assertEquals(1, searchResult.getScore());
        assertEquals("title", searchResult.getSortTitle());
        verify(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
    }

    @Test
    public void testConvertResultTwoWithSameIdDifferentScore() {
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(
                Arrays.asList(new Result[] { this.contentResult, this.contentResult }));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("firsttitle");
        expect(this.contentResult.getPublicationDate()).andReturn("date").times(2);
        expect(this.contentResult.getPublicationVolume()).andReturn("volume").times(2);
        expect(this.contentResult.getPublicationIssue()).andReturn("issue").times(2);
        expect(this.contentResult.getAuthor()).andReturn("author").times(2);
        expect(this.contentResult.getContentId()).andReturn("contentId").times(4);
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(100);
        expect(this.contentResult.getTitle()).andReturn("secondtitle");
        replay(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
        Collection<SearchResult> searchResults = this.conversionStrategy.convertResult(this.uberResult);
        assertEquals(1, searchResults.size());
        SearchResult searchResult = searchResults.iterator().next();
        assertEquals(100, searchResult.getScore());
        assertEquals("secondtitle", searchResult.getSortTitle());
        verify(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
    }

    @Test
    public void testConvertResultTwoWithSameIdSameScore() {
        expect(this.uberResult.getQuery()).andReturn(this.query);
        expect(this.uberResult.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.resourceResult, this.contentParentResult }));
        expect(this.resourceResult.getId()).andReturn("id");
        expect(this.contentParentResult.getId()).andReturn("id_content");
        expect(this.contentParentResult.getChildren()).andReturn(
                Arrays.asList(new Result[] { this.contentResult, this.contentResult }));
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("firsttitle");
        expect(this.contentResult.getPublicationDate()).andReturn("date").times(2);
        expect(this.contentResult.getPublicationVolume()).andReturn("volume").times(2);
        expect(this.contentResult.getPublicationIssue()).andReturn("issue").times(2);
        expect(this.contentResult.getAuthor()).andReturn("author").times(2);
        expect(this.contentResult.getContentId()).andReturn("contentId");
        expect(this.scoreStrategy.computeScore(eq(this.contentResult), isA(Pattern.class))).andReturn(1);
        expect(this.contentResult.getTitle()).andReturn("secondtitle");
        expect(this.contentResult.getContentId()).andReturn("contentId");
        replay(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
        Collection<SearchResult> searchResults = this.conversionStrategy.convertResult(this.uberResult);
        assertEquals(1, searchResults.size());
        SearchResult searchResult = searchResults.iterator().next();
        assertEquals(1, searchResult.getScore());
        assertEquals("firsttitle", searchResult.getSortTitle());
        verify(this.scoreStrategy, this.uberResult, this.query, this.result, this.resourceResult, this.contentParentResult,
                this.contentResult);
    }
}
