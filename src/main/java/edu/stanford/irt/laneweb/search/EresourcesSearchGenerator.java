package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractSearchGenerator implements ParametersAware {

    private CollectionManager collectionManager;

    private String type;
    
    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    public EresourcesSearchGenerator(final CollectionManager collectionManager, final SAXStrategy<PagingSearchResultSet> saxStrategy) {
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
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
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        PagingSearchResultSet eresourceSearchResults = new PagingSearchResultSet(this.query, this.page);
        eresourceSearchResults.addAll(getEresourceList());
        this.saxStrategy.toSAX(eresourceSearchResults, xmlConsumer);
    }

    protected Collection<SearchResult> getEresourceList() {
        Collection<Eresource> eresources = null;
        if (this.query == null || this.query.isEmpty()) {
            eresources = Collections.emptySet();
        } else if (this.type == null) {
            eresources = this.collectionManager.search(this.query);
        } else {
            eresources = this.collectionManager.searchType(this.type, this.query);
        }
        Collection<SearchResult> results = new LinkedList<SearchResult>();
        for (Eresource eresource : eresources) {
            results.add(new EresourceSearchResult(eresource));
        }
        return results;
    }
}
