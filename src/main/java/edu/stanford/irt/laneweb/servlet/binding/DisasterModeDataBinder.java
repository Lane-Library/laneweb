package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class DisasterModeDataBinder implements DataBinder {

    private Boolean disasterMode;

    public DisasterModeDataBinder(final Boolean disasterMode) {
        this.disasterMode = disasterMode == null ? Boolean.FALSE : disasterMode;
    }

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.DISASTER_MODE, this.disasterMode);
    }
}
