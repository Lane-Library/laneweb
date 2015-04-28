package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class ScoreStrategyTest {

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private ResultSet result;

    private ScoreStrategy scoreStrategy;

    @Before
    public void setUp() throws Exception {
        this.scoreStrategy = new ScoreStrategy();
        this.result = createMock(ResultSet.class);
    }

    @Test
    public void testCoreTitle() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(0);
        expect(this.result.getString("CORE")).andReturn("Y");
        expect(this.result.getInt("SCORE_TITLE")).andReturn(5);
        expect(this.result.getInt("SCORE_TEXT")).andReturn(5);
        replay(this.result);
        assertEquals(45, this.scoreStrategy.computeScore("query", "title", this.result));
        verify(this.result);
    }

    @Test
    public void testExactTitleMatch() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(0);
        replay(this.result);
        assertEquals(Integer.MAX_VALUE - 100, this.scoreStrategy.computeScore("query", "query", this.result));
        verify(this.result);
    }

    @Test
    public void testExactTitleMatchNew() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(THIS_YEAR);
        replay(this.result);
        assertEquals(Integer.MAX_VALUE - 90, this.scoreStrategy.computeScore("query", "query", this.result));
        verify(this.result);
    }

    @Test
    public void testExactTitleMatchOld() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(THIS_YEAR - 50);
        replay(this.result);
        assertEquals(Integer.MAX_VALUE - 110, this.scoreStrategy.computeScore("query", "query", this.result));
        verify(this.result);
    }

    @Test
    public void testExactTitleParensMatch() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(0);
        replay(this.result);
        assertEquals(Integer.MAX_VALUE - 100,
                this.scoreStrategy.computeScore("query", "query (edition stuff here)", this.result));
        verify(this.result);
    }

    @Test
    public void testNonCoreTitle() throws SQLException {
        expect(this.result.getInt("YEAR")).andReturn(0);
        expect(this.result.getString("CORE")).andReturn("N");
        expect(this.result.getInt("SCORE_TITLE")).andReturn(5);
        expect(this.result.getInt("SCORE_TEXT")).andReturn(5);
        replay(this.result);
        assertEquals(15, this.scoreStrategy.computeScore("query", "title", this.result));
        verify(this.result);
    }
}
