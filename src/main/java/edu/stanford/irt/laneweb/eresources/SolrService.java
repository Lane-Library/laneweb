package edu.stanford.irt.laneweb.eresources;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private static final ZoneId AMERICA_LA = ZoneId.of("America/Los_Angeles");

    private static final String AND = " AND ";

    private static final SimpleFilterQuery BASE_FQ = new SimpleFilterQuery(
            new SimpleStringCriteria("isRecent:1 OR isLaneConnex:1"));

    private static final SimpleFilterQuery CORE_FQ = new SimpleFilterQuery(new SimpleStringCriteria("isCore:1"));

    private static final int CURRENT_YEAR = ZonedDateTime.now(AMERICA_LA).getYear();

    private static final String DATE_QUERY_PREFIX = "date:[";

    private static final String DATE_QUERY_SUFFIX = " TO *]";

    private static final String EMPTY = "";

    private static final Collection<String> FACET_FIELDS = Arrays.asList("mesh", "publicationAuthor",
            "publicationLanguage", "publicationTitle", "publicationType", "type", "recordType", "year", "date");

    private static final String NULL_TYPE = "null type";

    private static final int PAGE_SIZE = 10;

    private static final int PAST_FIVE_YEARS = CURRENT_YEAR - 5;

    private static final int PAST_TEN_YEARS = CURRENT_YEAR - 10;

    private static final int PAST_YEAR = CURRENT_YEAR - 1;

    private static final String TYPE = "type:";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");

    private SolrQueryParser parser;

    private SolrRepository repository;

    private SolrTemplate solrTemplate;

    public SolrService(final SolrQueryParser parser, final SolrRepository repository, final SolrTemplate solrTemplate) {
        this.parser = parser;
        this.repository = repository;
        this.solrTemplate = solrTemplate;
    }

    private static final SimpleQuery baseBrowseQuery(final String query) {
        SimpleQuery q = new SimpleQuery(query);
        q.setRequestHandler(SolrRepository.Handlers.BROWSE);
        q.addSort(new Sort("title_sort", "id"));
        q.addFilterQuery(BASE_FQ);
        q.setTimeAllowed(Integer.MIN_VALUE);
        return q;
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

    public Eresource getByBibID(final String bibID) {
        return this.repository.getByBibID(bibID);
    }

    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        return browseAllCoreByType(type);
    }

    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return browseAllByMeshAndType(mesh, type);
    }

    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException(NULL_TYPE);
        }
        return browseAllByType(type);
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
        return browseByTypeTitleStartingWith(type, Character.toString(sAlpha));
    }

    public Map<String, Long> recordCount() {
        Map<String, Long> result = new HashMap<>();
        SolrResultPage<Eresource> facets = this.repository.facetByRecordType(new PageRequest(0, 1));
        for (Page<FacetFieldEntry> page : facets.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                result.put(entry.getValue(), Long.valueOf(entry.getValueCount()));
            }
        }
        return result;
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
        return this.repository.suggestFindAll(cleanQuery.toLowerCase(Locale.US), cleanQuery.replaceAll(" ", " +"),
                new PageRequest(0, PAGE_SIZE));
    }

    public List<Eresource> suggestFindByType(final String query, final String type) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindByType(cleanQuery, type, new PageRequest(0, PAGE_SIZE));
    }

    private List<Eresource> browseAllByMeshAndType(final String mesh, final String type) {
        SimpleQuery q = baseBrowseQuery(ALL_QUERY);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(TYPE + type)));
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria("mesh:" + mesh)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(q, Eresource.class);
        return cursorToList(cursor);
    }

    private List<Eresource> browseAllByType(final String type) {
        SimpleQuery q = baseBrowseQuery(ALL_QUERY);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(TYPE + type)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(q, Eresource.class);
        return cursorToList(cursor);
    }

    private List<Eresource> browseAllCoreByType(final String type) {
        SimpleQuery q = baseBrowseQuery(ALL_QUERY);
        q.addFilterQuery(CORE_FQ);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(TYPE + type)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(q, Eresource.class);
        return cursorToList(cursor);
    }

    private List<Eresource> browseByTypeTitleStartingWith(final String type, final String titleStart) {
        SimpleQuery q = baseBrowseQuery("ertlsw" + titleStart);
        q.addFilterQuery(new SimpleFilterQuery(new SimpleStringCriteria(TYPE + type)));
        Cursor<Eresource> cursor = this.solrTemplate.queryForCursor(q, Eresource.class);
        return cursorToList(cursor);
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
            filters = facets.replaceFirst(FACETS_SEPARATOR + "$", EMPTY);
            filters = filters.replaceAll(FACETS_SEPARATOR, AND);
        }
        return filters;
    }
}