package edu.stanford.irt.laneweb.servlet.binding;

import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.model.Model;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TodaysHoursBinder implements DataBinder {

    private TodaysHours hours;

    private String hoursPath;

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        if (null == this.hoursPath) {
            throw new IllegalArgumentException("null hoursPath");
        }
        if (null == this.hours) {
            URL contentBase = (URL) request.getAttribute(Model.CONTENT_BASE);
            this.hours = new TodaysHours(contentBase.getPath() + this.hoursPath);
        }
        model.put(Model.TODAYS_HOURS, this.hours.toString());
    }

    public void setHoursPath(final String hoursPath) {
        this.hoursPath = hoursPath;
    }
}
