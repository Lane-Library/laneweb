/**
 *
 */
package edu.stanford.irt.laneweb.hours;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ryanmax
 */
public class TodaysHoursTest {

    private TodaysHours todaysHours;

    @Before
    public void setUp() {
        this.todaysHours = new TodaysHours(new File("target/test-classes/edu/stanford/irt/laneweb/hours/hours.xml"));
    }

    @Test
    public final void testLocalDate() throws ParseException {
        assertEquals("Just an average Monday", this.todaysHours.toString(LocalDate.parse("2012-01-30")));
        assertEquals("8 am - 10 pm", this.todaysHours.toString(LocalDate.parse("2012-01-31")));
        assertEquals("2/9 hours", this.todaysHours.toString(LocalDate.parse("2012-02-09")));
        assertEquals("A special someone's birthday?", this.todaysHours.toString(LocalDate.parse("2012-02-10")));
        assertFalse(this.todaysHours.getHours().isEmpty());
        this.todaysHours = new TodaysHours(new File("target/test-classes/edu/stanford/irt/laneweb/hours/empty-file"));
        assertEquals("??", this.todaysHours.toString(LocalDate.parse("2012-01-30")));
    }
}
