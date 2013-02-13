package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BrowseEresourcesGenerator extends AbstractEresourcesGenerator {

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
        this.alpha = ModelUtil.getString(model, Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            // TODO: probably should not use alpha = null for all
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        super.setParameters(parameters);
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
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
    protected Collection<Eresource> getEresourceList(final CollectionManager collectionManager) {
        Collection<Eresource> list = null;
        if (this.subset == null && this.type == null) {
            list = Collections.emptySet();
        } else if (this.subset == null && this.alpha == null) {
            list = collectionManager.getType(this.type);
        } else if (this.subset == null) {
            list = collectionManager.getType(this.type, this.alpha.charAt(0));
        } else {
            list = collectionManager.getSubset(this.subset);
        }
        return list;
    }

    @Override
    protected PagingData getPagingData(final Collection<Eresource> eresources, final int page) {
        if (this.alpha == null) {
            return new PagingData(eresources, page);
        } else {
            return new PagingData(eresources, page, "a=" + this.alpha);
        }
    }
}
