package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.model.DefaultModelAware;
import edu.stanford.irt.laneweb.model.Model;


public class ProxyLinkSelector extends DefaultModelAware implements Selector {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean select(String expression, Map objectModel, Parameters parameters) {
        return getObject(objectModel, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE).booleanValue();
    }
}
