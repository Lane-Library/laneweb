package edu.stanford.irt.laneweb;

import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;

public class VersionHeaderAction implements Action, ThreadSafe, Parameterizable {
    
    private String version;

    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters params) {
        Response response = ObjectModelHelper.getResponse(objectModel);
        if (null != this.version) {
            response.addHeader("X-Laneweb-Version", this.version);
        }
        return Collections.emptyMap();
    }

    public void parameterize(Parameters params) throws ParameterException {
        this.version = params.getParameter("laneweb-version");
    }

}
