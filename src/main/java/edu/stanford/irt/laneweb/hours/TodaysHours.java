package edu.stanford.irt.laneweb.hours;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.LanewebException;

public class TodaysHours {

    public static final String UNKNOWN = "??";

    private Map<String, String> daysMap = new HashMap<>();

    private File hoursFile = null;

    private long hoursLastModified = 0;

    private DateTimeFormatter todaysDateFormatter = DateTimeFormatter.ofPattern("MMM d");

    private DateTimeFormatter todaysDayFormatter = DateTimeFormatter.ofPattern("EEEE");

    public TodaysHours(final File hoursFile) {
        this.hoursFile = hoursFile;
    }

    public String getHours() {
        return toString(LocalDate.now());
    }

    /**
     * @deprecated, use toString(LocalDate localDate)
     * 
     * @param date
     * @return a String representation of the library hours on the given date;
     */
    @Deprecated
    public String toString(final Date date) {
        return toString(date.toInstant().atZone(ZoneId.of("America/Los_Angeles")).toLocalDate());
    }

    public String toString(final LocalDate localDate) {
        String todaysDate = this.todaysDateFormatter.format(localDate);
        String todaysDay = this.todaysDayFormatter.format(localDate);
        updateHoursMap();
        String hours;
        if (this.daysMap.containsKey(todaysDate)) {
            hours = this.daysMap.get(todaysDate);
        } else if (this.daysMap.containsKey(todaysDay)) {
            hours = this.daysMap.get(todaysDay);
        } else {
            hours = UNKNOWN;
        }
        return hours;
    }

    private void updateHoursMap() {
        synchronized (this.hoursFile) {
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
}
