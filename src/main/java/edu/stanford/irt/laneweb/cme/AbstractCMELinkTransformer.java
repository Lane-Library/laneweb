package edu.stanford.irt.laneweb.cme;

import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractCMELinkTransformer extends AbstractTransformer implements ModelAware {

    private static final String UTD_CME_ARGS = "unid=?&srcsys=epic90710&eiv=2.1.0";

    private static final String UTD_CME_URL = "http://www.uptodate.com/online/content/search.do?";

    private static final String[] UTD_HOSTS = { "www.utdol.com", "www.uptodate.com" };

    protected String emrid;

    public void setModel(final Map<String, Object> model) {
        this.emrid = ModelUtil.getString(model, Model.EMRID);
    }

    protected String createCMELink(final String link) {
        StringBuffer sb = new StringBuffer();
        if (link.contains("?")) {
            sb.append(link).append("&").append(UTD_CME_ARGS.replaceFirst("\\?", this.emrid));
        } else if (link.endsWith("/") || link.endsWith("online")) {
            sb.append(UTD_CME_URL).append(UTD_CME_ARGS.replaceFirst("\\?", this.emrid));
        } else {
            sb.append(link);
        }
        return sb.toString();
    }

    protected boolean isCMEHost(final String link) {
        for (String host : UTD_HOSTS) {
            if (link.contains(host)) {
                return true;
            }
        }
        return false;
    }
}