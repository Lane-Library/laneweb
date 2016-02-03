package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 */
public class ContentSearchGenerator extends AbstractPagingSearchResultGenerator implements ParametersAware {

    private static final long DEFAULT_TIMEOUT = 20000;

    private ContentResultConversionStrategy conversionStrategy;

    private Collection<String> engines;

    private MetaSearchManager metasearchManager;

    private String timeout;

    public ContentSearchGenerator(final MetaSearchManager metaSearchManager,
            final SAXStrategy<PagingSearchResultList> saxStrategy,
            final ContentResultConversionStrategy conversionStrategy) {
        super(saxStrategy);
        this.metasearchManager = metaSearchManager;
        this.conversionStrategy = conversionStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class, Collections.emptyList());
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
    protected List<SearchResult> getSearchResults(final String query) {
        return this.conversionStrategy.convertResult(doMetaSearch(query));
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
        Result result = null;
        if (query == null || query.isEmpty()) {
            throw new LanewebException("no query");
        } else {
            result = this.metasearchManager.search(new SimpleQuery(query), this.engines, time);
        }
        return result;
    }
}
