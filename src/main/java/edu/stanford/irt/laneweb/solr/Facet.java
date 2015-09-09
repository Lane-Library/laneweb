package edu.stanford.irt.laneweb.solr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.LanewebException;

public class Facet {

    private static final String COLON = ":";

    private static final String DOT_STAR = ".*";

    private static final String EMPTY = "";

    private static final String QUOTE = "\"";

    private String activeFacets;

    private long count;

    private boolean enabled;

    private String escapedFacetValue;

    private String fieldName;

    private String maybeQuote;

    private String url;

    private String value;

    public Facet(final String fieldName, final String value, final long count, final String activeFacets) {
        this.activeFacets = activeFacets;
        this.fieldName = fieldName;
        this.value = value;
        this.count = count;
        this.enabled = isEnabled();
        this.url = buildUrl();
    }

    public long getCount() {
        return this.count;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value + " = " + this.count + "; enabled=" + this.enabled + "; url=" + this.url;
    }

    private String buildUrl() {
        String facetUrl = null;
        String joiner = this.activeFacets.isEmpty() ? EMPTY : SolrSearchService.FACETS_SEPARATOR;
        if (this.enabled) {
            facetUrl = this.activeFacets.replaceFirst("(^|::)" + this.fieldName + COLON + getMaybeQuote()
                    + getEscapedFacetValue() + getMaybeQuote(), EMPTY);
        } else {
            facetUrl = this.activeFacets + joiner + this.fieldName + COLON + getMaybeQuote() + this.value
                    + getMaybeQuote();
        }
        facetUrl = facetUrl.replaceAll("(^::|::$)", EMPTY);
        return encodeString(facetUrl);
    }

    private String encodeString(final String string) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
        return encoded;
    }

    private String getEscapedFacetValue() {
        if (null == this.escapedFacetValue) {
            this.escapedFacetValue = Pattern.quote(this.value);
        }
        return this.escapedFacetValue;
    }

    private String getMaybeQuote() {
        if (null == this.maybeQuote) {
            this.maybeQuote = this.value.startsWith("[") && this.value.endsWith("]") ? EMPTY : QUOTE;
        }
        return this.maybeQuote;
    }

    private boolean isEnabled() {
        if (this.activeFacets.isEmpty()) {
            return false;
        }
        return this.activeFacets.matches(DOT_STAR + this.fieldName + COLON + getMaybeQuote() + getEscapedFacetValue()
                + getMaybeQuote() + DOT_STAR);
    }
}
