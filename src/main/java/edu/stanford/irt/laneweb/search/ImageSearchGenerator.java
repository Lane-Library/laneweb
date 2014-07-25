package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.bassett.BassettCollectionManager;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ImageSearchGenerator extends AbstractMetasearchGenerator<Map<String, Object>> implements ParametersAware {

    public static final String BASSETT_RESULT = "bassett";
    public static final String METASEARCH_RESULT = "metasearch";
    public static final String SEARCH_TERM = "search-term";

    private static final long DEFAULT_TIMEOUT = 20000;

    private Collection<String> engines = Collections.emptyList();

    private MetaSearchManager metasearchManager;

    private BassettCollectionManager bassettCollection;

    private String urlEncodedSearchTerm;

    
    public ImageSearchGenerator(final MetaSearchManager metaSearchManager, final BassettCollectionManager collectionManager,
            final SAXStrategy<Map<String, Object>> saxStrategy) {
        super(metaSearchManager, saxStrategy);
        this.bassettCollection = collectionManager;
        this.metasearchManager = metaSearchManager;
    }

    @Override
    protected Map<String, Object> doSearch(String query) {
        
        Map<String, Object> result = new HashMap<>();
        Result metaSearchResult = null;
        if (query != null && !query.isEmpty()) {
            List<BassettImage> bassettResult = this.bassettCollection.search(query);
            result.put(BASSETT_RESULT, bassettResult);
            Query simpleQuery = new SimpleQuery(query, this.engines); 
            metaSearchResult = this.metasearchManager.search(simpleQuery,  DEFAULT_TIMEOUT , true);
            result.put(METASEARCH_RESULT, metaSearchResult);
            result.put(SEARCH_TERM, this.urlEncodedSearchTerm);
        }
        return result;
    }

    
    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.urlEncodedSearchTerm = ModelUtil.getString(model, Model.URL_ENCODED_QUERY);
    }

    public void setParameters(final Map<String, String> parameters) {
        if (this.engines == null || this.engines.isEmpty()) {
            String engineList = parameters.get(Model.ENGINES);
            if (engineList != null) {
                this.engines = new LinkedList<String>();
                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
                    this.engines.add(st.nextToken());
                }
            }
            
        }
    }

}

