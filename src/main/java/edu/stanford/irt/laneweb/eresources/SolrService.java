package edu.stanford.irt.laneweb.eresources;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.FacetOptions.FieldWithFacetParameters;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

public class SolrService {

    public static final String FACETS_SEPARATOR = "::";

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final String AND = " AND ";

    private static final String DATE_QUERY_PREFIX = "date:[";

    private static final String DATE_QUERY_SUFFIX = " TO *]";

    private static final String EMPTY = "";

    private static final Collection<String> FACET_FIELDS = Arrays.asList("mesh", "publicationAuthor",
            "publicationLanguage", "publicationTitle", "publicationType", "type", "recordType", "year", "date");

    private static final String NULL_TYPE = "null type";

    private static final int PAGE_SIZE = 10;

    private static final int THIS_YEAR = ZonedDateTime.now(AMERICA_LA).getYear();

    private static final int PAST_FIVE_YEARS = THIS_YEAR - 5;

    private static final int PAST_TEN_YEARS = THIS_YEAR - 10;

    private static final int PAST_YEAR = THIS_YEAR - 1;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");

    private SolrQueryParser parser;

    private SolrRepository repository;

    private SolrTemplate solrTemplate;

    @Autowired
    public SolrService(final SolrQueryParser parser, final SolrRepository repository,
            @Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        this.parser = parser;
        this.repository = repository;
        this.solrTemplate = solrTemplate;
    }

    public FacetPage<Eresource> facetByField(final String query, final String filters, final String field,
            final int pageNumber, final int facetLimit, final int facetMinCount, final FacetSort facetSort) {
        PageRequest pageRequest = new PageRequest(pageNumber, facetLimit);
        String facetFilters = facetStringToFilters(filters);
        int modifiedOffset = (facetLimit - 1) * pageNumber;
        FieldWithFacetParameters fieldWithFacetParams = new FieldWithFacetParameters(field);
        fieldWithFacetParams.setOffset(Integer.valueOf(modifiedOffset));
        String cleanQuery = this.parser.parse(query);
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(cleanQuery)).setFacetOptions(
                new FacetOptions().addFacetOnField(fieldWithFacetParams).setFacetMinCount(facetMinCount)
                        .setFacetLimit(pageRequest.getPageSize()).setFacetSort(facetSort));
        fquery.setRequestHandler(SolrRepository.Handlers.FACET);
        if (!facetFilters.isEmpty()) {
            fquery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(facetFilters)));
        }
        return this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
    }

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters, final int facetLimit) {
        String facetFilters = facetStringToFilters(filters);
        String cleanQuery = this.parser.parse(query);
        String monthDay = LocalDate.now(AMERICA_LA).format(this.formatter);
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnFlieldnames(FACET_FIELDS);
        facetOptions.setFacetMinCount(1);
        facetOptions.setFacetLimit(facetLimit);
        // TODO: use String.format(format, args) for these:
        facetOptions.addFacetQuery(new SimpleQuery(DATE_QUERY_PREFIX + PAST_YEAR + monthDay + DATE_QUERY_SUFFIX));
        facetOptions.addFacetQuery(new SimpleQuery(DATE_QUERY_PREFIX + PAST_FIVE_YEARS + monthDay + DATE_QUERY_SUFFIX));
        facetOptions.addFacetQuery(new SimpleQuery(DATE_QUERY_PREFIX + PAST_TEN_YEARS + monthDay + DATE_QUERY_SUFFIX));
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(cleanQuery)).setFacetOptions(facetOptions);
        fquery.setRequestHandler(SolrRepository.Handlers.FACET);
        if (!facetFilters.isEmpty()) {
            fquery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(facetFilters)));
        }
        return this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
    }

    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        return this.repository.browseAllCoreByType(type, new PageRequest(0, Integer.MAX_VALUE));
    }

    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return this.repository.browseAllByMeshAndType(mesh, type, new PageRequest(0, Integer.MAX_VALUE));
    }

    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        return this.repository.browseAllByType(type, new PageRequest(0, Integer.MAX_VALUE));
    }

    public List<Eresource> getType(final String type, final char alpha) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        char sAlpha = alpha;
        // solr stores starts with numeric as '1'
        if ('#' == sAlpha) {
            sAlpha = '1';
        }
        return this.repository.browseByTypeTitleStartingWith(type, Character.toString(sAlpha),
                new PageRequest(0, Integer.MAX_VALUE));
    }

    public Map<String, Long> searchCount(final String query) {
        Map<String, Long> result = new HashMap<>();
        SolrResultPage<Eresource> facets = this.repository.facetByType(this.parser.parse(query), new PageRequest(0, 1));
        result.put("all", Long.valueOf(facets.getTotalElements()));
        for (Page<FacetFieldEntry> page : facets.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                result.put(entry.getValue(), Long.valueOf(entry.getValueCount()));
            }
        }
        return result;
    }

    public Page<Eresource> searchType(final String type, final String query, final Pageable pageRequest) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        String cleanQuery = this.parser.parse(query);
        return this.repository.searchFindByType(cleanQuery, type, pageRequest);
    }

    public Page<Eresource> searchWithFilters(final String query, final String facets, final Pageable pageRequest) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.searchFindAllWithFilter(cleanQuery, facetStringToFilters(facets), pageRequest);
    }

    public List<Eresource> suggestFindAll(final String query) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindAll(cleanQuery.toLowerCase(), cleanQuery.replaceAll(" ", " +"),
                new PageRequest(0, PAGE_SIZE));
    }

    public List<Eresource> suggestFindByType(final String query, final String type) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindByType(cleanQuery, type, new PageRequest(0, PAGE_SIZE));
    }

    private String facetStringToFilters(final String facets) {
        String filters = EMPTY;
        if (null != facets) {
            filters = facets.replaceFirst(FACETS_SEPARATOR + "$", EMPTY);
            filters = filters.replaceAll(FACETS_SEPARATOR, AND);
        }
        return filters;
    }
}