package edu.stanford.irt.laneweb.solr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FacetOptions;
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

    private static final Collection<String> FACET_FIELDS = new ArrayList<String>();

    private static final FacetOptions FACET_OPTIONS = new FacetOptions();

    private static final int FACETS_BROWSE_SIZE = 21;

    private static final int FACETS_LIST_SIZE = 11;

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    static {
        FACET_FIELDS.add("mesh");
        FACET_FIELDS.add("publicationAuthor");
        FACET_FIELDS.add("publicationLanguage");
        FACET_FIELDS.add("publicationTitle");
        FACET_FIELDS.add("publicationType");
        FACET_FIELDS.add("type");
        FACET_FIELDS.add("year");
    }
    static {
        FACET_OPTIONS.setFacetMinCount(1);
        FACET_OPTIONS.addFacetOnFlieldnames(FACET_FIELDS);
        FACET_OPTIONS.addFacetQuery(new SimpleQuery("year:[" + (THIS_YEAR - 5) + " TO *]"));
        FACET_OPTIONS.addFacetQuery(new SimpleQuery("year:[" + (THIS_YEAR - 10) + " TO *]"));
    }

    @Autowired
    private SolrRepository repository;

    @Autowired
    private SolrTemplate solrTemplate;

    public FacetPage<Eresource> facetByField(final String query, final String filters, final String field,
            final int pageNumber) {
        PageRequest pageRequest = new PageRequest(pageNumber, FACETS_BROWSE_SIZE);
        String facetFilters = facetStringToFilters(filters);
        int modifiedOffset = (pageRequest.getOffset() == 0) ? 0 : pageRequest.getOffset() - 1;
        FieldWithFacetParameters fieldWithFacetParams = new FieldWithFacetParameters(field);
        fieldWithFacetParams.setOffset(Integer.valueOf(modifiedOffset));
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(query)).setFacetOptions(new FacetOptions()
        .addFacetOnField(fieldWithFacetParams).setFacetMinCount(1).setFacetLimit(pageRequest.getPageSize()));
        fquery.setRequestHandler(SolrRepository.FACET_HANDLER);
        if (!facetFilters.isEmpty()) {
            fquery.addCriteria(new SimpleStringCriteria(facetFilters));
        }
        return this.solrTemplate.queryForFacetPage(fquery, Eresource.class);
    }

    public FacetPage<Eresource> facetByManyFields(final String query, final String filters) {
        String facetFilters = facetStringToFilters(filters);
        FacetQuery fquery = new SimpleFacetQuery(new SimpleStringCriteria(query)).setFacetOptions(FACET_OPTIONS
                .setFacetLimit(FACETS_LIST_SIZE));
        fquery.setRequestHandler(SolrRepository.FACET_HANDLER);
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
        return this.repository.browseAllCoreByType(SolrTypeManager.convertToNewType(type), new PageRequest(0,
                Integer.MAX_VALUE));
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        return this.repository.browseAllByMeshAndType(mesh, SolrTypeManager.convertToNewType(type), new PageRequest(0,
                Integer.MAX_VALUE));
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
        return this.repository.browseAllByType(SolrTypeManager.convertToNewType(type), new PageRequest(0,
                Integer.MAX_VALUE));
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
        return this.repository.searchFindAllWithFilter(query, EMPTY, new PageRequest(0, 50)).getContent();
    }

    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        SolrResultPage<?> facets = this.repository.facetByType(query, new PageRequest(0, 1));
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

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.searchFindByType(query, SolrTypeManager.convertToNewType(type), new PageRequest(0, 50))
                .getContent();
    }

    public Page<Eresource> searchType(final String type, final String query, final Pageable pageRequest) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        return this.repository.searchFindByType(query, SolrTypeManager.convertToNewType(type), pageRequest);
    }

    public Page<Eresource> searchWithFilters(final String query, final String facets, final Pageable pageRequest) {
        return this.repository.searchFindAllWithFilter(query, facetStringToFilters(facets), pageRequest);
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