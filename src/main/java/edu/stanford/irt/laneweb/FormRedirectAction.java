package edu.stanford.irt.laneweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

/**
 * used to redirect form submissions where the source value is a http address.
 * This substitutes the q parameter into the http address and returns the value
 * in the return map.
 * 
 * @author ceyates
 */
public class FormRedirectAction extends AbstractAction implements ThreadSafe {

    private static final String FORM_REDIRECT_KEY = "form-redirect-key";

    private static final String Q = "q";

    private static final String SOURCE = "source";

    private static final String REPLACE = "\\{search-terms\\}";

    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String src,
            final Parameters parameters) {
        Map<String, String> result = new HashMap<String, String>();
        Request request = ObjectModelHelper.getRequest(objectModel);
        String q = request.getParameter(Q);
        String source = request.getParameter(SOURCE);
        if ((null == q) || (q.length() == 0)) {
            throw new IllegalArgumentException("null or empty query");
        }
        if ((null == source) || (source.length() == 0) || (source.indexOf("http") != 0)) {
            throw new IllegalArgumentException("bad source parameter " + source);
        }
        String url = null;
        try {
            url = source.replaceAll(REPLACE, URLEncoder.encode(q, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        result.put(FORM_REDIRECT_KEY, url);
        return result;
    }

}
