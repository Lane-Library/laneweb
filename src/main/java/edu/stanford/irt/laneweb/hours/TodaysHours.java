package edu.stanford.irt.laneweb.hours;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    String toString(final LocalDate localDate) {
        String todaysDate = this.todaysDateFormatter.format(localDate);
        String todaysDay = this.todaysDayFormatter.format(localDate);
        updateHoursMap();
        // copy this.daysMap in case it changes while the following is working with it
        Map<String, String> daysMapCopy = this.daysMap;
        String hours;
        if (daysMapCopy.containsKey(todaysDate)) {
            hours = daysMapCopy.get(todaysDate);
        } else if (daysMapCopy.containsKey(todaysDay)) {
            hours = daysMapCopy.get(todaysDay);
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
