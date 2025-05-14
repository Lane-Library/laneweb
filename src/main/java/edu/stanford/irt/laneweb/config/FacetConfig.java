package edu.stanford.irt.laneweb.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class FacetConfig {

    private Collection<String> facetFields;

    private Map<String, String> facetFieldsMatch;

    private Map<String, Integer> facetFieldsLimit;

    private int facetMincount = 1;

    private int facetLimit = 5;

    public FacetConfig() {
        this.facetFields = Collections
                .unmodifiableCollection(Arrays.asList("type", "publicationType", "recordType", "publicationTitle"));
        this.facetFieldsMatch = Collections.unmodifiableMap(
                Map.of("publicationType", "Review|Clinical Trial|Randomized Controlled Trial|Systematic Review"));

        this.facetFieldsLimit = Collections.unmodifiableMap(Map.of("recordType", Integer.MAX_VALUE));

    }

    public Collection<String> getFacetFields() {
        return this.facetFields;
    }

    public Map<String, String> getFacetFieldsMatch() {
        return this.facetFieldsMatch;
    }

    public String getFacetFieldsMatch(String field) {
        return this.facetFieldsMatch.get(field);
    }

    public Collection<String> getFacetFieldsMatchValues(String field) {
        String match = this.facetFieldsMatch.get(field);
        return match != null ? Arrays.asList(match.split("|")) : Collections.emptyList();
    }

    public Map<String, Integer> getFacetFieldsLimit() {
        return this.facetFieldsLimit;
    }

    public boolean hasFacetFieldsLimit(String field) {
        return this.facetFieldsLimit.containsKey(field);
    }

    public int getFacetFieldsLimit(String field) {
        return this.facetFieldsLimit.get(field);
    }

    public int getFacetMincount() {
        return this.facetMincount;
    }

    public int getFacetLimit() {
        return this.facetLimit;
    }

}
