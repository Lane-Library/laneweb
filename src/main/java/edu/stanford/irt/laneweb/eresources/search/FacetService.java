
package edu.stanford.irt.laneweb.eresources.search;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.FacetOptions.FieldWithFacetParameters;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.laneweb.eresources.SolrRepository;

public class FacetService {

    private static final String EMPTY = "";

    public static final String FACETS_SEPARATOR = "::";

    private static final String COLLECTION = "laneSearch";

    private static final Pattern FACETS_LAST_SEPARATOR_PATTERN = Pattern.compile(FACETS_SEPARATOR + "$");

    private static final Pattern FACETS_SEPARATOR_PATTERN = Pattern.compile(FACETS_SEPARATOR);

    private static final String AND = " AND ";

    private SolrQueryParser parser;

    private SolrTemplate solrTemplate;

    private Collection<String> facetFields;

    public FacetService(final SolrQueryParser parser, final SolrTemplate solrTemplate) {
        this.parser = parser;
        this.solrTemplate = solrTemplate;
    }

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters, final int facetLimit) {
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnFlieldnames(this.facetFields);
        facetOptions.setFacetMinCount(1);
        facetOptions.setFacetLimit(facetLimit);
        return getFacetPage(query, facetOptions, filters);
    }

    public FacetPage<Eresource> facetByFieldContains(final String query, final String searchTerm, final String field,
            final String filters, final int facetMinCount) {
        FieldWithFacetParameters fieldWithFacetParams = new FieldWithFacetParameters(field);
        fieldWithFacetParams.addFacetParameter("facet.contains.ignoreCase", true);
        fieldWithFacetParams.addFacetParameter("facet.contains", query);
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnField(fieldWithFacetParams).setFacetMinCount(facetMinCount);
        return getFacetPage(searchTerm, facetOptions, filters);
    }

    public FacetPage<Eresource> facetByField(final String query, final String filters, final String field,
            final int facetLimit, final int facetMinCount, final FacetSort facetSort) {
        FieldWithFacetParameters fieldWithFacetParams = new FieldWithFacetParameters(field);
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.setFacetLimit(facetLimit);
        facetOptions.setFacetSort(facetSort);
        facetOptions.addFacetOnField(fieldWithFacetParams).setFacetMinCount(facetMinCount);
        return getFacetPage(query, facetOptions, filters);
    }

    private FacetPage<Eresource> getFacetPage(final String searchTerm, FacetOptions facetOptions,
            final String filters) {
        String facetFilters = facetStringToFilters(filters);
        String cleanQuery = this.parser.parse(searchTerm);
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(cleanQuery)).setFacetOptions(facetOptions);
        fquery.setRequestHandler(SolrRepository.Handlers.FACET);
        if (!facetFilters.isEmpty()) {
            fquery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(facetFilters)));
        }
        return this.solrTemplate.queryForFacetPage(COLLECTION, fquery, Eresource.class);
    }

    private String facetStringToFilters(final String facets) {
        String filters = EMPTY;
        if (null != facets) {
            filters = FACETS_LAST_SEPARATOR_PATTERN.matcher(facets).replaceFirst(EMPTY);
            filters = FACETS_SEPARATOR_PATTERN.matcher(filters).replaceAll(AND);
        }
        return filters;
    }

    public void setFacets(Collection<String> facetFields) {
        this.facetFields = facetFields;
    }
}
