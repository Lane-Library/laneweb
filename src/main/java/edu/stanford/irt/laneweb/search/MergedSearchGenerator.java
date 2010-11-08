/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

/**
 * @author ryanmax $Id$
 */
public class MergedSearchGenerator extends ContentSearchGenerator {

    private CollectionManager collectionManager;

    @Override
    public void generate() throws SAXException {
//        XMLizableSearchResultSet mergedSearchResults = new XMLizableSearchResultSet(this.query);
        PagingXMLizableSearchResultSet mergedSearchResults = new PagingXMLizableSearchResultSet(this.query, this.show);
        mergedSearchResults.addAll(getEresourceList());
        mergedSearchResults.addAll(getContentResultList(doSearch()));
        this.xmlConsumer.startDocument();
        mergedSearchResults.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    private Collection<EresourceSearchResult> getEresourceList() {
        Collection<EresourceSearchResult> eresourceResults = new LinkedList<EresourceSearchResult>();
        for (Eresource eresource : this.collectionManager.search(this.query)) {
            eresourceResults.add(new EresourceSearchResult(eresource));
        }
        return eresourceResults;
    }
}
