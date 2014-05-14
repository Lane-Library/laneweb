package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractCMELinkTransformer extends AbstractXMLPipe implements Transformer, ModelAware {

    private static final String SHC_EMRID_ARGS = "unid=?&srcsys=epic90710&eiv=2.1.0";

    private static final String SU_SUNETID_ARGS = "unid=?&srcsys=EZPX90710&eiv=2.1.0";

    private static final String UTD_CME_URL = "http://www.uptodate.com/online/content/search.do?";

    private static final String UTD_HOST = "www.uptodate.com";

    private String emrid;

    private String sunetHash;

    protected String createCMELink(final String link) {
        StringBuilder sb = new StringBuilder();
        String id = null;
        String args = null;
        if (this.emrid != null) {
            id = this.emrid;
            args = SHC_EMRID_ARGS;
        } else if (this.sunetHash != null) {
            id = this.sunetHash;
            args = SU_SUNETID_ARGS;
        }
        if (link.contains("?")) {
            sb.append(link).append("&").append(args.replaceFirst("\\?", id));
        } else if (link.endsWith("/") || link.endsWith("online")) {
            sb.append(UTD_CME_URL).append(args.replaceFirst("\\?", id));
        } else {
            sb.append(link);
        }
        return sb.toString();
    }

    protected String getEmrid() {
        return this.emrid;
    }

    protected String getSunetHash() {
        return this.sunetHash;
    }

    protected boolean isCMEHost(final String link) {
        return link.contains(UTD_HOST);
    }

    public void setModel(final Map<String, Object> model) {
        this.emrid = ModelUtil.getString(model, Model.EMRID);
        this.sunetHash = ModelUtil.getString(model, Model.AUTH);
    }
}