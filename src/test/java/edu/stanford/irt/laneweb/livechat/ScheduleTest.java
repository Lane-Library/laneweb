package edu.stanford.irt.laneweb.livechat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {

    private Schedule schedule;

    @Before
    public void setUp() throws Exception {
        this.schedule = new Schedule();
    }

    @Test
    public void testIsAvaliableAtNoonFriday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        Date date = calendar.getTime();
        assertTrue(this.schedule.isAvaliableAt(date));
    }

    @Test
    public void testIsAvaliableAtNoonSunday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        Date date = calendar.getTime();
        assertTrue(this.schedule.isAvaliableAt(date));
    }

    @Test
    public void testIsNotAvaliable730PMMonday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        Date date = calendar.getTime();
        assertFalse(this.schedule.isAvaliableAt(date));
    }
}
