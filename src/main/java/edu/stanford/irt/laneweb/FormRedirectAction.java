package edu.stanford.irt.laneweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * used to redirect form submissions where the source value is a http address.
 * These are normally intercepted by javascript in the client. This is here so
 * that if javascript is off in the client, the user will get to the page
 * anyway. This substitutes the q parameter into the http address and returns
 * the value in the return map.
 * 
 * @author ceyates
 */
public class FormRedirectAction implements Action {

    private static final String FORM_REDIRECT_KEY = "form-redirect-key";

    private static final String Q = "q";

    private static final String SOURCE = "source";

    private static final String REPLACE = "\\{search-terms\\}";

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String src,
            final Parameters params) {
        Map<String, String> result = new HashMap<String, String>();
        String q = params.getParameter(Q, null);
        String source = params.getParameter(SOURCE, null);
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
