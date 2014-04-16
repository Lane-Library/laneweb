/**
 * 
 */
package edu.stanford.irt.laneweb.hours;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author ryanmax
 */
public class TodaysHoursTest {

    private TodaysHours todaysHours;
    
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.todaysHours = new TodaysHours(getClass().getResource("").toExternalForm(),"hours.xml");
    }

    @Test
    public final void test() throws ParseException {
        assertTrue(this.todaysHours.toString().length() > 0);
        assertEquals("Just an average Monday", this.todaysHours.toString(dateFormat.parse("1/30/2012")));
        assertEquals("8 am - 10 pm", this.todaysHours.toString(dateFormat.parse("1/31/2012")));
        assertEquals("2/9 hours", this.todaysHours.toString(dateFormat.parse("2/09/2012")));
        assertEquals("A special someone's birthday?", this.todaysHours.toString(dateFormat.parse("2/10/2012")));
    }
}
