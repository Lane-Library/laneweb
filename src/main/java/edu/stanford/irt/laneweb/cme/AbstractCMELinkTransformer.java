package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractCMELinkTransformer extends AbstractXMLPipe implements Transformer, ModelAware {

    protected static final String CME_REDIRECT = "/redirect/cme?url=";

    private static final String UTD_HOST = "www.uptodate.com";

    private String basePath;

    private String emrid;

    @Override
    public void setModel(final Map<String, Object> model) {
        this.emrid = ModelUtil.getString(model, Model.EMRID);
        this.basePath = ModelUtil.getString(model, Model.BASE_PATH);
    }

    protected String getBasePath() {
        return this.basePath;
    }

    protected String getEmrid() {
        return this.emrid;
    }

    protected boolean isCMEHost(final String link) {
        return link.contains(UTD_HOST);
    }
}
