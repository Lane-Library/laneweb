package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;

public class EresourcesSearchGenerator extends AbstractSearchGenerator {

    private String type;
    private CollectionManager collectionManager;
    private String subset;

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
        this.type = this.parameterMap.containsKey(Model.TYPE) ?
                this.parameterMap.get(Model.TYPE) :
                    this.model.getString(Model.TYPE);
        this.subset = this.parameterMap.containsKey(Model.SUBSET) ?
                this.parameterMap.get(Model.SUBSET) :
                    this.model.getString(Model.SUBSET);
    }

    public void generate() throws IOException, SAXException {
        XMLizableSearchResultSet eresources = new XMLizableSearchResultSet(this.query);
        eresources.addAll(getEresourceList());
        this.xmlConsumer.startDocument();
        eresources.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }
}
