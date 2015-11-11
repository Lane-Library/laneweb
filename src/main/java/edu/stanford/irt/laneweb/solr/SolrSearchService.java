package edu.stanford.irt.laneweb.solr;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;

public class SolrSearchService implements CollectionManager {

    public static final String FACETS_SEPARATOR = "::";

    private static final String AND = " AND ";

    private static final String EMPTY = "";

    private static final Collection<String> FACET_FIELDS = Arrays.asList("mesh", "publicationAuthor",
            "publicationLanguage", "publicationTitle", "publicationType", "type", "recordType", "year");

    private static final FacetOptions FACET_OPTIONS = new FacetOptions();

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    static {
        FACET_OPTIONS.setFacetMinCount(1);
        FACET_OPTIONS.addFacetOnFlieldnames(FACET_FIELDS);
        FACET_OPTIONS.addFacetQuery(new SimpleQuery("year:[" + (THIS_YEAR - 5) + " TO *]"));
        FACET_OPTIONS.addFacetQuery(new SimpleQuery("year:[" + (THIS_YEAR - 10) + " TO *]"));
    }

    @Autowired
    // protected for unit test ... better way to do this?
    protected SolrQueryParser parser;

    @Autowired
    // protected for unit test ... better way to do this?
    protected SolrRepository repository;

    @Autowired
    @Qualifier(value = "laneSearchSolrTemplate")
    // protected for unit test ... better way to do this?
    protected SolrTemplate solrTemplate;

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
            fquery.addCriteria(new SimpleStringCriteria(facetFilters));
        }
        return this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
    }

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters, final int facetLimit) {
        String facetFilters = facetStringToFilters(filters);
        String cleanQuery = this.parser.parse(query);
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(cleanQuery))
                .setFacetOptions(FACET_OPTIONS.setFacetLimit(facetLimit));
        fquery.setRequestHandler(SolrRepository.Handlers.FACET);
        if (!facetFilters.isEmpty()) {
            fquery.addCriteria(new SimpleStringCriteria(facetFilters));
        }
        return this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
    }

    @Override
    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        String newType = SolrTypeManager.convertToNewType(type);
        return this.repository.browseAllCoreByType(newType, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        String newType = SolrTypeManager.convertToNewType(type);
        return this.repository.browseAllByMeshAndType(mesh, newType, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getSubset(final String subset) {
        if (null == subset) {
            throw new IllegalArgumentException("null subset");
        }
        return this.repository.browseAllBySubset(subset, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        String newType = SolrTypeManager.convertToNewType(type);
        return this.repository.browseAllByType(newType, new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getType(final String type, final char alpha) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        char sAlpha = alpha;
        // solr stores starts with numeric as '1'
        if ('#' == sAlpha) {
            sAlpha = '1';
        }
        return this.repository.browseByTypeTitleStartingWith(SolrTypeManager.convertToNewType(type),
                Character.toString(sAlpha), new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> search(final String query) {
        return this.repository.searchFindAllWithFilter(this.parser.parse(query), EMPTY, new PageRequest(0, 50))
                .getContent();
    }

    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        SolrResultPage<?> facets = this.repository.facetByType(this.parser.parse(query), new PageRequest(0, 1));
        int total = (int) facets.getTotalElements();
        result.put("all", Integer.valueOf(total));
        for (Page<FacetFieldEntry> page : facets.getFacetResultPages()) {
            for (FacetFieldEntry entry : page) {
                Integer value = Integer.valueOf((int) entry.getValueCount());
                String fieldName = entry.getValue();
                String bwCompatibleFieldName = SolrTypeManager.convertToOldType(fieldName);
                if (types.contains(fieldName)) {
                    result.put(fieldName, value);
                } else if (types.contains(bwCompatibleFieldName)) {
                    result.put(bwCompatibleFieldName, value);
                }
            }
        }
        return result;
    }

    public List<Eresource> searchFindAllNotRecordTypePubmed() {
        return this.repository.searchFindAllNotRecordTypePubmed(new PageRequest(0, Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        String cleanQuery = this.parser.parse(query);
        String newType = SolrTypeManager.convertToNewType(type);
        return this.repository.searchFindByType(cleanQuery, newType, new PageRequest(0, 50)).getContent();
    }

    public Page<Eresource> searchType(final String type, final String query, final Pageable pageRequest) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        String cleanQuery = this.parser.parse(query);
        return this.repository.searchFindByType(cleanQuery, SolrTypeManager.convertToNewType(type), pageRequest);
    }

    public Page<Eresource> searchWithFilters(final String query, final String facets, final Pageable pageRequest) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.searchFindAllWithFilter(cleanQuery, facetStringToFilters(facets), pageRequest);
    }

    public List<Eresource> suggestFindAll(final String query) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindAll(cleanQuery.toLowerCase(), cleanQuery.replaceAll(" ", " +"),
                new PageRequest(0, 10));
    }

    public List<Eresource> suggestFindByType(final String query, final String type) {
        String cleanQuery = this.parser.parse(query);
        return this.repository.suggestFindByType(cleanQuery, SolrTypeManager.convertToNewType(type),
                new PageRequest(0, 10));
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