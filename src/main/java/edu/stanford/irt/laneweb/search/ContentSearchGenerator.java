package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.DefaultResult;
import edu.stanford.irt.search.impl.SimpleQuery;

/**
 * @author ryanmax
 */
public class ContentSearchGenerator extends AbstractPagingSearchResultGenerator implements ParametersAware {

    private static final long DEFAULT_TIMEOUT = 20000;

    private Collection<String> engines;

    private MetaSearchManager metasearchManager;
    
    private ContentResultConversionStrategy conversionStrategy;

    private String timeout;

    public ContentSearchGenerator(final MetaSearchManagerSource msms, final SAXStrategy<PagingSearchResultSet> saxStrategy, final ContentResultConversionStrategy conversionStrategy) {
        super(saxStrategy);
        this.metasearchManager = msms.getMetaSearchManager();
        this.conversionStrategy = conversionStrategy;
    }

    public ContentSearchGenerator(final MetaSearchManager metaSearchManager, final SAXStrategy<PagingSearchResultSet> saxStrategy, final ContentResultConversionStrategy conversionStrategy) {
        super(saxStrategy);
        this.metasearchManager = metaSearchManager;
        this.conversionStrategy = conversionStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class, Collections.<String> emptyList());
    }

    public void setParameters(final Map<String, String> parameters) {
        if (this.timeout == null) {
            this.timeout = parameters.get(Model.TIMEOUT);
        }
        if (this.engines.size() == 0) {
            String engineList = parameters.get(Model.ENGINES);
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
        }
    }

    @Override
    protected Collection<SearchResult> getSearchResults(final String query) {
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
            result = new DefaultResult("");
        } else {
            return this.metasearchManager.search(new SimpleQuery(query), time, this.engines, true);
        }
        return result;
    }
}

