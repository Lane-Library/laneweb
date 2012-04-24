package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.Initializable;
import edu.stanford.irt.laneweb.cocoon.ParametersAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractSearchGenerator implements ParametersAware, Initializable {

    private CollectionManager collectionManager;

    private String type;
    
    private Map<String, String> parameters;

    private Map<String, Object> model;

    public void generate() {
        PagingXMLizableSearchResultSet eresources = new PagingXMLizableSearchResultSet(this.query, this.page);
        eresources.addAll(getEresourceList());
        try {
            eresources.toSAX(getXMLConsumer());
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public void setModel(Map<String, Object> model) {
        super.setModel(model);
        this.model = model;
    }

    protected Collection<SearchResult> getEresourceList() {
        if (null == this.query) {
            throw new IllegalStateException("null query");
        }
        Collection<Eresource> eresources = null;
        if (null != this.type) {
            eresources = this.collectionManager.searchType(this.type, this.query);
        } else {
            eresources = this.collectionManager.search(this.query);
        }
        Collection<SearchResult> results = new LinkedList<SearchResult>();
        for (Eresource eresource : eresources) {
            results.add(new EresourceSearchResult(eresource));
        }
        return results;
    }

    public void initialize() {
        this.type = this.parameters.containsKey(Model.TYPE) ? this.parameters.get(Model.TYPE) : ModelUtil.getString(this.model,
                Model.TYPE);
    }
}
