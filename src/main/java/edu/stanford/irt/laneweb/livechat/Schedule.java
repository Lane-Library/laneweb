package edu.stanford.irt.laneweb.livechat;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <code>Schedule</code> contains the logic for the live chat schedule.
 */
public class Schedule {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final int EIGHT_AM = 8;

    private static final int NOON = 12;

    private static final int SEVEN_PM = 19;

    private static final int SIX_PM = 18;

    public boolean isAvailable() {
        return isAvailableAt(ZonedDateTime.now(AMERICA_LA));
    }

    boolean isAvailableAt(final ZonedDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        int hourOfDay = dateTime.getHour();
        boolean available;
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            available = hourOfDay >= NOON && hourOfDay < SIX_PM;
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            available = hourOfDay >= EIGHT_AM && hourOfDay < SIX_PM;
        } else {
            available = hourOfDay >= EIGHT_AM && hourOfDay < SEVEN_PM;
        }
        return available;
    }
}
