package edu.stanford.irt.laneweb.servlet.redirect;


public class SHCRedirectProcessor extends DefaultRedirectProcessor {

    @Override
    public String getRedirectURL(String uri) {
        if (uri.indexOf("/shc") != 0) {
            return null;
        }
        return super.getRedirectURL(uri);
    }
}
