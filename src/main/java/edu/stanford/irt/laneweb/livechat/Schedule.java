package edu.stanford.irt.laneweb.livechat;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <code>Schedule</code> contains the logic for the live chat schedule.
 */
public class Schedule {

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final int FIVE_PM = 17;

    private static final int NINE_AM = 9;

    private static final int NOON = 12;

    private static final int SEVEN_PM = 19;

    public boolean isAvailable() {
        return isAvailableAt(ZonedDateTime.now(AMERICA_LA));
    }

    boolean isAvailableAt(final ZonedDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        int hourOfDay = dateTime.getHour();
        boolean available;
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            available = hourOfDay >= NOON && hourOfDay < FIVE_PM;
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            available = hourOfDay >= NINE_AM && hourOfDay < FIVE_PM;
        } else {
            available = hourOfDay >= NINE_AM && hourOfDay < SEVEN_PM;
        }
        return available;
    }
}
