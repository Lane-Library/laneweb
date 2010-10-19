package edu.stanford.irt.laneweb.servlet.redirect;

import java.util.Collections;
import java.util.List;


public class CompositeRedirectProcessor implements RedirectProcessor {
    
    private List<RedirectProcessor> redirectProcessors = Collections.emptyList();

    public String getRedirectURL(String uri) {
        String redirectURI = null;
        for (RedirectProcessor redirectProcessor : this.redirectProcessors) {
            redirectURI = redirectProcessor.getRedirectURL(uri);
            if (redirectURI != null) {
                break;
            }
        }
        return redirectURI;
    }
    
    public void setRedirectProcessors(List<RedirectProcessor> redirectProcessors) {
        this.redirectProcessors = redirectProcessors;
    }
}
