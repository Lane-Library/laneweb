package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;

public class EresourcesSearchGenerator extends AbstractSearchGenerator {

    private CollectionManager collectionManager;

    private String subset;

    private String type;

    public void generate() throws IOException, SAXException {
        PagingXMLizableSearchResultSet eresources = new PagingXMLizableSearchResultSet(this.query, this.page);
        eresources.addAll(getEresourceList());
        eresources.toSAX(this.xmlConsumer);
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
        } else if (null != this.subset) {
            eresources = this.collectionManager.searchSubset(this.subset, this.query);
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
        this.type = this.parameterMap.containsKey(Model.TYPE) ? this.parameterMap.get(Model.TYPE) : 
            ModelUtil.getString(this.model, Model.TYPE);
        this.subset = this.parameterMap.containsKey(Model.SUBSET) ? this.parameterMap.get(Model.SUBSET) : 
            ModelUtil.getString(this.model, Model.SUBSET);
    }
}
