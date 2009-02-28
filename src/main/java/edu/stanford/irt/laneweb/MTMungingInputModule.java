package edu.stanford.irt.laneweb;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.components.modules.input.InputModule;

/**
 * this InputModule takes a parameter string and munges it the way MoveableType
 * does so that the category label resolves to the correct category archive
 * document. it also is useful in translating the formerly used _id form for faq
 * ids to the id without the preceeding underscore.
 * 
 * @author ceyates
 */
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
