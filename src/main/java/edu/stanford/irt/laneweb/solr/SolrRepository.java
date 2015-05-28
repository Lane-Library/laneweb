package edu.stanford.irt.laneweb.solr;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import edu.stanford.irt.laneweb.eresources.Eresource;

public interface SolrRepository extends SolrCrudRepository<Eresource, String> {

    static final String BROWSE_HANDLER = "/lane-browse";

    static final String FACET_HANDLER = "/lane-facet";

    static final String SEARCH_HANDLER = "/lane-search";

    static final String SUGGEST_HANDLER = "/lane-suggest";

    @Query(value = "*:*", filters = { "isRecent:true", "mesh:\"?0\"", "type:\"?1\"" }, requestHandler = BROWSE_HANDLER)
    public List<Eresource> browseAllByMeshAndType(String mesh, String type, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true", "subset:\"?0\"" }, requestHandler = BROWSE_HANDLER)
    public List<Eresource> browseAllBySubset(String subset, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true", "type:\"?0\"" }, requestHandler = BROWSE_HANDLER)
    public List<Eresource> browseAllByType(String type, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true", "isCore:1", "type:\"?0\"" }, requestHandler = BROWSE_HANDLER)
    public List<Eresource> browseAllCoreByType(String type, Pageable page);

    @Query(value = "ertlsw?1", filters = { "isRecent:true", "type:\"?0\"" }, requestHandler = BROWSE_HANDLER)
    public List<Eresource> browseByTypeTitleStartingWith(String type, String titleStart, Pageable page);

    @Query(value = "?0", filters = { "?1" }, requestHandler = FACET_HANDLER)
    @Facet(queries = { "*:*" }, limit = 11)
    // field list configured in solr lane-facet request handler (in solrconfig.xml)
    public FacetPage<Eresource> facetByManyFields(String term, String filter, Pageable page);

    @Query(value = "?0", requestHandler = FACET_HANDLER)
    @Facet(fields = { "type" }, minCount = 0, limit = 100)
    public SolrResultPage<?> facetByType(String term, Pageable page);

    // test
    @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    @Highlight
    public HighlightPage<Eresource> findAllHighlighted(String text, Pageable page);

    // @Query(value = "?0", requestHandler = SEARCH_HANDLER)
    // public Page<Eresource> searchFindAll(String text, Pageable page);
    //
    @Query(value = "-recordType:pubmed", requestHandler = SEARCH_HANDLER)
    public List<Eresource> searchFindAllNotRecordTypePubmed(Pageable page);

    @Query(value = "?0", filters = { "?1" }, requestHandler = SEARCH_HANDLER)
    public Page<Eresource> searchFindAllWithFilter(String query, String filter, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = SEARCH_HANDLER)
    public Page<Eresource> searchFindByType(String query, String type, Pageable page);

    @Query(value = "(+?1) OR title_sort:/?0.*/", requestHandler = SUGGEST_HANDLER)
    public List<Eresource> suggestFindAll(String term, String tokenizedTerm, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = SUGGEST_HANDLER)
    public List<Eresource> suggestFindByType(String term, String type, Pageable page);
}
