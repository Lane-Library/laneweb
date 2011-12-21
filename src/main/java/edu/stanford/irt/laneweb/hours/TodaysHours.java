package edu.stanford.irt.laneweb.hours;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class TodaysHours {

    private static final SimpleDateFormat TODAYS_DATE_FORMAT = new SimpleDateFormat("MMM d");

    private static final SimpleDateFormat TODAYS_DAY_FORMAT = new SimpleDateFormat("EEEE");

    private static final String UNKNOWN = "??";

    private HashMap<String, String> daysMap = new HashMap<String, String>();

    private Resource hoursFileResource;

    private long hoursLastModified = 0;

    private final Logger log = LoggerFactory.getLogger(TodaysHours.class);

    public TodaysHours(final String hoursPath) {
        if (null == hoursPath) {
            throw new IllegalArgumentException("null hoursPath");
        }
        if (null == this.hoursFileResource) {
            this.hoursFileResource = new FileSystemResource(new File(hoursPath));
        }
        updateHoursMap();
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(final Date date) {
        Date today;
        if (null == date) {
            today = new Date();
        } else {
            today = date;
        }
        String todaysDate = TODAYS_DATE_FORMAT.format(today);
        String todaysDay = TODAYS_DAY_FORMAT.format(today);
        updateHoursMap();
        if (this.daysMap.containsKey(todaysDate)) {
            return this.daysMap.get(todaysDate);
        } else if (this.daysMap.containsKey(todaysDay)) {
            return this.daysMap.get(todaysDay);
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
            this.log.error("can't find hours file", e);
        }
    }
}
