package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CMELinkSelector implements Selector {

    private static final String EMPTY_STRING = "";

    @Override
    public boolean select(final String expression, final Map<String, Object> objectModel,
            final Map<String, String> parameters) {
        return hasEmrid(objectModel) || isProxyNotHospital(objectModel);
    }

    private boolean hasEmrid(final Map<String, Object> objectModel) {
        return !EMPTY_STRING.equals(ModelUtil.getString(objectModel, Model.EMRID, EMPTY_STRING));
    }

    private boolean isNotHospital(final Map<String, Object> objectModel) {
        IPGroup ipGroup = ModelUtil.getObject(objectModel, Model.IPGROUP, IPGroup.class, IPGroup.ERR);
        return !(IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup));
    }

    private boolean isProxy(final Map<String, Object> objectModel) {
        return ModelUtil.getObject(objectModel, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE).booleanValue();
    }

    private boolean isProxyNotHospital(final Map<String, Object> objectModel) {
        if (isProxy(objectModel)) {
            return isNotHospital(objectModel);
        }
        return false;
    }
}
