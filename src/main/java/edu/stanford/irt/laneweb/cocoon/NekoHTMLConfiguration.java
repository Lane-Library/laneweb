package edu.stanford.irt.laneweb.cocoon;

import org.cyberneko.html.HTMLConfiguration;

public class NekoHTMLConfiguration extends HTMLConfiguration {

    public NekoHTMLConfiguration() {
        setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        setProperty("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        setFeature("http://cyberneko.org/html/features/insert-namespaces", true);
    }
}
