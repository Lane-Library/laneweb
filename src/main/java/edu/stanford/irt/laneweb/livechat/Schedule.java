package edu.stanford.irt.laneweb.livechat;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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

    /**
     * @deprecated use isAvailableAt(LocalDate localDate)
     * @param date
     *            can be null in which case the current time is used
     * @return whether or not live chat is available at a particular time.
     */
    @Deprecated
    public boolean isAvailableAt(final Date date) {
        if (date == null) {
            return isAvailable();
        } else {
            return isAvailableAt(ZonedDateTime.ofInstant(date.toInstant(), AMERICA_LA));
        }
    }

    public boolean isAvailableAt(final ZonedDateTime dateTime) {
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
