package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.search.QueryTermPattern;
import edu.stanford.irt.search.impl.ContentResult;

public class ScoreStrategyTest {

    private static final int THIS_YEAR = ZonedDateTime.now(ZoneId.of("America/Los_Angeles")).getYear();

    private ContentResult result;

    private ScoreStrategy scoreStrategy;

    @BeforeEach
    public void setUp() throws Exception {
        this.scoreStrategy = new ScoreStrategy();
        this.result = mock(ContentResult.class);
    }

    @Test
    public void testComputedMultipleDescriptionAndTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title query query");
        expect(this.result.getDescription()).andReturn("query query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(60, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeExactTitleDoubleWeightScore() {
        expect(this.result.getId()).andReturn("pubmed_cochrane_reviews_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(220, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeExactTitleHalfWeightScore() {
        expect(this.result.getId()).andReturn("pubmed_clinicaltrial_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(55, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeExactTitleQuarterWeightScore() {
        expect(this.result.getId()).andReturn("aafp_patients_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(27, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeExactTitleScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(110, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeExactTitleScoreWithPeriodInResult() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title with period.");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(110,
                this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title with period")));
        verify(this.result);
    }

    @Test
    public void testComputeLowScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(1994);
        replay(this.result);
        assertEquals(0, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeMultipleDescriptionAndOneTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title query");
        expect(this.result.getDescription()).andReturn("query query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(40, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeNullDescriptionScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn(null);
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(1, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeNullPublicationDateScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(100, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeOlderScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(2010);
        replay(this.result);
        assertTrue(108 > this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("title")));
        verify(this.result);
    }

    @Test
    public void testComputeOneDescriptionAndMultipleTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title query query");
        expect(this.result.getDescription()).andReturn("query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(50, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeOneDescriptionAndOneTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title query");
        expect(this.result.getDescription()).andReturn("query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(30, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeOneDescriptionMatchScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title");
        expect(this.result.getDescription()).andReturn("query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(10, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeOneTitleMatchScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("title query");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(20, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeTitleBeginsWithAndMultipleDescriptionAndTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("query title query");
        expect(this.result.getDescription()).andReturn("query query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(90, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeTitleBeginsWithAndMultipleTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("query title query");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(70, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeTitleBeginsWithAndOneDescriptionAndMultipleTitleMatchesScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("query title query");
        expect(this.result.getDescription()).andReturn("query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(80, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testComputeTitleBeginsWithAndScore() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn("query title");
        expect(this.result.getDescription()).andReturn("description");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(65, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }

    @Test
    public void testTitleNull() {
        expect(this.result.getId()).andReturn("foo_content_1");
        expect(this.result.getTitle()).andReturn(null);
        expect(this.result.getDescription()).andReturn("query query");
        expect(this.result.getYear()).andReturn(0);
        replay(this.result);
        assertEquals(10, this.scoreStrategy.computeScore(this.result, QueryTermPattern.getPattern("query")));
        verify(this.result);
    }
}
