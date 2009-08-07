package edu.stanford.irt.laneweb;

import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;


public class CreateSessionAction implements Action {

    public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) {
        ObjectModelHelper.getRequest(objectModel).getSession(true);
        return Collections.emptyMap();
    }
}
