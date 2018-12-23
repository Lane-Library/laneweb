package edu.stanford.irt.laneweb.eresources.browse;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;

public abstract class AbstractEresourcesGenerator extends AbstractGenerator {

    /** the default cache expiration time, 20 minutes */
    private static final long DEFAULT_EXPIRES = Duration.ofMinutes(20).toMillis();

    private String componentType;

    private long expires = DEFAULT_EXPIRES;

    private String key;

    private int page;

    private String queryString;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SolrService solrService;

    private Validity validity;

    public AbstractEresourcesGenerator(final String componentType, final SolrService solrService,
            final SAXStrategy<PagingEresourceList> saxStrategy) {
        this.componentType = componentType;
        this.solrService = solrService;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public Serializable getKey() {
        if (null == this.key) {
            this.key = createKey().toString();
        }
        return this.key;
    }

    @Override
    public String getType() {
        return this.componentType;
    }

    @Override
    public Validity getValidity() {
        if (this.validity == null) {
            this.validity = new ExpiresValidity(this.expires);
        }
        return this.validity;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
    public void setModel(final Map<String, Object> model) {
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        if ("all".equals(p)) {
            this.page = -1;
        } else {
            try {
                this.page = Integer.parseInt(p) - 1;
            } catch (NumberFormatException e) {
                this.page = 0;
            }
        }
        this.queryString = ModelUtil.getString(model, Model.QUERY_STRING, "");
    }

    /**
     * @deprecated this will be replaced with constructor injection
     */
    @Override
    @Deprecated
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
        List<Eresource> eresources = getEresourceList(this.solrService);
        String baseQuery = this.queryString;
        if (baseQuery.indexOf("&page=") > -1) {
            baseQuery = baseQuery.substring(0, baseQuery.indexOf("&page="));
        } else if (baseQuery.indexOf("page=") == 0) {
            baseQuery = "";
        }
        PagingData pagingData = getPagingData(eresources, this.page, baseQuery);
        this.saxStrategy.toSAX(new PagingEresourceList(eresources, pagingData, getHeading()), xmlConsumer);
    }

    protected abstract List<Eresource> getEresourceList(SolrService solrService);

    protected abstract String getHeading();

    protected PagingData getPagingData(final List<Eresource> eresources, final int page, final String baseQuery) {
        return new EresourceListPagingData(eresources, page, baseQuery, null);
    }
}
