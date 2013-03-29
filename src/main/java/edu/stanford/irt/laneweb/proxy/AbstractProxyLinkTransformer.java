package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractProxyLinkTransformer extends AbstractTransformer implements ModelAware {

    private String baseProxyURL;

    public void setModel(final Map<String, Object> model) {
        this.baseProxyURL = ModelUtil.getString(model, Model.BASE_PROXY_URL);
    }

    protected String createProxyLink(final String link) {
        StringBuilder sb = new StringBuilder(this.baseProxyURL);
        return sb.append(link).toString();
    }
}
