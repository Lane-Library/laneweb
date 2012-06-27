package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private CollectionManager collectionManager;

    public MergedSearchGenerator(final CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        PagingXMLizableSearchResultSet mergedSearchResults = new PagingXMLizableSearchResultSet(this.query, this.page);
        mergedSearchResults.addAll(getEresourceList());
        mergedSearchResults.addAll(getContentResultList(doSearch()));
        try {
            mergedSearchResults.toSAX(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private Collection<EresourceSearchResult> getEresourceList() {
        Collection<EresourceSearchResult> eresourceResults = new LinkedList<EresourceSearchResult>();
        for (Eresource eresource : this.collectionManager.search(this.query)) {
            eresourceResults.add(new EresourceSearchResult(eresource));
        }
        return eresourceResults;
    }
}
