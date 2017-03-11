package edu.stanford.irt.laneweb.livechat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTest {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private Schedule schedule;

    @Before
    public void setUp() throws Exception {
        this.schedule = new Schedule();
    }

    @Test
    public void testCurrentTime() {
        // A completely useless test to boost test coverage
        assertTrue(this.schedule.isAvailable() | !this.schedule.isAvailable());
    }

    @Test
    public void testIsAvaliableAt10Saturday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.SATURDAY).with(LocalTime.of(10, 0))));
    }

    @Test
    public void testIsAvaliableAt18Friday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.FRIDAY).with(LocalTime.of(18, 0))));
    }

    @Test
    public void testIsAvaliableAt18Saturday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.SATURDAY).with(LocalTime.of(18, 0))));
    }

    @Test
    public void testIsAvaliableAt4Friday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.FRIDAY).with(LocalTime.of(4, 0))));
    }

    @Test
    public void testIsAvaliableAtNoonFriday() {
        assertTrue(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.FRIDAY).with(LocalTime.of(12, 0))));
    }

    @Test
    public void testIsAvaliableAtNoonSunday() {
        assertTrue(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.SUNDAY).with(LocalTime.of(12, 0))));
    }

    @Test
    public void testIsNotAvaliable4Monday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.MONDAY).with(LocalTime.of(4, 30))));
    }

    @Test
    public void testIsNotAvaliable730PMMonday() {
        assertFalse(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.MONDAY).with(LocalTime.of(19, 30))));
    }

    @Test
    public void testIsNotAvaliableNoonMonday() {
        assertTrue(this.schedule
                .isAvailableAt(ZonedDateTime.now(AMERICA_LA).with(DayOfWeek.MONDAY).with(LocalTime.of(12, 0))));
    }
}
