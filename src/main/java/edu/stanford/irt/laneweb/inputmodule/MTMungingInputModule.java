package edu.stanford.irt.laneweb.inputmodule;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.InputModule;

public class MTMungingInputModule implements InputModule {

    private Pattern pattern = Pattern.compile("[_\\W\\s]+");

    @SuppressWarnings("unchecked")
    public Object getAttribute(final String name, final Configuration modeConf, final Map objectModel) throws ConfigurationException {
        return this.pattern.matcher(name).replaceAll(" ").trim().replace(' ', '_').toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public Iterator getAttributeNames(final Configuration modeConf, final Map objectModel) throws ConfigurationException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Object[] getAttributeValues(final String name, final Configuration modeConf, final Map objectModel) throws ConfigurationException {
        throw new UnsupportedOperationException();
    }
}
