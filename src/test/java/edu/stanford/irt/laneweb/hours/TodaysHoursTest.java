/**
 *
 */
package edu.stanford.irt.laneweb.hours;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ryanmax
 */
public class TodaysHoursTest {

    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private TodaysHours todaysHours;

    @Before
    public void setUp() {
        this.todaysHours = new TodaysHours(new File("target/test-classes/edu/stanford/irt/laneweb/hours/hours.xml"));
    }

    @Test
    public final void testDate() throws ParseException {
        assertEquals("Just an average Monday", this.todaysHours.toString(this.dateFormat.parse("1/30/2012")));
        assertEquals("8 am - 10 pm", this.todaysHours.toString(this.dateFormat.parse("1/31/2012")));
        assertEquals("2/9 hours", this.todaysHours.toString(this.dateFormat.parse("2/09/2012")));
        assertEquals("A special someone's birthday?", this.todaysHours.toString(this.dateFormat.parse("2/10/2012")));
        assertFalse(this.todaysHours.getHours().isEmpty());
        this.todaysHours = new TodaysHours(new File("target/test-classes/edu/stanford/irt/laneweb/hours/empty-file"));
        assertEquals("??", this.todaysHours.toString(this.dateFormat.parse("1/30/2012")));
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
