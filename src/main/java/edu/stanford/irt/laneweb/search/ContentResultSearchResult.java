package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.ContentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult implements SearchResult {

    private static final Pattern DOUBLE_WEIGHT_PATTERN = Pattern
            .compile("pubmed_cochrane_reviews|dare|acpjc|bmj_clinical_evidence|jama_rce");

    private static final Pattern ENGINEID_PATTERN = Pattern.compile("_content_\\d+");

    private static final Pattern HALF_WEIGHT_PATTERN = Pattern
            .compile("^pubmed_(clinicaltrial|recent_reviews|treatment_focused|diagnosis_focused|prognosis_focused|harm_focused|etiology_focused|epidemiology_focused)");

    private static final Pattern QUARTER_WEIGHT_PATTERN = Pattern.compile("aafp_patients|medlineplus_0");

    private ContentResult contentResult;

    private Pattern queryTermPattern;

    private String resourceHits;

    private String resourceId;

    private String resourceName;

    private String resourceUrl;

    private int score;

    private String sortTitle;

    public ContentResultSearchResult(final ContentResult contentResult, final Pattern queryTermPattern) {
        this.contentResult = contentResult;
        this.sortTitle = NON_FILING_PATTERN.matcher(this.contentResult.getTitle()).replaceFirst("");
        this.sortTitle = this.sortTitle.toLowerCase().replaceAll("\\W", "");
        this.queryTermPattern = queryTermPattern;
        this.score = computeScore();
    }

    public int compareTo(final SearchResult o) {
        int scoreCmp = o.getScore() - this.score;
        int titleCmp = this.sortTitle.compareTo(o.getSortTitle());
        if (titleCmp == 0) {
            return titleCmp;
        }
        return (scoreCmp != 0 ? scoreCmp : this.sortTitle.compareTo(o.getSortTitle()));
    }

    // return -10 to 10, based on pub date's proximity to THIS_YEAR
    private int computeDateAdjustment() {
        if (null == this.contentResult.getPublicationDate()) {
            return 0;
        }
        Matcher yearMatcher = YEAR_PATTERN.matcher(this.contentResult.getPublicationDate());
        if (yearMatcher.matches()) {
            return Math.max(-10, 10 - (THIS_YEAR - Integer.parseInt(yearMatcher.group(1))));
        }
        return 0;
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
        return (int) ((score + computeDateAdjustment()) * weight);
    }

    private double computeWeight(final String engineId) {
        if (DOUBLE_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 2;
        } else if (HALF_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.5;
        } else if (QUARTER_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.25;
        }
        return 1;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ContentResultSearchResult)) {
            return false;
        }
        ContentResultSearchResult scmr = (ContentResultSearchResult) other;
        return scmr.getSortTitle().equals(this.sortTitle);
    }

    public String getContentUrl() {
        return this.contentResult.getURL();
    }

    /**
     * @return the publicationTitle from contentResult
     */
    public String getPublicationTitle() {
        return this.contentResult.getPublicationTitle();
    }

    /**
     * @return the resourceHits
     */
    public String getResourceHits() {
        return this.resourceHits;
    }

    /**
     * @return the resourceId
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * @return the resourceUrl
     */
    public String getResourceUrl() {
        return this.resourceUrl;
    }

    public int getScore() {
        return this.score;
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return this.sortTitle.hashCode();
    }

    private void maybeCreateElement(final ContentHandler handler, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(handler, NAMESPACE, name, value);
        }
    }

    /**
     * @param resourceHits
     *            the resourceHits to set
     */
    public void setResourceHits(final String resourceHits) {
        this.resourceHits = resourceHits;
    }

    /**
     * @param resourceId
     *            the resourceId to set
     */
    public void setResourceId(final String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @param resourceName
     *            the resourceName to set
     */
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @param resourceUrl
     *            the resourceUrl to set
     */
    public void setResourceUrl(final String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
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
}
