package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.model.Model;

public class TodaysHoursBinder implements DataBinder {

    private static final Logger log = LoggerFactory.getLogger(TodaysHoursBinder.class);

    private TodaysHours hours;

    public TodaysHoursBinder(final TodaysHours hours) {
        this.hours = hours;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String todaysHoursString = null;
        try {
            todaysHoursString = this.hours.getHours();
        } catch (LanewebException e) {
            log.error(e.getMessage(), e);
            todaysHoursString = TodaysHours.UNKNOWN;
        }
        model.put(Model.TODAYS_HOURS, todaysHoursString);
    }
}
