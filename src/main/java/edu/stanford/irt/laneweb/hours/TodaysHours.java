package edu.stanford.irt.laneweb.hours;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import edu.stanford.irt.laneweb.LanewebException;

public class TodaysHours {

    public static final String UNKNOWN = "??";

    private Map<String, String> daysMap = new HashMap<String, String>();

    private Resource hoursFileResource = null;

    private long hoursLastModified = 0;

    private final SimpleDateFormat todaysDateFormat = new SimpleDateFormat("MMM d");

    private final SimpleDateFormat todaysDayFormat = new SimpleDateFormat("EEEE");

    public TodaysHours(final String hoursPath) {
        if (null == hoursPath) {
            throw new IllegalArgumentException("null hoursPath");
        }
        this.hoursFileResource = new FileSystemResource(hoursPath);
    }

    @Override
    public String toString() {
        return toString(null);
    }

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
        try {
            if (this.hoursFileResource.lastModified() > this.hoursLastModified) {
                this.hoursLastModified = this.hoursFileResource.lastModified();
                this.daysMap = new StreamDaysMapping(this.hoursFileResource.getInputStream());
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
