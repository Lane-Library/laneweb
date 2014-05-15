package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CMELinkSelector implements Selector {

    private static final String EMPTY_STRING = "";

    @Override
    public boolean select(final String expression, final Map<String, Object> objectModel,
            final Map<String, String> parameters) {
        return ModelUtil.getObject(objectModel, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE).booleanValue()
                || !EMPTY_STRING.equals(ModelUtil.getString(objectModel, Model.EMRID, EMPTY_STRING));
    }
}
