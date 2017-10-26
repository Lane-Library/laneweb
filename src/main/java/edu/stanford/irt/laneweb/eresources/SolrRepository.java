package edu.stanford.irt.laneweb.eresources;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

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

    @Query(value = "*:*", requestHandler = Handlers.FACET)
    @Facet(fields = { "recordType" }, minCount = 0, limit = 100)
    SolrResultPage<Eresource> facetByRecordType(Pageable page);

    @Query(value = "?0", requestHandler = Handlers.FACET)
    @Facet(fields = { "type" }, minCount = 0, limit = 100)
    SolrResultPage<Eresource> facetByType(String term, Pageable page);

    @Query(value = "*:*", filters = { "id:bib-?0" }, requestHandler = Handlers.BROWSE)
    Eresource getByBibID(String bibID);

    @Query(value = "?0", filters = { "?1" }, requestHandler = Handlers.SEARCH)
    Page<Eresource> searchFindAllWithFilter(String query, String filter, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SEARCH)
    Page<Eresource> searchFindByType(String query, String type, Pageable page);

    @Query(value = "(+?1) OR title_sort:/?0.*/", requestHandler = Handlers.SUGGEST)
    List<Eresource> suggestFindAll(String term, String tokenizedTerm, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SUGGEST)
    List<Eresource> suggestFindByType(String term, String type, Pageable page);
}
