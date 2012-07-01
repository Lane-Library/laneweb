package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator implements ParametersAware, ModelAware {

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected int page;

    protected String subset;

    protected String type;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    public AbstractEresourcesGenerator(final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    public void setModel(final Map<String, Object> model) {
        this.type = ModelUtil.getString(model, Model.TYPE);
        this.subset = ModelUtil.getString(model, Model.SUBSET);
        this.alpha = ModelUtil.getString(model, Model.ALPHA);
        if (this.alpha != null && this.alpha.length() > 1) {
            //TODO: probably should not use alpha = null for all
            if ("all".equals(this.alpha)) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1);
            }
        }
        this.mesh = ModelUtil.getString(model, Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
        String page = ModelUtil.getString(model, Model.PAGE, "1");
        this.page = "all".equals(page) ? -1 : Integer.parseInt(page) - 1;
    }

    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
        if (parameters.containsKey(Model.SUBSET)) {
            this.subset = parameters.get(Model.SUBSET);
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(new PagingEresourceList(getEresourceList(), this.page), xmlConsumer);
    }

    protected abstract Collection<edu.stanford.irt.eresources.Eresource> getEresourceList();
}
