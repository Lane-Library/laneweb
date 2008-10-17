package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

public class JavascriptLoggerAction extends AbstractAction implements ThreadSafe {

    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string,
            final Parameters param) throws Exception {
//        Logger log = getLogger();
        Request request = ObjectModelHelper.getRequest(objectModel);
//        String userAgent = request.getParameter("userAgent");
//        String message = request.getParameter("message");
//        String line = request.getParameter("line");
//        String url = request.getParameter("url");
//        StringBuffer logs = new StringBuffer("\n");
//        logs.append("userAgent=");
//        if (message != null) {
//            logs.append(userAgent);
//        }
//        logs.append("\nmessage=");
//        if (message != null) {
//            logs.append(message);
//        }
//        logs.append("\nline=");
//        if (line != null) {
//            logs.append(line);
//        }
//        logs.append("\nurl=");
//        if (url != null) {
//            logs.append(url);
//        }
//        log.error(logs.toString());
        getLogger().error(request.getQueryString());
        return EMPTY_MAP;
    }

}
