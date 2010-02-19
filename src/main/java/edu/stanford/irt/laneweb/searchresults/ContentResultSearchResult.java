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

import edu.stanford.irt.laneweb.AbstractResource;
import edu.stanford.irt.laneweb.search.QueryTermPattern;
import edu.stanford.irt.search.ContentResult;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class ContentResultSearchResult extends AbstractResource implements SearchResult {

    private ContentResult contentResult;

    private static final Pattern DOUBLE_WEIGHT_PATTERN = Pattern
            .compile("pubmed_cochrane_reviews|dare|acpjc|bmj_clinical_evidence|jama_rce");

    private static final Pattern HALF_WEIGHT_PATTERN = Pattern
            .compile("^pubmed_(clinicaltrial|recent_reviews|treatment_focused|diagnosis_focused|prognosis_focused|harm_focused|etiology_focused|epidemiology_focused)");

    private static final Pattern QUARTER_WEIGHT_PATTERN = Pattern.compile("aafp_patients|medlineplus_0");

    private static final Pattern ENGINEID_PATTERN = Pattern.compile("_content_\\d+");

    private Pattern queryTermPattern;

    private String resourceId;

    private String resourceName;

    private String resourceHits;

    private String resourceUrl;
    
    private String sortTitle;
    
    private String dedupTitle;
    
    int score;

    /**
     * 
     */
    public ContentResultSearchResult(ContentResult contentResult, Pattern queryTermPattern) {
        this.contentResult = contentResult;
        this.dedupTitle = this.contentResult.getTitle().toLowerCase().replaceAll("\\W", QueryTermPattern.EMPTY);
        this.sortTitle = QueryTermPattern.NON_FILING_PATTERN.matcher(dedupTitle).replaceFirst(QueryTermPattern.EMPTY);
        this.queryTermPattern = queryTermPattern;
        this.score = computeScore();
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    public String getDedupTitle() {
        return this.dedupTitle;
    }

    /**
     * @return the resourceId
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * @param resourceId
     *            the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * @param resourceName
     *            the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return the resourceHits
     */
    public String getResourceHits() {
        return this.resourceHits;
    }

    /**
     * @param resourceHits
     *            the resourceHits to set
     */
    public void setResourceHits(String resourceHits) {
        this.resourceHits = resourceHits;
    }

    /**
     * @return the resourceUrl
     */
    public String getResourceUrl() {
        return this.resourceUrl;
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

    @Override
    public int hashCode() {
        return this.dedupTitle.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ContentResultSearchResult)) {
            return false;
        }
        ContentResultSearchResult scmr = (ContentResultSearchResult) other;
        return scmr.dedupTitle.equals(this.dedupTitle);
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
        double weight = computeWeight(ENGINEID_PATTERN.matcher(this.contentResult.getId()).replaceFirst(
                QueryTermPattern.EMPTY));
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
        if (null != this.getResourceId()) {
            handleElement(handler, RESOURCE_ID, this.getResourceId());
        }
        if (null != this.getResourceName()) {
            handleElement(handler, RESOURCE_NAME, this.getResourceName());
        }
        if (null != this.getResourceUrl()) {
            handleElement(handler, RESOURCE_URL, this.getResourceUrl());
        }
        if (null != this.getResourceHits()) {
            handleElement(handler, RESOURCE_HITS, this.getResourceHits());
        }
        if (null != this.contentResult.getId()) {
            handleElement(handler, ID, this.contentResult.getId());
        }
        if (null != this.contentResult.getContentId()) {
            handleElement(handler, CONTENT_ID, this.contentResult.getContentId());
        }
        if (null != this.contentResult.getTitle()) {
            handleElement(handler, TITLE, this.contentResult.getTitle());
            handleElement(handler, SORT_TITLE, QueryTermPattern.NON_FILING_PATTERN.matcher(
                    this.contentResult.getTitle()).replaceFirst(QueryTermPattern.EMPTY));
            handleElement(handler, DEDUP_TITLE, this.getDedupTitle());
        }
        if (null != this.contentResult.getDescription()) {
            handleElement(handler, DESCRIPTION, this.contentResult.getDescription());
        }
        if (null != this.contentResult.getAuthor()) {
            handleElement(handler, AUTHOR, this.contentResult.getAuthor());
        }
        if (null != this.contentResult.getPublicationDate()) {
            handleElement(handler, PUBLICATION_DATE, this.contentResult.getPublicationDate());
        }
        if (null != this.contentResult.getPublicationTitle()) {
            handleElement(handler, PUBLICATION_TITLE, this.contentResult.getPublicationTitle());
        }
        if (null != this.contentResult.getPublicationVolume()) {
            handleElement(handler, PUBLICATION_VOLUME, this.contentResult.getPublicationVolume());
        }
        if (null != this.contentResult.getPublicationIssue()) {
            handleElement(handler, PUBLICATION_ISSUE, this.contentResult.getPublicationIssue());
        }
        if (null != this.contentResult.getPages()) {
            handleElement(handler, PAGES, this.contentResult.getPages());
        }
        if (null != this.contentResult.getURL()) {
            handleElement(handler, URL, this.contentResult.getURL());
        }
        XMLUtils.endElement(handler, RESULT);
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
}
