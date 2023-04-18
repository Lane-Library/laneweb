package edu.stanford.irt.laneweb.eresources;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.FacetOptions.FieldWithFacetParameters;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

public class SolrService {

    public static final String FACETS_SEPARATOR = "::";

    private static final String ALL_QUERY = "*:*";

    private static final String AND = " AND ";

    // consider moving this to /lane-browse solr handler
    private static final SimpleFilterQuery BASE_FQ = new SimpleFilterQuery(
            new SimpleStringCriteria("recordType:bib AND isRecent:1"));

    private static final String DATE_QUERY = "date:[%s TO *]";

    private static final String EMPTY = "";

    private static final Collection<String> FACET_FIELDS = Arrays.asList("mesh", "publicationAuthor",
            "publicationLanguage", "publicationTitle", "publicationType", "type", "recordType", "year", "date");

    private static final Pattern FACETS_LAST_SEPARATOR_PATTERN = Pattern.compile(FACETS_SEPARATOR + "$");

    private static final Pattern FACETS_SEPARATOR_PATTERN = Pattern.compile(FACETS_SEPARATOR);

    private static final String NULL_QUERY = "null query";

    private static final String NULL_TYPE = "null type";

    private static final int PAGE_SIZE = 10;

    private static final Pattern SINGLE_SPACE_PATTERN = Pattern.compile(" ");

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private SolrQueryParser parser;

    private SolrRepository repository;

    private String solrCollectionName;

    private SolrTemplate solrTemplate;

    public SolrService(final SolrQueryParser parser, final SolrRepository repository, final SolrTemplate solrTemplate,
            final String solrCollectionName) {
        this.parser = parser;
        this.repository = repository;
        this.solrTemplate = solrTemplate;
        this.solrCollectionName = solrCollectionName;
    }

    public List<Eresource> browseByQuery(final String query) {
        if (null == query) {
            throw new IllegalArgumentException(NULL_QUERY);
        }
        SimpleQuery q = buildBaseBrowseQuery(ALL_QUERY);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(query)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(this.solrCollectionName, q, Eresource.class);
        return cursorToList(cursor);
    }

    public List<Eresource> browseByQuery(final String query, final char alpha) {
        if (null == query) {
            throw new IllegalArgumentException(NULL_QUERY);
        }
        char sAlpha = alpha;
        // solr stores starts with numeric as '1'
        if ('#' == sAlpha) {
            sAlpha = '1';
        }
        SimpleQuery q = buildBaseBrowseQuery("ertlsw" + sAlpha);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(query)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(this.solrCollectionName, q, Eresource.class);
        return cursorToList(cursor);
    }

    public FacetPage<Eresource> facetByField(final String query, final String filters, final String field,
            final int pageNumber, final int facetLimit, final int facetMinCount, final FacetSort facetSort) {
        PageRequest pageRequest = PageRequest.of(pageNumber, facetLimit);
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
        return this.solrTemplate.queryForFacetPage(this.solrCollectionName, fquery, Eresource.class);
    }

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters, final int facetLimit) {
        String facetFilters = facetStringToFilters(filters);
        String cleanQuery = this.parser.parse(query);
        FacetOptions facetOptions = new FacetOptions();
        facetOptions.addFacetOnFlieldnames(FACET_FIELDS);
        facetOptions.setFacetMinCount(1);
        facetOptions.setFacetLimit(facetLimit);
        LocalDate now = LocalDate.now();
        String lastYear = now.minus(1, ChronoUnit.YEARS).format(this.formatter);
        String lastFiveYears = now.minus(5, ChronoUnit.YEARS).format(this.formatter);
        String lastTenYears = now.minus(10, ChronoUnit.YEARS).format(this.formatter);
        facetOptions.addFacetQuery(new SimpleQuery(String.format(DATE_QUERY, lastYear)));
        facetOptions.addFacetQuery(new SimpleQuery(String.format(DATE_QUERY, lastFiveYears)));
        facetOptions.addFacetQuery(new SimpleQuery(String.format(DATE_QUERY, lastTenYears)));
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(cleanQuery)).setFacetOptions(facetOptions);
        fquery.setRequestHandler(SolrRepository.Handlers.FACET);
        if (!facetFilters.isEmpty()) {
            fquery.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(facetFilters)));
        }
        return this.solrTemplate.queryForFacetPage(this.solrCollectionName, fquery, Eresource.class);
    }

    public Eresource getByBibID(final String bibID) {
        return this.repository.getByBibID(bibID);
    }

    public Map<String, Long> recordCount() {
        Map<String, Long> result = new HashMap<>();
        SolrResultPage<Eresource> facets = this.repository.facetByRecordType(PageRequest.of(0, 1));
        for (Page<FacetFieldEntry> page : facets.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                result.put(entry.getValue(), Long.valueOf(entry.getValueCount()));
            }
        }
        return result;
    }

    public Map<String, Long> searchCount(final String query) {
        Map<String, Long> result = new HashMap<>();
        SolrResultPage<Eresource> facets = this.repository.facetByType(this.parser.parse(query), PageRequest.of(0, 1));
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
        String parsedQuery = this.parser.parse(query);
        String plusQuery = SINGLE_SPACE_PATTERN.matcher(parsedQuery).replaceAll(" +");
        return this.repository.suggestFindAll(parsedQuery.toLowerCase(Locale.US), plusQuery,
                PageRequest.of(0, PAGE_SIZE));
    }

    public List<Eresource> suggestFindByType(final String query, final String type) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindByType(cleanQuery, type, PageRequest.of(0, PAGE_SIZE));
    }

    private SimpleQuery buildBaseBrowseQuery(final String query) {
        SimpleQuery q = new SimpleQuery(query);
        q.setRequestHandler(SolrRepository.Handlers.BROWSE);
        q.addSort(Sort.by("title_sort", "id"));
        q.addFilterQuery(BASE_FQ);
        q.setTimeAllowed(Integer.MIN_VALUE);
        return q;
    }

    private List<Eresource> cursorToList(final Cursor<Eresource> cursor) {
        List<Eresource> ers = new ArrayList<>();
        while (cursor.hasNext()) {
            ers.add(cursor.next());
        }
        return ers;
    }

    private String facetStringToFilters(final String facets) {
        String filters = EMPTY;
        if (null != facets) {
            filters = FACETS_LAST_SEPARATOR_PATTERN.matcher(facets).replaceFirst(EMPTY);
            filters = FACETS_SEPARATOR_PATTERN.matcher(filters).replaceAll(AND);
        }
        return filters;
    }
}
