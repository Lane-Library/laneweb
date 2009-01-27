package edu.stanford.irt.laneweb;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.InputModule;

public class MTMungingInputModule implements InputModule {
    
    private Pattern pattern = Pattern.compile("[_\\W\\s]+");

    @SuppressWarnings("unchecked")
    public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
        return this.pattern.matcher(name).replaceAll(" ").trim().replace(' ', '_').toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public Iterator getAttributeNames(Configuration modeConf, Map objectModel) throws ConfigurationException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Object[] getAttributeValues(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
        throw new UnsupportedOperationException();
    }

}
