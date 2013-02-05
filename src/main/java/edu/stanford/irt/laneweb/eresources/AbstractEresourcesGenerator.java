package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.source.ExpiresValidity;
import edu.stanford.irt.cocoon.source.SourceValidity;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator implements CacheablePipelineComponent,
        ParametersAware, ModelAware {

    private static final long DEFAULT_EXPIRES = 1000 * 60 * 5;

    private CollectionManager collectionManager;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private String key;

    private int page;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SourceValidity validity;

    public AbstractEresourcesGenerator(final String componentType, final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        this.componentType = componentType;
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey().toString();
        }
        return this.key;
    }

    public String getType() {
        return this.componentType;
    }

    public SourceValidity getValidity() {
        if (this.validity == null) {
            this.validity = new ExpiresValidity(this.expires);
        }
        return this.validity;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    public void setModel(final Map<String, Object> model) {
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(p) ? -1 : Integer.parseInt(p) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
    }

    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.EXPIRES)) {
            this.expires = Long.parseLong(parameters.get(Model.EXPIRES));
        }
    }

    protected StringBuilder createKey() {
        return new StringBuilder("p=").append(this.page);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(new PagingEresourceList(getEresourceList(this.collectionManager), this.page), xmlConsumer);
    }

    protected abstract Collection<Eresource> getEresourceList(CollectionManager collectionManager);
}
