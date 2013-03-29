package edu.stanford.irt.laneweb.ipgroup;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class IPGroupSelector implements Selector {

    public boolean select(final String expression, final Map<String, Object> objectModel, final Map<String, String> parameters) {
        IPGroup group = ModelUtil.getObject(objectModel, Model.IPGROUP, IPGroup.class);
        return expression.equals(group.toString());
    }
}
