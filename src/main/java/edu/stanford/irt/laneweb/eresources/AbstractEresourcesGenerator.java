package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator implements CacheableProcessingComponent,
        ParametersAware, ModelAware {

    private static final long DEFAULT_EXPIRES = 1000 * 60 * 5;

    private String alpha;

    private CollectionManager collectionManager;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private String key;

    private String mesh;

    private int page;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private String subset;

    private String type;

    private SourceValidity validity;

    public AbstractEresourcesGenerator(final String componentType, final CollectionManager collectionManager,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        this.componentType = componentType;
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey();
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
        this.mesh = ModelUtil.getString(model, Model.MESH);
        if (this.mesh != null) {
            this.mesh = this.mesh.toLowerCase();
        }
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(p) ? -1 : Integer.parseInt(p) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
    }

    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
        if (parameters.containsKey(Model.SUBSET)) {
            this.subset = parameters.get(Model.SUBSET);
        }
        if (parameters.containsKey(Model.EXPIRES)) {
            this.expires = Long.parseLong(parameters.get(Model.EXPIRES));
        }
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(new PagingEresourceList(getEresourceList(), this.page), xmlConsumer);
    }

    protected Collection<Eresource> getCore() {
        if (this.type == null) {
            return Collections.emptySet();
        }
        return this.collectionManager.getCore(this.type);
    }

    protected abstract Collection<Eresource> getEresourceList();

    protected Collection<Eresource> getMesh() {
        if (this.mesh == null || this.type == null) {
            return Collections.emptySet();
        }
        return this.collectionManager.getMesh(this.type, this.mesh);
    }

    protected Collection<Eresource> getTypeOrSubset() {
        Collection<Eresource> list = null;
        if (this.subset == null && this.type == null) {
            list = Collections.emptySet();
        } else if (this.subset == null && this.alpha == null) {
            list = this.collectionManager.getType(this.type);
        } else if (this.subset == null) {
            list = this.collectionManager.getType(this.type, this.alpha.charAt(0));
        } else {
            list = this.collectionManager.getSubset(this.subset);
        }
        return list;
    }

    private String createKey() {
        return new StringBuilder("t=").append(null == this.type ? "" : this.type).append(";s=")
                .append(null == this.subset ? "" : this.subset).append(";a=").append(null == this.alpha ? "" : this.alpha)
                .append(";m=").append(null == this.mesh ? "" : this.mesh).append(";page=").append(this.page).toString();
    }
}
