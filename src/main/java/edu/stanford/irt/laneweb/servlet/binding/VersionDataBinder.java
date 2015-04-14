package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class VersionDataBinder implements DataBinder {

    private static final String VERSION_INIT_PARAMETER_NAME = "laneweb.context.version";

    private String version;

    public VersionDataBinder(final ServletContext servletContext) {
        this.version = servletContext.getInitParameter(VERSION_INIT_PARAMETER_NAME);
        if (this.version == null) {
            throw new LanewebException("unable to retrieve version from context");
        }
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.VERSION, this.version);
    }
}
