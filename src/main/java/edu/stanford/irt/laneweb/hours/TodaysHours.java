package edu.stanford.irt.laneweb.hours;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.LanewebException;

public class TodaysHours {

    public static final String UNKNOWN = "??";

    private Map<String, String> daysMap = new HashMap<>();

    private File hoursFile = null;

    private long hoursLastModified = 0;

    private final SimpleDateFormat todaysDateFormat = new SimpleDateFormat("MMM d");

    private final SimpleDateFormat todaysDayFormat = new SimpleDateFormat("EEEE");

    public TodaysHours(final File hoursFile) {
        this.hoursFile = hoursFile;
    }

    public String getHours() {
        return toString(LocalDate.now());
    }
    
    public String toString(LocalDate localDate) {
        return toString(Date.from(localDate.atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant()));
    }

    /**
     * @deprecated, use toString(LocalDate localDate)
     * @param date
     * @return a String representation of the library hours on the given date;
     */
    @Deprecated
    public String toString(final Date date) {
        Date today;
        String todaysDate;
        String todaysDay;
        if (null == date) {
            today = new Date();
        } else {
            today = date;
        }
        synchronized (this) {
            todaysDate = this.todaysDateFormat.format(today);
            todaysDay = this.todaysDayFormat.format(today);
            updateHoursMap();
            if (this.daysMap.containsKey(todaysDate)) {
                return this.daysMap.get(todaysDate);
            } else if (this.daysMap.containsKey(todaysDay)) {
                return this.daysMap.get(todaysDay);
            }
        }
        return UNKNOWN;
    }

    private void updateHoursMap() {
        long fileLastModified = this.hoursFile.lastModified();
        if (fileLastModified > this.hoursLastModified) {
            this.hoursLastModified = fileLastModified;
            try (InputStream input = new FileInputStream(this.hoursFile)) {
                this.daysMap = new StreamDaysMapping(input);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
    }
}
