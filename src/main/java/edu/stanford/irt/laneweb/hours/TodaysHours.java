package edu.stanford.irt.laneweb.hours;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.libraryhours.Hours;
import edu.stanford.irt.libraryhours.LibraryHoursException;
import edu.stanford.irt.libraryhours.LibraryHoursService;

public class TodaysHours {

    public static final long ONE_HOUR = Duration.ofHours(1).toMillis();

    public static final String UNKNOWN = "??";

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("h a");

    private static final Logger log = LoggerFactory.getLogger(TodaysHours.class);

    private Clock clock;

    private long expires;

    private String hours;

    private Object lock;

    private long nextUpdate;

    private LibraryHoursService service;

    public TodaysHours(final LibraryHoursService service) {
        this(service, Clock.systemDefaultZone(), ONE_HOUR);
    }

    public TodaysHours(final LibraryHoursService service, final Clock clock) {
        this(service, clock, ONE_HOUR);
    }

    TodaysHours(final LibraryHoursService service, final Clock clock, final long expires) {
        this.service = service;
        this.clock = clock;
        this.expires = expires;
        this.hours = getLatestHours(LocalDate.now(AMERICA_LA));
        this.nextUpdate = clock.millis() + expires;
        this.lock = new Object();
    }

    public String getHours() {
        return toString(LocalDate.now(AMERICA_LA));
    }

    String toString(final LocalDate localDate) {
        synchronized (this.lock) {
            long now = this.clock.millis();
            if (now >= this.nextUpdate) {
                this.hours = getLatestHours(localDate);
                this.nextUpdate = now + this.expires;
            }
        }
        return this.hours;
    }

    private final String getLatestHours(final LocalDate localDate) {
        String latestHours;
        try {
            Hours calendarHours = this.service.getHours(localDate);
            if (calendarHours == null) {
                log.error("failed to get hours for {} from service", localDate);
                latestHours = UNKNOWN;
            } else if (calendarHours.isClosed()) {
                latestHours = "CLOSED";
            } else {
                latestHours = String.format("%s – %s", calendarHours.getOpen().format(FORMAT).toLowerCase(Locale.US),
                        calendarHours.getClose().format(FORMAT).toLowerCase(Locale.US));
            }
        } catch (LibraryHoursException e) {
            log.error("failed to get hours from service", e);
            latestHours = UNKNOWN;
        }
        return latestHours;
    }
}
