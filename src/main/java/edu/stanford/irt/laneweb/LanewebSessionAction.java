package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * creates a session object
 * 
 * @author ceyates
 */

public class LanewebSessionAction implements Action {

    public Map act(final Redirector arg0, final SourceResolver arg1, final Map objectModel, final String arg3, final Parameters arg4)
            throws Exception {
        ObjectModelHelper.getRequest(objectModel).getSession();
        return null;
    }

}
