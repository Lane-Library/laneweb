package edu.stanford.irt.laneweb.solr;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface SolrRepository extends SolrCrudRepository<SolrEresource, String> {

    static final String BROWSE_HANDLER = "/lane-browse";

    static final String SEARCH_HANDLER = "/lane-search";

    static final String SUGGEST_HANDLER = "/lane-suggest";

    @Query(value = "ertlsw?1", filters = { "type:\"?0\"" }, requestHandler = BROWSE_HANDLER)
    public List<SolrEresource> browseFindByTypeTitleStartingWith(String type, String titleStart, Pageable page);

    // test
    @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    @Highlight
    public HighlightPage<SolrEresource> findAllHighlighted(String text, Pageable page);

    // test
    @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    @Facet(fields = { "type", "mesh", "year", "publicationTitle", "publicationAuthor", "publicationType", "publicationLanguage", "isRecent", "isEnglish" }, limit = 10)
    public FacetPage<SolrEresource> searchFacetByManyFields(String term, Pageable page);

    @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    @Facet(fields = { "type" }, minCount = 0, limit = 100)
    public SolrResultPage<?> searchFacetByType(String term, Pageable page);

    @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAll(String text, Pageable page);

    @Query(value = "*:*", filters = { "mesh:?0", "type:\"?1\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAllByMeshAndType(String mesh, String type, Pageable page);

    @Query(value = "*:*", filters = { "subset:\"?0\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAllBySubset(String subset, Pageable page);

    @Query(value = "*:*", filters = { "type:\"?0\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAllByType(String type, Pageable page);

    @Query(value = "*:*", filters = { "isCore:1", "type:\"?0\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAllCoreByType(String type, Pageable page);

    @Query(value = "-recordType:pubmed", requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindAllNotRecordTypePubmed(Pageable page);

    @Query(value = "?0", filters = { "subset:\"?1\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindBySubset(String query, String subset, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = SEARCH_HANDLER)
    public List<SolrEresource> searchFindByType(String query, String type, Pageable page);

    @Query(value = "(+?1) OR title_sort:/?0.*/", requestHandler = SUGGEST_HANDLER)
    public List<SolrEresource> suggestFindAll(String term, String tokenizedTerm, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = SUGGEST_HANDLER)
    public List<SolrEresource> suggestFindByType(String term, String type, Pageable page);
}
