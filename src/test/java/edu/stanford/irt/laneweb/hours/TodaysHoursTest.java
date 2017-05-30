package edu.stanford.irt.laneweb.hours;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

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

    @Before
    public void setUp() {
        this.service = createMock(LibraryHoursService.class);
        this.hours = createMock(Hours.class);
    }

    @Test
    public void testClosed() {
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours);
        expect(this.hours.isClosed()).andReturn(true);
        replay(this.service, this.hours);
        assertEquals("CLOSED", new TodaysHours(this.service, 10).getHours());
        verify(this.service, this.hours);
    }

    @Test
    public void testDefaultConstructor() {
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours).atLeastOnce();
        expect(this.hours.isClosed()).andReturn(true).atLeastOnce();
        replay(this.service, this.hours);
        assertEquals("CLOSED", new TodaysHours(this.service).getHours());
        verify(this.service, this.hours);
    }

    @Test
    public void testGetHoursLibraryHoursException() {
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andThrow(new LibraryHoursException(null))
                .atLeastOnce();
        replay(this.service);
        assertEquals(TodaysHours.UNKNOWN, new TodaysHours(this.service).getHours());
        verify(this.service);
    }

    @Test
    public void testGetHoursNull() {
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(null).atLeastOnce();
        replay(this.service);
        assertEquals(TodaysHours.UNKNOWN, new TodaysHours(this.service).getHours());
        verify(this.service);
    }

    @Test
    public void testOpen() {
        ZonedDateTime open = ZonedDateTime.parse("2017-03-10T08:00:00.000-08:00[America/Los_Angeles]");
        ZonedDateTime close = ZonedDateTime.parse("2017-03-10T20:00:00.000-08:00[America/Los_Angeles]");
        expect(this.service.getHours(LocalDate.now(AMERICA_LA))).andReturn(this.hours).times(2);
        expect(this.hours.isClosed()).andReturn(false).times(2);
        expect(this.hours.getOpen()).andReturn(open).times(2);
        expect(this.hours.getClose()).andReturn(close).times(2);
        replay(this.service, this.hours);
        assertEquals("8 am – 8 pm", new TodaysHours(this.service, 0).getHours());
        verify(this.service, this.hours);
    }
}
