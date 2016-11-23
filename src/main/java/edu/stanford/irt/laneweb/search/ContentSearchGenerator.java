package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 */
public class ContentSearchGenerator extends AbstractMetasearchGenerator<PagingSearchResultList>
        implements ParametersAware {

    private static final long DEFAULT_TIMEOUT = 20000;

    private ContentResultConversionStrategy conversionStrategy;

    private Collection<String> engines;

    private int page;

    private String timeout;

    private String urlEncodedQuery;

    public ContentSearchGenerator(final MetaSearchService metaSearchService,
            final SAXStrategy<PagingSearchResultList> saxStrategy,
            final ContentResultConversionStrategy conversionStrategy) {
        super(metaSearchService, saxStrategy);
        this.conversionStrategy = conversionStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class, Collections.emptyList());
        String p = ModelUtil.getString(model, Model.PAGE, "1");
        try {
            this.page = "all".equals(p) ? -1 : Integer.parseInt(p) - 1;
        } catch (NumberFormatException nfe) {
            this.page = 0;
        }
        this.urlEncodedQuery = ModelUtil.getString(model, Model.URL_ENCODED_QUERY);
    }

    @Override
    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
        if (this.engines.isEmpty()) {
            String engineList = parameters.get(Model.ENGINES);
            if (engineList != null) {
                this.engines = new ArrayList<>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }

    @Override
    protected PagingSearchResultList doSearch(final String query) {
        List<SearchResult> results = this.conversionStrategy.convertResult(doMetaSearch(query));
        PagingData pagingData = new PagingData(results, this.page, "q=" + this.urlEncodedQuery);
        return new PagingSearchResultList(results, pagingData, query);
    }

    @Override
    protected PagingSearchResultList getEmptyResult() {
        PagingData pagingData = new PagingData(Collections.emptyList(), this.page, "q=");
        return new PagingSearchResultList(Collections.emptyList(), pagingData, null);
    }

    private Result doMetaSearch(final String query) {
        long time = DEFAULT_TIMEOUT;
        if (null != this.timeout) {
            try {
                time = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                time = DEFAULT_TIMEOUT;
            }
        }
        return search(new SimpleQuery(query), this.engines, time);
    }
}
