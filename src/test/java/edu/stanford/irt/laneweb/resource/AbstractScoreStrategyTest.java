/**
 * 
 */
package edu.stanford.irt.laneweb.resource;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class AbstractScoreStrategyTest {

    private static final class TestAbstractScoreStrategy extends AbstractScoreStrategy {

        public TestAbstractScoreStrategy() {
            // empty
        }
    }

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private AbstractScoreStrategy abstractScoreStrategy;

    @Before
    public void setUp() {
        this.abstractScoreStrategy = new TestAbstractScoreStrategy();
    }

    @Test
    public final void testComputeDateAdjustmentInt() {
        assertEquals(0, this.abstractScoreStrategy.computeDateAdjustment(0));
        assertEquals(10, this.abstractScoreStrategy.computeDateAdjustment(THIS_YEAR));
        assertEquals(1, this.abstractScoreStrategy.computeDateAdjustment(THIS_YEAR - 9));
    }

    @Test
    public final void testComputeDateAdjustmentString() {
        assertEquals(0, this.abstractScoreStrategy.computeDateAdjustment("no year"));
        assertEquals(10, this.abstractScoreStrategy.computeDateAdjustment(Integer.toString(THIS_YEAR)));
        assertEquals(5, this.abstractScoreStrategy.computeDateAdjustment(Integer.toString(THIS_YEAR - 5)));
    }
}
