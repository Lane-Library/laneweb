package edu.stanford.irt.laneweb.servlet.binding;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class TodaysHoursBinder implements DataBinder {

    private Map<String, TodaysHours> hours = Collections.synchronizedMap(new HashMap<String, TodaysHours>());

    private final String hoursPath;
    
    private final Logger log;
    
    public TodaysHoursBinder(final String hoursPath, final Logger log) {
        this.hoursPath = hoursPath;
        this.log = log;
    }

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String basePath = ModelUtil.getString(model, Model.BASE_PATH);
        if (null == this.hours.get(basePath)) {
            URL contentBase = ModelUtil.getObject(model, Model.CONTENT_BASE, URL.class);
            this.hours.put(basePath, new TodaysHours(contentBase.getPath() + this.hoursPath));
        }
        String todaysHoursString = null;
        try {
            todaysHoursString = this.hours.get(basePath).toString();
        } catch (LanewebException e) {
            this.log.error(e.getMessage(), e);
            todaysHoursString = TodaysHours.UNKNOWN;
        }
        model.put(Model.TODAYS_HOURS, todaysHoursString);
    }
}
