package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.http.HttpEnvironment;

public class LanewebEnvironment extends HttpEnvironment {

    public LanewebEnvironment(final String uri, Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res, final ServletContext servletContext,
            final Context context) throws IOException {
        super(uri, req, res, servletContext, context, null, null);
        model.putAll(this.objectModel);
        this.objectModel = model;
        super.setContentType(servletContext.getMimeType(uri));
    }
    
    
}
