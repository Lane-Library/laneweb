package edu.stanford.irt.laneweb.images;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.search.AbstractSolrSearchResult;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchResult extends AbstractSolrSearchResult<Image> {

    public static final SolrImageSearchResult EMPTY_RESULT = new SolrImageSearchResult(null,
            new SolrResultPage<Image>(Collections.emptyList()), null,
            new SolrResultPage<FacetFieldEntry>(Collections.emptyList()), null, null, null);

    private Page<FacetFieldEntry> facet;

    private String path;

    private String selectedResource;

    private String source;

    private String tab;

    public SolrImageSearchResult(final String query, final Page<Image> page, final String selectedResource,
            final Page<FacetFieldEntry> facet, final String path, final String tab, final String source) {
        super(query, page);
        this.selectedResource = selectedResource;
        this.facet = facet;
        this.path = path;
        this.tab = tab;
        this.source = source;
    }

    public Page<FacetFieldEntry> getFacet() {
        return this.facet;
    }

    public String getPath() {
        return this.path;
    }

    public String getSelectedResource() {
        return this.selectedResource;
    }

    public String getSource() {
        return this.source;
    }

    public String getTab() {
        return this.tab;
    }
}
