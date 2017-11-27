package edu.stanford.irt.laneweb.hours;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.libraryhours.Hours;
import edu.stanford.irt.libraryhours.LibraryHoursException;
import edu.stanford.irt.libraryhours.LibraryHoursService;

public class TodaysHoursTest {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private Hours hours;

    private LibraryHoursService service;

    private Clock clock;

    @Before
    public void setUp() {
        this.service = createMock(LibraryHoursService.class);
        this.hours = createMock(Hours.class);
        this.clock = createMock(Clock.class);
    }

    @Test
    public void testClosed() {
        expect(this.clock.millis()).andReturn(System.currentTimeMillis()).times(2);
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours);
        expect(this.hours.isClosed()).andReturn(true);
        replay(this.service, this.hours, this.clock);
        assertEquals("CLOSED", new TodaysHours(this.service, this.clock).getHours());
        verify(this.service, this.hours, this.clock);
    }

    @Test
    public void testDefaultConstructor() {
        expect(this.clock.millis()).andReturn(System.currentTimeMillis()).times(2);
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours).atLeastOnce();
        expect(this.hours.isClosed()).andReturn(true).atLeastOnce();
        replay(this.service, this.hours, this.clock);
        assertEquals("CLOSED", new TodaysHours(this.service, this.clock).getHours());
        verify(this.service, this.hours, this.clock);
    }

    @Test
    public void testGetHoursLibraryHoursException() {
        expect(this.clock.millis()).andReturn(System.currentTimeMillis()).times(2);
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andThrow(new LibraryHoursException(null))
                .atLeastOnce();
        replay(this.service, this.clock);
        assertEquals(TodaysHours.UNKNOWN, new TodaysHours(this.service, this.clock).getHours());
        verify(this.service, this.clock);
    }

    @Test
    public void testGetHoursNull() {
        expect(this.clock.millis()).andReturn(System.currentTimeMillis()).times(2);
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(null).atLeastOnce();
        replay(this.service, this.clock);
        assertEquals(TodaysHours.UNKNOWN, new TodaysHours(this.service, this.clock).getHours());
        verify(this.service, this.clock);
    }

    @Test
    public void testOpen() {
        long now = System.currentTimeMillis();
        expect(this.clock.millis()).andReturn(now);
        ZonedDateTime open = ZonedDateTime.parse("2017-03-10T08:00:00.000-08:00[America/Los_Angeles]");
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours).times(2);
        expect(this.hours.isClosed()).andReturn(false).times(2);
        expect(this.hours.getOpen()).andReturn(open).times(2);
        expect(this.hours.getClose()).andReturn(ZonedDateTime.parse("2017-03-10T20:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose()).andReturn(ZonedDateTime.parse("2017-03-10T18:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.clock.millis()).andReturn(now + Duration.ofMinutes(1).toMillis());
        expect(this.clock.millis()).andReturn(now + Duration.ofHours(1).toMillis());
        replay(this.service, this.hours, this.clock);
        TodaysHours todaysHours = new TodaysHours(this.service, this.clock);
        assertEquals("8 am – 8 pm", todaysHours.getHours());
        assertEquals("8 am – 6 pm", todaysHours.getHours());
        verify(this.service, this.hours, this.clock);
    }
}
