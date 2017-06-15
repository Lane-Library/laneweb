package edu.stanford.irt.laneweb.servlet.redirect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultRedirectProcessor implements RedirectProcessor {

    private Map<Pattern, String> redirectMap;

    public DefaultRedirectProcessor(final Map<String, String> redirectMap) {
        if (null == redirectMap) {
            throw new IllegalArgumentException("null redirectMap");
        }
        Map<Pattern, String> newRedirectMap = new LinkedHashMap<>();
        for (Entry<String, String> entry : redirectMap.entrySet()) {
            newRedirectMap.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
        this.redirectMap = newRedirectMap;
    }

    @Override
    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        String testURI = queryString == null ? uri : uri + '?' + queryString;
        for (Entry<Pattern, String> entry : this.redirectMap.entrySet()) {
            Matcher matcher = entry.getKey().matcher(testURI);
            if (matcher.matches()) {
                String result = matcher.replaceAll(entry.getValue());
                if (result.charAt(0) == '/') {
                    return basePath + result;
                } else {
                    return result;
                }
            }
        }
        return null;
    }
}
