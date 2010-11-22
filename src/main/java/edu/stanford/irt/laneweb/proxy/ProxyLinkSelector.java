package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;


public class ProxyLinkSelector implements Selector {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean select(String expression, Map objectModel, Parameters parameters) {
        return ModelUtil.getObject(objectModel, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE).booleanValue();
    }
}
