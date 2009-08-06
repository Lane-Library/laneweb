package edu.stanford.irt.laneweb.inputmodule;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.cocoon.components.modules.input.InputModule;

public class MTMungingInputModule implements InputModule {

    private Pattern pattern = Pattern.compile("[_\\W\\s]+");

    @SuppressWarnings("unchecked")
    public Object getAttribute(final String name, final Configuration modeConf, final Map objectModel) {
        try {
            return this.pattern.matcher(URLDecoder.decode(name,"UTF-8")).replaceAll(" ").trim().replace(' ', '_').toLowerCase();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator getAttributeNames(final Configuration modeConf, final Map objectModel) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Object[] getAttributeValues(final String name, final Configuration modeConf, final Map objectModel) {
        throw new UnsupportedOperationException();
    }
}
