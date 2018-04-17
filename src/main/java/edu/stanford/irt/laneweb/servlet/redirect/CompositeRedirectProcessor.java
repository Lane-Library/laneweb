package edu.stanford.irt.laneweb.servlet.redirect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeRedirectProcessor implements RedirectProcessor {

    private List<RedirectProcessor> redirectProcessors = Collections.emptyList();

    public CompositeRedirectProcessor(final List<RedirectProcessor> redirectProcessors) {
        this.redirectProcessors = new ArrayList<>(redirectProcessors);
    }

    @Override
    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        String redirectURI = null;
        for (RedirectProcessor redirectProcessor : this.redirectProcessors) {
            redirectURI = redirectProcessor.getRedirectURL(uri, basePath, queryString);
            if (redirectURI != null) {
                break;
            }
        }
        return redirectURI;
    }
}
