package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class ProxyLinkSelector implements Selector {

    public boolean select(final String expression, final Map<String, Object> objectModel, final Map<String, String> parameters) {
        return ModelUtil.getObject(objectModel, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE).booleanValue();
    }
}
