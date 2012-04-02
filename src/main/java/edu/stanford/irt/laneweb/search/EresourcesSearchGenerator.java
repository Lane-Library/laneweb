package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class EresourcesSearchGenerator extends AbstractSearchGenerator {

    private CollectionManager collectionManager;

    private String type;

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

    @Override
    protected void initialize() {
        super.initialize();
        this.type = getParameterMap().containsKey(Model.TYPE) ? getParameterMap().get(Model.TYPE) : ModelUtil.getString(getModel(),
                Model.TYPE);
    }
}
