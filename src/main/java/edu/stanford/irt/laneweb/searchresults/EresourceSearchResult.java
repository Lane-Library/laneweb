/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.eresources.EresourceVersionComparator;

import java.util.Collection;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class EresourceSearchResult implements SearchResult, SAXableSearchResult {

    private Eresource eresource;

    private static final EresourceVersionComparator VERSION_COMPARATOR = new EresourceVersionComparator();

    private Pattern queryTermPattern = null;
    
    private String sortTitle;
    
    private String dedupTitle;
    
    private int hashcode;

    /**
     * 
     */
    public EresourceSearchResult(Eresource eresource) {
        this.eresource = eresource;
        this.hashcode = Integer.toString(this.eresource.getId()).hashCode();
        this.dedupTitle = eresource.getTitle().toLowerCase().replaceAll("\\W", SearchResultHelper.EMPTY);
        this.sortTitle = SearchResultHelper.NON_FILING_PATTERN.matcher(this.dedupTitle).replaceFirst(SearchResultHelper.EMPTY);
    }

    public int getScore() {
        return this.eresource.getScore();
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    public String getDedupTitle() {
        return this.dedupTitle;
    }

    public void setQueryTermPattern(Pattern queryTermPattern) {
        this.queryTermPattern = queryTermPattern;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.laneweb.MetaResult#toSAX(org.xml.sax.ContentHandler)
     */
    public void toSAX(ContentHandler handler) throws SAXException {
        this.handleEresource(handler, this.eresource);
    }

    public int compareTo(SearchResult o) {
        int scoreCmp = o.getScore() - this.eresource.getScore();
        return (scoreCmp != 0 ? scoreCmp : this.getSortTitle().compareTo(o.getSortTitle()));
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EresourceSearchResult)) {
            return false;
        }
        EresourceSearchResult eres = (EresourceSearchResult) other;
        return eres.eresource.getId() == this.eresource.getId();
    }

    private void handleEresource(final ContentHandler handler, final Eresource eresource) throws SAXException {
        Collection<Version> versions = new TreeSet<Version>(VERSION_COMPARATOR);
        // TODO: returning result element for now ... turn into displayable?
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(this.eresource.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "eresource");
        XMLUtils.startElement(handler, NAMESPACE, RESULT, atts);
        SearchResultHelper.handleElement(handler, ID, Integer.toString(this.eresource.getId()));
        SearchResultHelper.handleHighlightedElement(handler, TITLE, this.eresource.getTitle(), this.queryTermPattern);
        SearchResultHelper.handleElement(handler, SORT_TITLE, SearchResultHelper.NON_FILING_PATTERN.matcher(
                this.eresource.getTitle()).replaceFirst(SearchResultHelper.EMPTY));
        SearchResultHelper.handleElement(handler, DEDUP_TITLE, this.getDedupTitle());
        XMLUtils.startElement(handler, NAMESPACE, VERSIONS);
        versions.addAll(this.eresource.getVersions());
        for (Version version : versions) {
            handleVersion(handler, version);
        }
        XMLUtils.endElement(handler, VERSIONS);
        XMLUtils.endElement(handler, RESULT);
    }

    private void handleVersion(final ContentHandler handler, final Version version) throws SAXException {
        XMLUtils.startElement(handler, NAMESPACE, VERSION);
        if (null != version.getSummaryHoldings()) {
            SearchResultHelper.handleElement(handler, SUMMARY_HOLDINGS, version.getSummaryHoldings());
        }
        if (null != version.getDates()) {
            SearchResultHelper.handleElement(handler, DATES, version.getDates());
        }
        if (null != version.getPublisher()) {
            SearchResultHelper.handleElement(handler, PUBLISHER, version.getPublisher());
        }
        if (null != version.getDescription()) {
            SearchResultHelper.handleElement(handler, DESCRIPTION, version.getDescription());
        }
        XMLUtils.startElement(handler, NAMESPACE, LINKS);
        for (Link link : version.getLinks()) {
            handleLink(handler, link);
        }
        XMLUtils.endElement(handler, LINKS);
        XMLUtils.endElement(handler, VERSION);
    }

    private void handleLink(final ContentHandler handler, final Link link) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        String label = link.getLabel();
        String type;
        if ((null != label) && "get password".equalsIgnoreCase(label)) {
            type = "getPassword";
        } else if ((null != label) && "impact factor".equalsIgnoreCase(label)) {
            type = "impactFactor";
        } else {
            type = "normal";
        }
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", type);
        XMLUtils.startElement(handler, NAMESPACE, LINK, atts);
        if (null != link.getLabel()) {
            SearchResultHelper.handleElement(handler, LABEL, label);
        }
        if (null != link.getUrl()) {
            SearchResultHelper.handleElement(handler, URL, link.getUrl());
        }
        if (null != link.getInstruction()) {
            SearchResultHelper.handleElement(handler, INSTRUCTION, link.getInstruction());
        }
        XMLUtils.endElement(handler, LINK);
    }
}
