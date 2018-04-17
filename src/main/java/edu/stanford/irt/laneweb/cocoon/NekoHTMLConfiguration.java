package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;
import java.util.Map.Entry;

import org.cyberneko.html.HTMLConfiguration;

public final class NekoHTMLConfiguration extends HTMLConfiguration {

    public NekoHTMLConfiguration(final Map<String, String> properties, final Map<String, Boolean> features) {
        for (Entry<String, String> entry : properties.entrySet()) {
            setProperty(entry.getKey(), entry.getValue());
        }
        for (Entry<String, Boolean> entry : features.entrySet()) {
            setFeature(entry.getKey(), entry.getValue());
        }
    }
}
