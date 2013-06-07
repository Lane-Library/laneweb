package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractPagingSearchResultGenerator implements ParametersAware {

    private CollectionManager collectionManager;

    private String type;

    public EresourcesSearchGenerator(final CollectionManager collectionManager, final SAXStrategy<PagingSearchResultList> saxStrategy) {
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
            try {
                this.type = URLDecoder.decode(parameters.get(Model.TYPE), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
    }

    @Override
    protected List<SearchResult> getSearchResults(final String query) {
        Collection<Eresource> eresources = null;
        if (this.type == null) {
            eresources = this.collectionManager.search(query);
        } else {
            eresources = this.collectionManager.searchType(this.type, query);
        }
        List<SearchResult> results = new LinkedList<SearchResult>();
        for (Eresource eresource : eresources) {
            results.add(new EresourceSearchResult(eresource));
        }
        return results;
    }
}
