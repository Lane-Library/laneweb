package edu.stanford.irt.laneweb.livechat;

import java.util.Calendar;
import java.util.Date;

/**
 * <code>Schedule</code> contains the logic for the live chat schedule.
 */
public class Schedule {

    private static final int FIVE_PM = 17;

    private static final int NINE_AM = 9;

    private static final int NOON = 12;

    private static final int SEVEN_PM = 19;

    public boolean isAvailable() {
        return isAvailableAt(null);
    }

    /**
     * @param date
     *            can be null in which case the current time is used
     * @return whether or not live chat is available at a particular time.
     */
    public boolean isAvailableAt(final Date date) {
        boolean available;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
            available = hourOfDay >= NOON && hourOfDay < FIVE_PM;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            available = hourOfDay >= NINE_AM && hourOfDay < FIVE_PM;
        } else {
            available = hourOfDay >= NINE_AM && hourOfDay < SEVEN_PM;
        }
        return available;
    }
}
