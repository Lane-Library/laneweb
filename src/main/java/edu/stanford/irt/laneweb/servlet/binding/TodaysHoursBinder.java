package edu.stanford.irt.laneweb.servlet.binding;

import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.model.Model;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TodaysHoursBinder implements DataBinder {

    private Map<String, TodaysHours> hours = Collections.synchronizedMap(new HashMap<String, TodaysHours>());

    private String hoursPath;

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        if (null == this.hoursPath) {
            throw new IllegalArgumentException("null hoursPath");
        }
        String basePath = request.getAttribute(Model.BASE_PATH).toString();
        if (null == this.hours.get(basePath)) {
            URL contentBase = (URL) request.getAttribute(Model.CONTENT_BASE);
            this.hours.put(basePath, new TodaysHours(contentBase.getPath() + this.hoursPath));
        }
        model.put(Model.TODAYS_HOURS, this.hours.get(basePath).toString());
    }

    public void setHoursPath(final String hoursPath) {
        this.hoursPath = hoursPath;
    }
}
