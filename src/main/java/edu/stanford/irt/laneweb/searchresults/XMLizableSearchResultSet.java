package edu.stanford.irt.laneweb.searchresults;

import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.Resource;

/**
 * 
 * @author ryanmax
 * 
 * $Id$
 *
 */
public class XMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource, XMLizable {

    private String query = null;

//    private Collection<Eresource> eresources;
//
//    private Collection<EresourceSearchResult> eresourceSearchResults;
//
//    private Collection<ContentResultSearchResult> contentResultSearchResults;
//
//    public void setQuery(final String query) {
//        this.query = query;
//    }
//
//    public void setEresources(final Collection<Eresource> eresources) {
//        if (null == eresources) {
//            throw new IllegalArgumentException("null eresources");
//        }
//        this.eresources = eresources;
//    }
//
//    public void setContentResultSearchResults(final Collection<ContentResultSearchResult> contentResults) {
//        if (null == contentResults) {
//            throw new IllegalArgumentException("null contentResults");
//        }
//        this.contentResultSearchResults = contentResults;
//    }
//
//    public void setEresourceSearchResults(final Collection<EresourceSearchResult> eresources) {
//        if (null == eresources) {
//            throw new IllegalArgumentException("null eresources");
//        }
//        this.eresourceSearchResults = eresources;
//    }

    public XMLizableSearchResultSet(String query) {
        this.query = query;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startPrefixMapping("", NAMESPACE);
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES);
            XMLUtils.startElement(handler, NAMESPACE, QUERY);
            XMLUtils.data(handler, this.query);
            XMLUtils.endElement(handler, NAMESPACE, QUERY);
//        Collection<SearchResult> searchResults = new TreeSet<SearchResult>();
//        if (null != this.eresources) {
//            for (Eresource eresource : this.eresources) {
//                EresourceSearchResult ersr = new EresourceSearchResult(eresource);
//                searchResults.add(ersr);
//            }
//        }
//        if (null != this.eresourceSearchResults) {
//            for (EresourceSearchResult eresource : this.eresourceSearchResults) {
//                searchResults.add(eresource);
//            }
//        }
//        if (null != this.contentResultSearchResults) {
//            for (ContentResultSearchResult contentResult : this.contentResultSearchResults) {
//                searchResults.add(contentResult);
//            }
//        }
        for (SearchResult resource : this) {
            resource.toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
    }
}
