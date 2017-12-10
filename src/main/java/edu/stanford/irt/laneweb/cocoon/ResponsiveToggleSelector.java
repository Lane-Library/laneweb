package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class ResponsiveToggleSelector implements Selector {

    @Override
    public boolean select(final String expression, final Map<String, Object> objectModel,
            final Map<String, String> parameters) {
        return ModelUtil.getObject(objectModel, Model.RESPONSIVE, Boolean.class, Boolean.FALSE).booleanValue();
    }
}
