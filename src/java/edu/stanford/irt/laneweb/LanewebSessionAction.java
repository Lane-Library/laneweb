package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * creates a session object
 * @author ceyates
 *
 */

public class LanewebSessionAction extends AbstractAction implements ThreadSafe {

	public Map act(Redirector arg0, SourceResolver arg1, Map objectModel, String arg3,
			Parameters arg4) throws Exception {
		ObjectModelHelper.getRequest(objectModel).getSession();
		return null;
	}

}
