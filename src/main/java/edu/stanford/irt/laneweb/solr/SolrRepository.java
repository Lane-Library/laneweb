package edu.stanford.irt.laneweb.solr;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import edu.stanford.irt.laneweb.eresources.Eresource;

public interface SolrRepository extends SolrCrudRepository<Eresource, String> {

    public final class Handlers {

        public static final String BROWSE = "/lane-browse";

        public static final String FACET = "/lane-facet";

        public static final String SEARCH = "/lane-search";

        public static final String SUGGEST = "/lane-suggest";

        private Handlers() {
            // empty private constructor
        }
    }

    @Query(value = "*:*", filters = { "isRecent:true OR isLaneConnex:1", "mesh:\"?0\"", "type:\"?1\"" }, requestHandler = Handlers.BROWSE)
    public List<Eresource> browseAllByMeshAndType(String mesh, String type, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true OR isLaneConnex:1", "subset:\"?0\"" }, requestHandler = Handlers.BROWSE)
    public List<Eresource> browseAllBySubset(String subset, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true OR isLaneConnex:1", "type:\"?0\"" }, requestHandler = Handlers.BROWSE)
    public List<Eresource> browseAllByType(String type, Pageable page);

    @Query(value = "*:*", filters = { "isRecent:true OR isLaneConnex:1", "isCore:1", "type:\"?0\"" }, requestHandler = Handlers.BROWSE)
    public List<Eresource> browseAllCoreByType(String type, Pageable page);

    @Query(value = "ertlsw?1", filters = { "isRecent:true OR isLaneConnex:1", "type:\"?0\"" }, requestHandler = Handlers.BROWSE)
    public List<Eresource> browseByTypeTitleStartingWith(String type, String titleStart, Pageable page);

    @Query(value = "?0", requestHandler = Handlers.FACET)
    @Facet(fields = { "type" }, minCount = 0, limit = 100)
    public SolrResultPage<?> facetByType(String term, Pageable page);

    @Query(value = "-recordType:pubmed", requestHandler = Handlers.SEARCH)
    public List<Eresource> searchFindAllNotRecordTypePubmed(Pageable page);

    @Query(value = "?0", filters = { "?1" }, requestHandler = Handlers.SEARCH)
    public Page<Eresource> searchFindAllWithFilter(String query, String filter, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SEARCH)
    public Page<Eresource> searchFindByType(String query, String type, Pageable page);

    @Query(value = "(+?1) OR title_sort:/?0.*/", requestHandler = Handlers.SUGGEST)
    public List<Eresource> suggestFindAll(String term, String tokenizedTerm, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SUGGEST)
    public List<Eresource> suggestFindByType(String term, String type, Pageable page);
}