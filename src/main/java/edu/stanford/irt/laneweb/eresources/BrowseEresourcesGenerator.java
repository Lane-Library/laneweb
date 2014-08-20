package edu.stanford.irt.laneweb.eresources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public class BrowseEresourcesGenerator extends AbstractEresourcesGenerator {

    private static final String ALL = "all";

    private String alpha;

    private String subset;

    private String type;

    public BrowseEresourcesGenerator(final String type, final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        super(type, collectionManager, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
        this.subset = ModelUtil.getString(model, Model.SUBSET);
        this.alpha = ModelUtil.getString(model, Model.ALPHA, ALL);
        if (this.alpha.length() == 0) {
            this.alpha = ALL;
        }
        if (!ALL.equals(this.alpha) && this.alpha.length() > 1) {
            this.alpha = this.alpha.substring(0, 1);
        }
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = decode(parameters.get(Model.TYPE));
        }
        if (parameters.containsKey(Model.SUBSET)) {
            this.subset = decode(parameters.get(Model.SUBSET));
        }
    }

    @Override
    protected StringBuilder createKey() {
        return super.createKey().append(";a=").append(null == this.alpha ? "" : this.alpha).append(";t=")
                .append(null == this.type ? "" : this.type).append(";s=")
                .append(null == this.subset ? "" : this.subset);
    }

    @Override
    protected List<Eresource> getEresourceList(final CollectionManager collectionManager) {
        List<Eresource> list = null;
        if (this.subset == null && this.type == null) {
            list = Collections.emptyList();
        } else if (this.subset == null && ALL.equals(this.alpha)) {
            list = collectionManager.getType(this.type);
        } else if (this.subset == null) {
            list = collectionManager.getType(this.type, this.alpha.charAt(0));
        } else {
            list = collectionManager.getSubset(this.subset);
        }
        return list;
    }

    @Override
    protected String getHeading() {
        String heading = null;
        if (this.subset == null && this.type.indexOf("software, installed") == -1) {
            heading = this.alpha.toUpperCase();
        }
        return heading;
    }

    @Override
    protected PagingData getPagingData(final List<Eresource> eresources, final int page, final String baseQuery) {
        return new EresourceListPagingData(eresources, page, baseQuery, this.alpha);
    }

    /**
     * A convenience method to URLDecode a string.
     *
     * @param string
     *            a string to decode
     * @return the decoded string
     */
    private String decode(final String string) {
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException("won't happen", e);
        }
    }
}
