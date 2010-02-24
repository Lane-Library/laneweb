/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.search.ContentResult;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class ContentResultSearchResult implements SearchResult {

    private static final Pattern DOUBLE_WEIGHT_PATTERN = Pattern
            .compile("pubmed_cochrane_reviews|dare|acpjc|bmj_clinical_evidence|jama_rce");

    private static final Pattern HALF_WEIGHT_PATTERN = Pattern
            .compile("^pubmed_(clinicaltrial|recent_reviews|treatment_focused|diagnosis_focused|prognosis_focused|harm_focused|etiology_focused|epidemiology_focused)");

    private static final Pattern QUARTER_WEIGHT_PATTERN = Pattern.compile("aafp_patients|medlineplus_0");

    private static final Pattern ENGINEID_PATTERN = Pattern.compile("_content_\\d+");

    private ContentResult contentResult;

    private Pattern queryTermPattern;

    private String resourceId;

    private String resourceName;

    private String resourceHits;

    private String resourceUrl;
    
    private String sortTitle;
    
    private int score;

    /**
     * 
     */
    public ContentResultSearchResult(ContentResult contentResult, Pattern queryTermPattern) {
        this.contentResult = contentResult;
        this.sortTitle = this.contentResult.getTitle().toLowerCase().replaceAll("\\W", "");
        this.sortTitle = NON_FILING_PATTERN.matcher(this.sortTitle).replaceFirst("");
        this.queryTermPattern = queryTermPattern;
        this.score = computeScore();
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    /**
     * @param resourceId
     *            the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @param resourceName
     *            the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @param resourceHits
     *            the resourceHits to set
     */
    public void setResourceHits(String resourceHits) {
        this.resourceHits = resourceHits;
    }

    /**
     * @param resourceUrl
     *            the resourceUrl to set
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int compareTo(SearchResult o) {
        int scoreCmp = o.getScore() - this.score;
        return (scoreCmp != 0 ? scoreCmp : this.sortTitle.compareTo(o.getSortTitle()));
    }

    public int hashCode() {
        return this.sortTitle.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ContentResultSearchResult)) {
            return false;
        }
        ContentResultSearchResult scmr = (ContentResultSearchResult) other;
        return scmr.getSortTitle().equals(this.sortTitle);
    }
    
    public int getScore() {
        return this.score;
    }

    /**
     * <pre>
     *  100 exact title match
     *  90 title begins with AND title contains more than one match AND desc contains more than one match
     *  80 title begins with AND title contains more than one match AND desc match
     *  70 title begins with AND title contains more than one match
     *  65 title begins with
     *  60 title contains more than one match AND desc contains more than one match
     *  50 title contains more than one match
     *  40 title match AND desc contains more than one match
     *  30 title match AND desc match
     *  20 title match
     *  10 desc match
     *  1
     * </pre>
     */
    private int computeScore() {
        int score;
        double weight = computeWeight(ENGINEID_PATTERN.matcher(this.contentResult.getId()).replaceFirst(""));
        Pattern titleBeginsWithPattern = Pattern.compile("^(" + this.queryTermPattern.toString() + ").*",
                Pattern.CASE_INSENSITIVE);
        boolean titleBeginsWithQueryTerms = titleBeginsWithPattern.matcher(this.contentResult.getTitle()).matches();
        Pattern exactTitlePattern = Pattern.compile("^(" + this.queryTermPattern.toString() + ")$",
                Pattern.CASE_INSENSITIVE);
        boolean exactTitle = exactTitlePattern.matcher(this.contentResult.getTitle()).matches();
        int titleHits = 0;
        int descriptionHits = 0;
        Matcher ntMatcher = this.queryTermPattern.matcher(this.contentResult.getTitle());
        while (ntMatcher.find()) {
            titleHits++;
        }
        if (null != this.contentResult.getDescription()) {
            Matcher ndMatcher = this.queryTermPattern.matcher(this.contentResult.getDescription());
            while (ndMatcher.find()) {
                descriptionHits++;
            }
        }
        if (exactTitle) {
            score = 100;
        } else if (titleBeginsWithQueryTerms && titleHits > 1 && descriptionHits > 1) {
            score = 90;
        } else if (titleBeginsWithQueryTerms && titleHits > 1 && descriptionHits == 1) {
            score = 80;
        } else if (titleBeginsWithQueryTerms && titleHits > 1) {
            score = 70;
        } else if (titleBeginsWithQueryTerms) {
            score = 65;
        } else if (titleHits > 1 && descriptionHits > 1) {
            score = 60;
        } else if (titleHits > 1) {
            score = 50;
        } else if (titleHits > 0 && descriptionHits > 1) {
            score = 40;
        } else if (titleHits > 0 && descriptionHits > 0) {
            score = 30;
        } else if (titleHits > 0) {
            score = 20;
        } else if (descriptionHits > 0) {
            score = 10;
        } else {
            score = 1;
        }
        return (int) (score * weight);
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.laneweb.searchresults.SearchResult#toSAX(org.xml.sax.ContentHandler)
     */
    public void toSAX(ContentHandler handler) throws SAXException {
        // TODO: returning result element for now ... turn into displayable?
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(this.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "searchContent");
        XMLUtils.startElement(handler, NAMESPACE, RESULT, atts);
        maybeCreateElement(handler, RESOURCE_ID, this.resourceId);
        maybeCreateElement(handler, RESOURCE_NAME, this.resourceName);
        maybeCreateElement(handler, RESOURCE_URL, this.resourceUrl);
        maybeCreateElement(handler, RESOURCE_HITS, this.resourceHits);
        maybeCreateElement(handler, ID, this.contentResult.getId());
        maybeCreateElement(handler, CONTENT_ID, this.contentResult.getContentId());
        maybeCreateElement(handler, TITLE, this.contentResult.getTitle());
        maybeCreateElement(handler, DESCRIPTION, this.contentResult.getDescription());
        maybeCreateElement(handler, AUTHOR, this.contentResult.getAuthor());
        maybeCreateElement(handler, PUBLICATION_DATE, this.contentResult.getPublicationDate());
        maybeCreateElement(handler, PUBLICATION_TITLE, this.contentResult.getPublicationTitle());
        maybeCreateElement(handler, PUBLICATION_VOLUME, this.contentResult.getPublicationVolume());
        maybeCreateElement(handler, PUBLICATION_ISSUE, this.contentResult.getPublicationIssue());
        maybeCreateElement(handler, PAGES, this.contentResult.getPages());
        maybeCreateElement(handler, URL, this.contentResult.getURL());
        XMLUtils.endElement(handler, NAMESPACE, RESULT);
    }

    private double computeWeight(String engineId) {
        if (DOUBLE_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 2;
        } else if (HALF_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.5;
        } else if (QUARTER_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.25;
        }
        return 1;
    }

    private void maybeCreateElement(final ContentHandler handler, String name, String value) throws SAXException {
        if (value != null) {
            XMLUtils.createElementNS(handler, NAMESPACE, name, value);
        }
    }
}
