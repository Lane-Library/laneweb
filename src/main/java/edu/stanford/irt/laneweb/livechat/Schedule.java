package edu.stanford.irt.laneweb.livechat;

import java.util.Calendar;
import java.util.Date;

/**
 * <code>Schedule</code> contains the logic for the live chat schedule.
 */
public class Schedule {
    
    
    /**
     * 
     * @param date can be null in which case the current time is used
     * @return whether or not live chat is available at a particular time.
     */
    public boolean isAvailableAt(Date date) {
        boolean available = false;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
            available = hourOfDay >= 12 && hourOfDay < 17;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            available = hourOfDay >= 9 && hourOfDay < 17;
        } else {
            available = hourOfDay >= 8 && hourOfDay < 19;
        }
        return available;
    }
    
    public boolean isAvailable() {
        return isAvailableAt(null);
    }
}
