package edu.stanford.irt.laneweb.eresources;

import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
@Repository
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

    public final class HighlightTags {

        public static final String END = ":::";

        public static final String START = "___";

        private HighlightTags() {
            // empty private constructor
        }
    }

    @Query(value = "*:*", requestHandler = Handlers.FACET)
    @Facet(fields = { "recordType" }, minCount = 0, limit = 100)
    SolrResultPage<Eresource> facetByRecordType(Pageable page);

    @Query(value = "?0", requestHandler = Handlers.FACET)
    @Facet(fields = { "type" }, minCount = 0, limit = 100)
    SolrResultPage<Eresource> facetByType(String term, Pageable page);

    @Query(value = "*:*", filters = { "id:bib-?0" }, requestHandler = Handlers.SEARCH)
    Eresource getByBibID(String bibID);

    @Query(value = "?0", filters = { "?1" }, requestHandler = Handlers.SEARCH)
    @Highlight(fragsize = Integer.MAX_VALUE, prefix = HighlightTags.START, postfix = HighlightTags.END, fields = {
            "title", "description", "publicationAuthorsText", "publicationText" })
    HighlightPage<Eresource> searchFindAllWithFilter(String query, String filter, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SEARCH)
    @Highlight(fragsize = Integer.MAX_VALUE, prefix = HighlightTags.START, postfix = HighlightTags.END, fields = {
            "title", "description", "publicationAuthorsText", "publicationText" })
    HighlightPage<Eresource> searchFindByType(String query, String type, Pageable page);

    @Query(value = "(+?1) title_sort:/?0.*/", requestHandler = Handlers.SUGGEST)
    List<Eresource> suggestFindAll(String term, String tokenizedTerm, Pageable page);

    @Query(value = "?0", filters = { "type:\"?1\"" }, requestHandler = Handlers.SUGGEST)
    List<Eresource> suggestFindByType(String term, String type, Pageable page);
}
