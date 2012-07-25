package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractPagingSearchResultGenerator implements ParametersAware {

    private CollectionManager collectionManager;

    private String type;

    public EresourcesSearchGenerator(final CollectionManager collectionManager, final SAXStrategy<PagingSearchResultSet> saxStrategy) {
        super(saxStrategy);
        this.collectionManager = collectionManager;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        super.setModel(model);
        this.type = ModelUtil.getString(model, Model.TYPE);
    }

    public void setParameters(final Map<String, String> parameters) {
        if (parameters.containsKey(Model.TYPE)) {
            this.type = parameters.get(Model.TYPE);
        }
    }

    @Override
    protected Collection<SearchResult> getSearchResults(final String query) {
        Collection<Eresource> eresources = null;
        if (this.type == null) {
            eresources = this.collectionManager.search(query);
        } else {
            eresources = this.collectionManager.searchType(this.type, query);
        }
        Collection<SearchResult> results = new LinkedList<SearchResult>();
        for (Eresource eresource : eresources) {
            results.add(new EresourceSearchResult(eresource));
        }
        return results;
    }
}
