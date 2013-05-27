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
        if (model.containsKey(Model.TYPE)) {
        try {
            this.type = URLDecoder.decode(ModelUtil.getString(model, Model.TYPE), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException("won't happen");
        }
        }
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
            try {
                this.type = URLDecoder.decode(parameters.get(Model.TYPE), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen");
            }
        }
        if (parameters.containsKey(Model.SUBSET)) {
            this.subset = parameters.get(Model.SUBSET);
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
}
