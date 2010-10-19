package edu.stanford.irt.laneweb.servlet.redirect;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRedirectProcessor implements RedirectProcessor {

    private Map<Pattern, String> redirectMap = Collections.emptyMap();

    /**
     * @see edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor#getRedirectURL(java.lang.String)
     */
    public String getRedirectURL(final String uri) {
        for (Entry<Pattern, String> entry : this.redirectMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(uri);
            if (matcher.matches()) {
                return matcher.replaceAll(entry.getValue());
            }
        }
        return null;
    }

    public void setRedirectMap(final Map<String, String> redirectMap) {
        if (null == redirectMap) {
            throw new IllegalArgumentException("null redirectMap");
        }
        Map<Pattern, String> newRedirectMap = new LinkedHashMap<Pattern, String>();
        for (Entry<String, String> entry : redirectMap.entrySet()) {
            newRedirectMap.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
        this.redirectMap = newRedirectMap;
    }
}
