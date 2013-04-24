package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator implements CacheablePipelineComponent,
        ParametersAware, ModelAware {

    private static final long DEFAULT_EXPIRES = 1000 * 60 * 5;

    private CollectionManager collectionManager;

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private String key;

    private int page;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private Validity validity;

	private String queryString;

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

    public Validity getValidity() {
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
        this.queryString = ModelUtil.getString(model, Model.QUERY_STRING, "");
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
        Collection<Eresource> eresources = getEresourceList(this.collectionManager);
        //coerce the Collection to a List for PagingData
        List<Eresource> list = null;
        if (eresources instanceof List) {
        	list = (List) eresources;
        } else {
        	list = new LinkedList<Eresource>(eresources);
        }
        String baseQuery = this.queryString;
        if (baseQuery.indexOf("&page=") > 0) {
        	baseQuery = baseQuery.substring(0, baseQuery.indexOf("&page="));
        } else if (baseQuery.indexOf("page=") == 0) {
        	baseQuery = "";
        }
        PagingData pagingData = new EresourceListPagingData(list, this.page, baseQuery);
        this.saxStrategy.toSAX(new PagingEresourceList(list, pagingData), xmlConsumer);
    }

    protected abstract Collection<Eresource> getEresourceList(CollectionManager collectionManager);
}
