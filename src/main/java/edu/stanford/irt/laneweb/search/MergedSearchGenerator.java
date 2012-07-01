package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private CollectionManager collectionManager;
    
    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    public MergedSearchGenerator(final CollectionManager collectionManager, SAXStrategy<PagingSearchResultSet> saxStrategy) {
        super(saxStrategy);
        this.collectionManager = collectionManager;
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        PagingSearchResultSet mergedSearchResults = new PagingSearchResultSet(this.query, this.page);
        mergedSearchResults.addAll(getEresourceList());
        mergedSearchResults.addAll(getContentResultList(doSearch()));
        this.saxStrategy.toSAX(mergedSearchResults, xmlConsumer);
    }

    private Collection<EresourceSearchResult> getEresourceList() {
        Collection<EresourceSearchResult> eresourceResults = new LinkedList<EresourceSearchResult>();
        if (this.query != null && !this.query.isEmpty()) {
            for (Eresource eresource : this.collectionManager.search(this.query)) {
                eresourceResults.add(new EresourceSearchResult(eresource));
            }
        }
        return eresourceResults;
    }
}
