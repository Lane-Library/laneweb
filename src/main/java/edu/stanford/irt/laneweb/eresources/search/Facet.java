package edu.stanford.irt.laneweb.eresources.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.SolrService;

public final class Facet {

    private static final String BEGINS_WITH_OR_TWO_COLONS = "(^|::)";

    private static final String COLON = ":";

    private static final String DOT_STAR = ".*";

    private static final String EMPTY = "";

    private static final String QUOTE = "\"";

    private static final String TYPE_FACET = "type";

    private static final String[] TYPE_FACET_EXCEPTIONS = { "Book", "Journal" };

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private String activeFacets;

    private long count;

    private boolean enabled;

    private String fieldName;

    private String maybeQuote;

    private String url;

    private String value;

    public Facet(final String fieldName, final String value, final long count, final String activeFacets) {
        this.activeFacets = (null == activeFacets) ? EMPTY : activeFacets;
        this.fieldName = fieldName;
        this.value = value;
        this.count = count;
        this.enabled = isEnabled();
        this.url = getUrl();
    }

    public long getCount() {
        return this.count;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getUrl() {
        if (null != this.url) {
            return this.url;
        }
        String facetUrl;
        String joiner = this.activeFacets.isEmpty() ? EMPTY : SolrService.FACETS_SEPARATOR;
        if (this.enabled) {
            facetUrl = this.activeFacets.replaceFirst(BEGINS_WITH_OR_TWO_COLONS + this.fieldName + COLON
                    + getMaybeQuote() + Pattern.quote(this.value) + getMaybeQuote(), EMPTY);
            if (TYPE_FACET.equals(this.fieldName)) {
                for (String typeValue : TYPE_FACET_EXCEPTIONS) {
                    if (typeValue.equals(this.value)) {
                        facetUrl = this.activeFacets.replaceAll(BEGINS_WITH_OR_TWO_COLONS + TYPE_FACET + COLON + QUOTE
                                + typeValue + " ?(Digital|Print)?\"", EMPTY);
                    }
                }
            }
        } else {
            facetUrl = this.activeFacets + joiner + this.fieldName + COLON + getMaybeQuote() + this.value
                    + getMaybeQuote();
        }
        facetUrl = facetUrl.replaceAll("(^::|::$)", EMPTY);
        return encodeString(facetUrl);
    }

    public String getValue() {
        return this.value;
    }

    public boolean isEnabled() {
        if (this.activeFacets.isEmpty()) {
            return false;
        }
        if (TYPE_FACET.equals(this.fieldName)) {
            for (String typeValue : TYPE_FACET_EXCEPTIONS) {
                if (typeValue.equals(this.value)
                        && this.activeFacets.contains(TYPE_FACET + COLON + QUOTE + typeValue)) {
                    return true;
                }
            }
        }
        return this.activeFacets.matches(DOT_STAR + this.fieldName + COLON + getMaybeQuote() + Pattern.quote(this.value)
                + getMaybeQuote() + DOT_STAR);
    }

    @Override
    public String toString() {
        return this.value + " = " + this.count + "; enabled=" + this.enabled + "; url=" + this.url;
    }

    private String encodeString(final String string) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(string, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
        return encoded;
    }

    private String getMaybeQuote() {
        if (null == this.maybeQuote) {
            this.maybeQuote = this.value.startsWith("[") && this.value.endsWith("]") ? EMPTY : QUOTE;
        }
        return this.maybeQuote;
    }
}
