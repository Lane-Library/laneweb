/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author ryanmax
 */
public interface SearchResult extends Comparable<SearchResult> {

    /**
     * @return the search score
     */
    int getScore();

    /**
     * @return the sortTitle
     */
    String getSortTitle();

    /**
     * @return the dedupTitle
     */
    String getDedupTitle();

    void toSAX(ContentHandler handler) throws SAXException;
}
